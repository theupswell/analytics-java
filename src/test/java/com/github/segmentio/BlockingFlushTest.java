package com.github.segmentio;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BlockingFlushTest extends AbstractAppengineTest
{

	private AnalyticsClient client;

	@Before
	public void setup() throws Exception
	{
		this.setUpAppengineInternal("queue.xml", null);
		client = new AnalyticsClient(TestConstants.WRITE_KEY);
	}

	@Test
	public void testBlockingFlush()
	{
		int trials = 50;
		for (int i = 0; i < trials; i += 1)
			Actions.random(client);
		Assert.assertEquals(trials, client.getStatistics().getInserted().getCount());
		Assert.assertEquals(trials, client.getStatistics().getSuccessful().getCount());
	}

	@After
	public void close()
	{
		client.close();
	}
}
