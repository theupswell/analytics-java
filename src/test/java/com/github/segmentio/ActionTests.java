package com.github.segmentio;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ActionTests
{

	private AnalyticsClient client;

	@Before
	public void setup()
	{
		client = new AnalyticsClient(TestConstants.WRITE_KEY);
	}

	@Test
	public void testIdentify()
	{
		Actions.identify(client);
		Assert.assertEquals(1, client.getStatistics().getIdentifies().getCount());
		Assert.assertEquals(1, client.getStatistics().getSuccessful().getCount());
	}

	@Test
	public void testGroup()
	{
		Actions.group(client);
		Assert.assertEquals(1, client.getStatistics().getGroup().getCount());
		Assert.assertEquals(1, client.getStatistics().getSuccessful().getCount());
	}

	@Test
	public void testAlias()
	{
		Actions.alias(client);
		Assert.assertEquals(1, client.getStatistics().getAlias().getCount());
		Assert.assertEquals(1, client.getStatistics().getSuccessful().getCount());
	}

	@Test
	public void testTrack()
	{
		Actions.track(client);
		Assert.assertEquals(1, client.getStatistics().getTracks().getCount());
		Assert.assertEquals(1, client.getStatistics().getSuccessful().getCount());
	}

	@Test
	public void testPage()
	{
		Actions.page(client);
		Assert.assertEquals(1, client.getStatistics().getPage().getCount());
		Assert.assertEquals(1, client.getStatistics().getSuccessful().getCount());
	}

	@Test
	public void testScreen()
	{
		Actions.screen(client);
		Assert.assertEquals(1, client.getStatistics().getScreen().getCount());
		Assert.assertEquals(1, client.getStatistics().getSuccessful().getCount());
	}

	@After
	public void close()
	{
		client.close();
	}
}
