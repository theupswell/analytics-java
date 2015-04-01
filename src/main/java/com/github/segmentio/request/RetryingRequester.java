package com.github.segmentio.request;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.github.segmentio.AnalyticsClient;
import com.github.segmentio.Constants;
import com.github.segmentio.models.Batch;

public class RetryingRequester extends BlockingRequester
{

	private static final Logger logger = Logger.getLogger(Constants.LOGGER);

	private int retries;
	private int backoff;

	public RetryingRequester(AnalyticsClient client)
	{
		super(client);
		retries = client.getOptions().getRetries();
		backoff = client.getOptions().getBackoff();
	}

	@Override
	public boolean send(Batch batch)
	{
		int attempts = 0;
		boolean success = super.send(batch);
		while (!success && attempts < retries)
		{
			attempts += 1;
			try
			{
				Thread.sleep(backoff);
			}
			catch (InterruptedException e)
			{
				logger.log(Level.WARNING, "Interrupted during backoff", e);
			}
			logger.info(String.format("Retrying request [attempt %s] ..", attempts));
			success = super.send(batch);
		}
		if (!success)
		{
			logger.log(Level.SEVERE, String.format("Unable to complete request after %s attempts", attempts));
		}
		return success;
	}

}
