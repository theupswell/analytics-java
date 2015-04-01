package com.github.segmentio.flush;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.github.segmentio.AnalyticsClient;
import com.github.segmentio.Constants;
import com.github.segmentio.models.BasePayload;
import com.github.segmentio.models.Batch;
import com.github.segmentio.request.IRequester;
import com.github.segmentio.utils.ManualResetEvent;

public class Flusher extends Thread
{

	private static final Logger logger = Logger.getLogger(Constants.LOGGER);

	private LinkedBlockingQueue<BasePayload> queue;

	// volatile protects the flushing thread from caching the go variable in its
	// own thread context (register)
	// http://stackoverflow.com/questions/2423622/volatile-vs-static-in-java
	// http://stackoverflow.com/questions/4569338/how-is-thread-context-switching-done
	private volatile boolean go;
	/**
	 * An event that helps synchronize when the flusher is idle, so that clients can block until the flushing thread is
	 * done.
	 */
	private ManualResetEvent idle;

	private AnalyticsClient client;
	private IBatchFactory factory;
	private IRequester requester;

	public Flusher(AnalyticsClient client, IBatchFactory factory, IRequester requester)
	{
		this.client = client;
		this.factory = factory;
		this.requester = requester;
		this.queue = new LinkedBlockingQueue<BasePayload>();
		this.go = true;
		this.idle = new ManualResetEvent(true);
	}

	public void run()
	{
		while (go)
		{
			List<BasePayload> current = new LinkedList<BasePayload>();

			do
			{
				if (queue.size() == 0)
					idle.set();

				BasePayload payload = null;
				try
				{
					// wait half a second for an item to appear
					// otherwise yield to confirm that we aren't restarting
					payload = queue.poll(500, TimeUnit.MILLISECONDS);
				}
				catch (InterruptedException e)
				{
					logger.log(Level.SEVERE, "Interrupted while trying to flush analytics queue.", e);
				}

				if (payload != null)
				{
					// we are no longer idle since there's messages to be processed
					idle.reset();
					current.add(payload);
					client.getStatistics().updateQueued(this.queue.size());
				}
			}
			// keep iterating and collecting the current batch
			// while we're active, there's something in the queue, and we haven't already
			// over-filled this batch
			while (go && queue.size() > 0 && current.size() < Constants.BATCH_INCREMENT);

			sendBatch(current);

			try
			{
				// thread context switch to avoid resource contention
				Thread.sleep(0);
			}
			catch (InterruptedException e)
			{
				logger.log(Level.SEVERE, "Interrupted while sleeping flushing thread.", e);
			}
		}
	}

	private void sendBatch(List<BasePayload> current)
	{
		boolean success = true;
		int retryCount = 0;

		do
		{
			try
			{
				if (current.size() > 0)
				{
					// we have something to send in this batch
					logger.info(String.format("Preparing to send batch.. [%s items]", current.size()));
					Batch batch = factory.create(current);
					client.getStatistics().updateFlushAttempts(1);
					requester.send(batch);
					logger.info(String.format("Initiated batch request .. [%s items]", current.size()));
					current = new LinkedList<BasePayload>();
				}
				success = true;
			}
			catch (RuntimeException e)
			{
				// We will log and loop back around, so we
				logger.log(Level.SEVERE, "Unexpected error while sending batch, catching so we don't lose records", e);
				retryCount++;
				success = false;
			}
		}
		while (!success && retryCount < 3);

		if (!success)
		{
			logger.log(Level.SEVERE, "Unable to send batch after {} retries. Giving up on this batch.", retryCount);
		}

	}

	final static int QUEUE_WARNING_PCT_THRESHHOLD = 80;

	public void enqueue(BasePayload payload)
	{
		int maxQueueSize = client.getOptions().getMaxQueueSize();
		int currentQueueSize = queue.size();

		if (currentQueueSize <= maxQueueSize)
		{
			this.queue.add(payload);
			this.client.getStatistics().updateInserted(1);
			this.client.getStatistics().updateQueued(this.queue.size());

			//logQueueDepth(payload, currentQueueSize, maxQueueSize);
		}
		else
		{
			// the queue is too high, we can't risk memory overflow
			// add dropped message to statistics, but don't log
			// because the system is likely very resource strapped
			this.client.getStatistics().updateDropped(1);
			logger.log(Level.SEVERE, "Queue has reached maxSize, dropping payload.");
		}
	}

	public void flush()
	{
		try
		{
			idle.waitOne(2, TimeUnit.MINUTES);
		}
		catch (InterruptedException e)
		{
			logger.log(Level.SEVERE, "Interrupted while waiting for the thread to flush.", e);
		}
	}

	public void close()
	{
		go = false;

		queue.clear();
	}

	public int getQueueDepth()
	{
		return queue.size();
	}
}
