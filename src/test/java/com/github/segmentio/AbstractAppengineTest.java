package com.github.segmentio;

import org.apache.commons.lang3.StringUtils;

import com.google.appengine.api.taskqueue.dev.LocalTaskQueueCallback;
import com.google.appengine.tools.development.testing.LocalBlobstoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalCapabilitiesServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalImagesServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalMailServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalMemcacheServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalModulesServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalRdbmsServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalTaskQueueTestConfig;
import com.google.appengine.tools.development.testing.LocalURLFetchServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalXMPPServiceTestConfig;

/**
 * Abstract test class for AppEngine
 */
public abstract class AbstractAppengineTest
{
	private LocalServiceTestHelper helper = null;

	// Unlike CountDownLatch, TaskCountDownlatch lets us reset.
	protected final LocalTaskQueueTestConfig.TaskCountDownLatch latch = new LocalTaskQueueTestConfig.TaskCountDownLatch(
		1);

	/**
	 * Initialize the Appengine test harness for a particular JUnit test.
	 *
	 * @throws Exception
	 */
	protected void setUpAppengineInternal(final String queueXMLPath,
			final Class<? extends LocalTaskQueueCallback> localTaskQueueCallbackClass) throws Exception
	{

		final LocalTaskQueueTestConfig localTaskQueueConfig = initLocalTaskQueues(queueXMLPath,
			localTaskQueueCallbackClass);

		final LocalModulesServiceTestConfig localModulesServiceTestConfig = new LocalModulesServiceTestConfig()
			.addDefaultModuleVersion().addBasicScalingModuleVersion("sampleModule", "1-0-0", 1);

		// See
		// http://code.google.com/appengine/docs/java/tools/localunittesting.html#Writing_HRD_Datastore_Tests
		final LocalDatastoreServiceTestConfig hrdConfig = new LocalDatastoreServiceTestConfig()
			.setDefaultHighRepJobPolicyUnappliedJobPercentage(0.1f).setNoStorage(true);

		// ///////////////////////////
		// Create the Helper
		// ///////////////////////////

		helper = new LocalServiceTestHelper(new LocalBlobstoreServiceTestConfig(),
			new LocalCapabilitiesServiceTestConfig(), new LocalImagesServiceTestConfig(),
			new LocalMailServiceTestConfig(), new LocalMemcacheServiceTestConfig(), new LocalRdbmsServiceTestConfig(),
			localTaskQueueConfig, new LocalURLFetchServiceTestConfig(), new LocalUserServiceTestConfig(),
			new LocalXMPPServiceTestConfig(), localModulesServiceTestConfig, hrdConfig);

		helper.setUp();
	}

	/**
	 * Initializes and returns the Local TaskQueue Test Queues.
	 *
	 * @param queueXMLPath
	 * @param localTaskQueueCallbackClass
	 * @return
	 */
	protected LocalTaskQueueTestConfig initLocalTaskQueues(String queueXMLPath,
			final Class<? extends LocalTaskQueueCallback> localTaskQueueCallbackClass)
	{

		if (StringUtils.isBlank(queueXMLPath))
		{
			queueXMLPath = "war/WEB-INF/queue.xml";
		}

		// Create the LocalTaskQueueConfig with the latch and callback
		// information. See
		// http://code.google.com/appengine/docs/java/tools/localunittesting.html#Writing_Task_Queue_Tests
		final LocalTaskQueueTestConfig localTaskQueueConfig = new LocalTaskQueueTestConfig()
			.setDisableAutoTaskExecution(false).setQueueXmlPath(queueXMLPath).setTaskExecutionLatch(latch);

		if (localTaskQueueCallbackClass != null)
		{
			localTaskQueueConfig.setCallbackClass(localTaskQueueCallbackClass);
		}
		else
		{
			localTaskQueueConfig.setCallbackClass(LocalTaskQueueTestConfig.DeferredTaskCallback.class);
		}

		return localTaskQueueConfig;
	}

}
