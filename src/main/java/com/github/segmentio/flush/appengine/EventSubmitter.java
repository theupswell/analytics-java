package com.github.segmentio.flush.appengine;

import com.google.appengine.api.taskqueue.DeferredTask;

/**
 * An implementation of {@link DeferredTask} for submitting events to Segment.
 */
public class EventSubmitter implements DeferredTask
{
	@Override
	public void run()
	{

	}
}
