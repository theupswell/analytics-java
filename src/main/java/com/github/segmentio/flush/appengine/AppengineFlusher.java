package com.github.segmentio.flush.appengine;

import static com.google.appengine.api.taskqueue.DeferredTaskContext.setDoNotRetry;
import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withPayload;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.github.segmentio.AnalyticsClient;
import com.github.segmentio.Constants;
import com.github.segmentio.flush.IBatchFactory;
import com.github.segmentio.models.BasePayload;
import com.github.segmentio.models.Batch;
import com.github.segmentio.request.IRequester;
import com.google.appengine.api.taskqueue.DeferredTask;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.common.collect.ImmutableList;

public class AppengineFlusher
{
	private static final Logger logger = Logger.getLogger(Constants.LOGGER);

	// We have only a single Flusher available, so we just use the id of "FLUSHER";
	private static final String FLUSHER_ID = "FLUSHER_ID";
	private static final Map<String, AppengineFlusher> APP_ENGINE_ASYNC_FLUSHER = new HashMap();

	/**
	 * Identifier of the async connection.
	 */
	private final String asyncConnectionId;
	private final AnalyticsClient client;

	/**
	 * Queue used to send deferred tasks.
	 */
	private Queue queue = QueueFactory.getDefaultQueue();
	/**
	 * Boolean used to check whether the connection is still open or not.
	 */
	private boolean closed;

	private final IBatchFactory factory;
	private final IRequester requester;

	/**
	 * Required-args Constructor.
	 * 
	 * @param client
	 * @param factory
	 * @param requester
	 */
	public AppengineFlusher(AnalyticsClient client, IBatchFactory factory, IRequester requester)
	{
		this.asyncConnectionId = FLUSHER_ID;
		APP_ENGINE_ASYNC_FLUSHER.put(asyncConnectionId, this);
		this.client = client;

		this.factory = factory;
		this.requester = requester;
	}

	/**
	 * The basePayload will be added to a queue and will be handled by a separate {@code Thread} later on.
	 */
	public void enqueue(BasePayload basePayload)
	{
		if (!closed)
		{
			queue.add(withPayload(new EventSubmitter(basePayload)));
			this.client.getStatistics().updateInserted(1);
		}
	}

	/**
	 *
	 * Closing the {@link AppengineFlusher} will gracefully remove every task created earlier from the queue.
	 */
	public void close()
	{
		logger.info("Gracefully stopping Segment tasks.");
		closed = true;
		APP_ENGINE_ASYNC_FLUSHER.remove(asyncConnectionId);
	}

	/**
	 * Set the queue used to send EventSubmitter tasks.
	 *
	 * @param queueName name of the queue to use.
	 */
	public void setQueue(String queueName)
	{
		this.queue = QueueFactory.getQueue(queueName);
	}

	/**
	 * Simple DeferredTask using the that sends a BasePayload to Segment.
	 */
	private static final class EventSubmitter implements DeferredTask
	{
		private final BasePayload basePayload;

		private EventSubmitter(BasePayload basePayload)
		{
			this.basePayload = basePayload;
		}

		@Override
		public void run()
		{
			setDoNotRetry(true);
			try
			{
				// The current thread is managed by raven
				AppengineFlusher flusher = APP_ENGINE_ASYNC_FLUSHER.get(FLUSHER_ID);
				if (flusher == null)
				{
					logger.warning("Couldn't find the AppengineFlusher identified by " + FLUSHER_ID
						+ ". The connection has probably been closed.");
					return;
				}

				Batch batch = flusher.factory.create(ImmutableList.of(basePayload));
				flusher.requester.send(batch);
				logger.info("Initiated batch request with Batch: " + batch);
				flusher.client.getStatistics().updateDropped(1);
			}
			catch (Exception e)
			{
				logger.log(Level.SEVERE, "An exception occurred while sending the event to Segement.", e);
			}

		}
	}

}
