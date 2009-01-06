/***********************************************************************************
 * SmsCoreTest.java
 * Copyright (c) 2008 Sakai Project/Sakai Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.s
 *
 **********************************************************************************/
package org.sakaiproject.sms.test;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Level;
import org.sakaiproject.sms.hibernate.logic.impl.SmsConfigLogicImpl;
import org.sakaiproject.sms.hibernate.logic.impl.SmsMessageLogicImpl;
import org.sakaiproject.sms.hibernate.logic.impl.SmsTaskLogicImpl;
import org.sakaiproject.sms.hibernate.model.SmsMessage;
import org.sakaiproject.sms.hibernate.model.SmsTask;
import org.sakaiproject.sms.hibernate.model.constants.SmsConst_DeliveryStatus;
import org.sakaiproject.sms.hibernate.model.constants.SmsConst_SmscDeliveryStatus;
import org.sakaiproject.sms.hibernate.util.AbstractBaseTestCase;
import org.sakaiproject.sms.hibernate.util.HibernateUtil;
import org.sakaiproject.sms.impl.SmsBillingImpl;
import org.sakaiproject.sms.impl.SmsCoreImpl;
import org.sakaiproject.sms.impl.SmsSmppImpl;

/**
 * This test also send messages to the smpp simulator but it check the specific
 * statuses of sent messages. It also test the retrieval of the next sms task
 * from the SMS_TASK table.
 * 
 * @author etienne@psybergate.co.za
 * 
 */

public class SmsCoreTest extends AbstractBaseTestCase {

	static SmsSmppImpl smsSmppImpl = null;
	static SmsCoreImpl smsCoreImpl = null;

	private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger
			.getLogger(SmsCoreTest.class);

	static {
		HibernateUtil.createSchema();
		smsCoreImpl = new SmsCoreImpl();
		smsSmppImpl = new SmsSmppImpl();
		smsCoreImpl.setSmsBilling(new SmsBillingImpl());
		smsSmppImpl.setSmsMessageLogic(new SmsMessageLogicImpl());
		smsSmppImpl.setSmsTaskLogic(new SmsTaskLogicImpl());
		smsSmppImpl.init();
		smsCoreImpl.setSmsSmpp(smsSmppImpl);
		smsCoreImpl.setSmsTaskLogic(new SmsTaskLogicImpl());
		smsCoreImpl.setSmsConfigLogic(new SmsConfigLogicImpl());
		smsCoreImpl.enableDebugInformation(true);
		LOG.setLevel(Level.ALL);
	}

	public SmsCoreTest() {
	}

	public SmsCoreTest(String name) {
		super(name);
	}

	/**
	 * The tearDown method safely calls disconnectGateWay at the end of every
	 * test.
	 */
	@Override
	protected void tearDown() throws Exception {
		smsSmppImpl.disconnectGateWay();
	}

	/**
	 * In this test the ProcessNextTask method is tested. 4 smsTasks are created
	 * with different sending times and statuses. The ProcessNextTask method
	 * must pick up the oldest SmsTask with an (pending/incomplete/reply)
	 * status. The test succeeds if the Smstasks are returned in the proper
	 * order and the correct amount of delivery reports were received.
	 * 
	 * NOTE: Make sure that the SMS_TASK table is empty before running this
	 * test, else it will fail.
	 */
	public void testProcessNextTask() {
		smsSmppImpl.connectToGateway();
		smsSmppImpl.setLogLevel(Level.ALL);
		if (smsCoreImpl.getSmsSmpp().getConnectionStatus()) {

			Calendar now = Calendar.getInstance();
			SmsTask smsTask3 = smsCoreImpl.insertNewTask("smsTask3", new Date(
					now.getTimeInMillis()), "smsTask3", null);

			smsTask3.setStatusCode(SmsConst_DeliveryStatus.STATUS_PENDING);

			now.add(Calendar.MINUTE, -15);
			SmsTask smsTask2 = smsCoreImpl.insertNewTask("smsTask2", new Date(
					now.getTimeInMillis()), "smsTask2MessageBody", null);
			smsTask2.setStatusCode(SmsConst_DeliveryStatus.STATUS_PENDING);

			now.add(Calendar.MINUTE, -55);
			SmsTask smsTask1 = smsCoreImpl.insertNewTask("smsTask1", new Date(
					now.getTimeInMillis()), "smsTask1MessageBody", null);

			smsTask1.setStatusCode(SmsConst_DeliveryStatus.STATUS_PENDING);

			now.add(Calendar.MINUTE, 120);
			SmsTask smsTask4 = smsCoreImpl.insertNewTask("smsTask4", new Date(
					now.getTimeInMillis()), "smsTask4MessageBody", null);

			smsTask4.setStatusCode(SmsConst_DeliveryStatus.STATUS_PENDING);

			smsCoreImpl.getSmsTaskLogic().persistSmsTask(smsTask3);
			smsCoreImpl.getSmsTaskLogic().persistSmsTask(smsTask2);
			smsCoreImpl.getSmsTaskLogic().persistSmsTask(smsTask1);
			smsCoreImpl.getSmsTaskLogic().persistSmsTask(smsTask4);

			assertEquals(true, smsTask1.getId().equals(
					smsCoreImpl.getNextSmsTask().getId()));
			smsCoreImpl.processNextTask();
			assertEquals(true, smsTask2.getId().equals(
					smsCoreImpl.getNextSmsTask().getId()));
			smsCoreImpl.processNextTask();
			assertEquals(true, smsTask3.getId().equals(
					smsCoreImpl.getNextSmsTask().getId()));
			smsCoreImpl.processNextTask();
			assertEquals(true, smsCoreImpl.getNextSmsTask() == (null));

			// we give the delivery reports time to get back.
			try {
				Thread.sleep(30000);

			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			SmsTask smsTask1Update = smsCoreImpl.smsTaskLogic
					.getSmsTask(smsTask1.getId());
			SmsTask smsTask2Update = smsCoreImpl.smsTaskLogic
					.getSmsTask(smsTask2.getId());
			SmsTask smsTask3Update = smsCoreImpl.smsTaskLogic
					.getSmsTask(smsTask3.getId());
			SmsTask smsTask4Update = smsCoreImpl.smsTaskLogic
					.getSmsTask(smsTask4.getId());

			assertEquals(true, smsTask1Update.getMessagesWithSmscStatus(
					SmsConst_SmscDeliveryStatus.ENROUTE).size() == 0);
			assertEquals(true, smsTask1Update.getMessagesWithStatus(
					SmsConst_DeliveryStatus.STATUS_PENDING).size() == 0);
			assertEquals(true, smsTask2Update.getMessagesWithSmscStatus(
					SmsConst_SmscDeliveryStatus.ENROUTE).size() == 0);
			assertEquals(true, smsTask2Update.getMessagesWithStatus(
					SmsConst_DeliveryStatus.STATUS_PENDING).size() == 0);
			assertEquals(true, smsTask3Update.getMessagesWithSmscStatus(
					SmsConst_SmscDeliveryStatus.ENROUTE).size() == 0);
			assertEquals(true, smsTask3Update.getMessagesWithStatus(
					SmsConst_DeliveryStatus.STATUS_PENDING).size() == 0);

		}
	}

	/**
	 * In this test the populating of the task messages is tested. The test
	 * succeeds if the smsTask's message count is > 0.
	 */
	public void testGenerateSmsMessages() {

		SmsTask smsTask = new SmsTask();
		Set<String> userIds = new HashSet<String>();
		smsTask.setMessageBody("tesing sms");
		smsTask.setSakaiSiteId("sakaiSiteId");
		smsTask.setDeliveryGroupId("deliveryGroupId");
		// smsTask.setSmsMessagesOnTask(smsCoreImpl.generateSmsMessages(smsTask,
		// null));
		// assertEquals(true, smsTask.getSmsMessages() != null
		// && smsTask.getSmsMessages().size() > 0);

		userIds.add("Sakaiuser1");
		userIds.add("Sakaiuser2");
		userIds.add("Sakaiuser3");
		userIds.add("Sakaiuser4");

		smsTask.setSmsMessagesOnTask(smsCoreImpl.generateSmsMessages(smsTask,
				userIds));

		for (SmsMessage message : smsTask.getSmsMessages()) {
			assertTrue(userIds.contains(message.getSakaiUserId()));

		}

	}

	/**
	 * In this test the smsc (gateway) is not bound (disconnected). The task is
	 * executed 5 times to simulate the scheduler retrying and eventually
	 * failing.
	 */
	public void testProcessTaskFail() {
		smsSmppImpl.connectToGateway();
		SmsTask smsTask = smsCoreImpl.insertNewTask("testProcessTaskFail",
				new Date(System.currentTimeMillis()),
				"testProcessTaskFailMessageBody", null);
		smsTask.setStatusCode(SmsConst_DeliveryStatus.STATUS_PENDING);
		smsTask.setAttemptCount(0);
		smsSmppImpl.setLogLevel(Level.OFF);
		LOG.info("Disconnecting from server for fail test ");
		smsSmppImpl.disconnectGateWay();
		for (int i = 0; i < 5; i++) {
			smsCoreImpl.processTask(smsTask);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		SmsTask smsTaskUpdate = smsCoreImpl.getSmsTaskLogic().getSmsTask(
				smsTask.getId());
		assertEquals(true, smsTaskUpdate.getStatusCode().equals(
				SmsConst_DeliveryStatus.STATUS_FAIL));
		assertEquals(true, smsTaskUpdate.getAttemptCount() == 5);
		assertEquals(true, smsTaskUpdate.getMessagesWithStatus(
				SmsConst_DeliveryStatus.STATUS_FAIL).size() == (smsTask
				.getSmsMessages().size()));

		smsCoreImpl.getSmsTaskLogic().deleteSmsTask(smsTask);
		LOG.info("Reconnecting to server after fail test ");
		smsSmppImpl.connectToGateway();
	}

	/**
	 * In this test the updating of smsStatuses is tested. First a new task is
	 * created and populated with smsMessages.The total number of pending
	 * messages must equal 0 at the end.The total sent messages must equal the
	 * total messages on the task.
	 */
	public void testMessageStatusUpdate() {
		smsSmppImpl.connectToGateway();
		smsSmppImpl.setLogLevel(Level.OFF);
		if (smsCoreImpl.getSmsSmpp().getConnectionStatus()) {
			SmsTask smsTask = smsCoreImpl.insertNewTask(
					"testMessageStatusUpdate", new Date(System
							.currentTimeMillis()),
					"testMessageStatusUpdateMessageBody", null);
			smsTask.setStatusCode(SmsConst_DeliveryStatus.STATUS_PENDING);
			smsTask.setAttemptCount(0);
			smsTask.setSmsMessagesOnTask(smsCoreImpl.generateSmsMessages(
					smsTask, null));

			LOG
					.info("SMS-messages on task: "
							+ smsTask.getSmsMessages().size());
			LOG.info("SMS-messages Pending: "
					+ smsTask.getMessagesWithStatus(
							SmsConst_DeliveryStatus.STATUS_PENDING).size());
			LOG.info("Sending Messages To Gateway");
			smsSmppImpl.sendMessagesToGateway(smsTask.getSmsMessages());
			LOG.info("SMS-messages Pending: "
					+ smsTask.getMessagesWithStatus(
							SmsConst_DeliveryStatus.STATUS_PENDING).size());
			LOG.info("SMS-messages STATUS_SENT: "
					+ smsTask.getMessagesWithStatus(
							SmsConst_DeliveryStatus.STATUS_SENT).size());
			assertEquals(true, smsTask.getMessagesWithStatus(
					SmsConst_DeliveryStatus.STATUS_PENDING).size() == 0);

			smsCoreImpl.getSmsTaskLogic().deleteSmsTask(smsTask);
		}
	}
}
