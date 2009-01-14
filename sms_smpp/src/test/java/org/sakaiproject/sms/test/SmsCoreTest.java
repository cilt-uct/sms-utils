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
import java.util.Set;

import org.apache.log4j.Level;
import org.sakaiproject.sms.hibernate.logic.impl.HibernateLogicFactory;
import org.sakaiproject.sms.hibernate.model.SmsMessage;
import org.sakaiproject.sms.hibernate.model.SmsTask;
import org.sakaiproject.sms.hibernate.model.constants.SmsConst_DeliveryStatus;
import org.sakaiproject.sms.hibernate.model.constants.SmsConst_SmscDeliveryStatus;
import org.sakaiproject.sms.hibernate.model.constants.SmsHibernateConstants;
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
		smsSmppImpl.init();
		smsSmppImpl.setLogLevel(Level.ALL);
		smsCoreImpl.setSmsSmpp(smsSmppImpl);
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
			SmsTask smsTask3 = smsCoreImpl.getPreliminaryTask(
					"testProcessNextTask-smsTask3", new Date(now
							.getTimeInMillis()),
					"testProcessNextTask-smsTask3",
					SmsHibernateConstants.SMS_DEV_DEFAULT_SAKAI_ID, null);

			smsTask3.setStatusCode(SmsConst_DeliveryStatus.STATUS_PENDING);

			now.add(Calendar.MINUTE, -1);
			SmsTask smsTask2 = smsCoreImpl.getPreliminaryTask(
					"testProcessNextTask-smsTask2", new Date(now
							.getTimeInMillis()),
					"testProcessNextTask-smsTask2MessageBody",
					SmsHibernateConstants.SMS_DEV_DEFAULT_SAKAI_ID, null);
			smsTask2.setStatusCode(SmsConst_DeliveryStatus.STATUS_PENDING);

			now.add(Calendar.MINUTE, -3);
			SmsTask smsTask1 = smsCoreImpl.getPreliminaryTask(
					"testProcessNextTask-smsTask1", new Date(now
							.getTimeInMillis()),
					"testProcessNextTask-smsTask1MessageBody",
					SmsHibernateConstants.SMS_DEV_DEFAULT_SAKAI_ID, null);

			smsTask1.setStatusCode(SmsConst_DeliveryStatus.STATUS_PENDING);

			now.add(Calendar.MINUTE, 60);
			SmsTask smsTask4 = smsCoreImpl.getPreliminaryTask(
					"testProcessNextTask-smsTask4", new Date(now
							.getTimeInMillis()),
					"testProcessNextTask-smsTask4MessageBody",
					SmsHibernateConstants.SMS_DEV_DEFAULT_SAKAI_ID, null);

			smsTask4.setStatusCode(SmsConst_DeliveryStatus.STATUS_PENDING);
			smsCoreImpl.insertTask(smsTask3);
			smsCoreImpl.insertTask(smsTask2);
			smsCoreImpl.insertTask(smsTask1);
			smsCoreImpl.insertTask(smsTask4);

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

			SmsTask smsTask1Update = HibernateLogicFactory.getTaskLogic()
					.getSmsTask(smsTask1.getId());
			SmsTask smsTask2Update = HibernateLogicFactory.getTaskLogic()
					.getSmsTask(smsTask2.getId());
			SmsTask smsTask3Update = HibernateLogicFactory.getTaskLogic()
					.getSmsTask(smsTask3.getId());
			SmsTask smsTask4Update = HibernateLogicFactory.getTaskLogic()
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
	 * In this test the updating of the tasks statusses are tested.
	 */
	public void testTaskStatuses() {
		smsSmppImpl.connectToGateway();
		smsSmppImpl.setLogLevel(Level.ALL);
		if (smsCoreImpl.getSmsSmpp().getConnectionStatus()) {

			Calendar now = Calendar.getInstance();
			now.add(Calendar.MINUTE, -1);
			SmsTask smsTask2 = smsCoreImpl.getPreliminaryTask(
					"TestTaskStatuses-SusscessFullTask", new Date(now
							.getTimeInMillis()),
					"TestTaskStatuses-SmsTask2MessageBody",
					SmsHibernateConstants.SMS_DEV_DEFAULT_SAKAI_ID, null);

			smsTask2.setMaxTimeToLive(300);
			now.add(Calendar.MINUTE, -3);
			SmsTask smsTask1 = smsCoreImpl.getPreliminaryTask(
					"TestTaskStatuses-ExpiresTask", new Date(now
							.getTimeInMillis()),
					"TestTaskStatuses-ExpiresTask",
					SmsHibernateConstants.SMS_DEV_DEFAULT_SAKAI_ID, null);
			smsTask1.setMaxTimeToLive(60);

			smsCoreImpl.insertTask(smsTask2);
			smsCoreImpl.insertTask(smsTask1);

			assertEquals(true, smsTask1.getId().equals(
					smsCoreImpl.getNextSmsTask().getId()));
			smsCoreImpl.processNextTask();
			assertEquals(true, smsTask2.getId().equals(
					smsCoreImpl.getNextSmsTask().getId()));
			smsCoreImpl.processNextTask();

			// we give the delivery reports time to get back.
			try {
				Thread.sleep(15000);

			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			SmsTask smsTask1Update = HibernateLogicFactory.getTaskLogic()
					.getSmsTask(smsTask1.getId());
			SmsTask smsTask2Update = HibernateLogicFactory.getTaskLogic()
					.getSmsTask(smsTask2.getId());

			assertEquals(true, smsTask1Update.getMessagesWithSmscStatus(
					SmsConst_SmscDeliveryStatus.ENROUTE).size() == 0);
			assertEquals(smsTask1Update.getStatusCode().equals(
					SmsConst_DeliveryStatus.STATUS_EXPIRE), true);

			assertEquals(smsTask2Update.getStatusCode().equals(
					SmsConst_DeliveryStatus.STATUS_SENT), true);
			assertEquals(true, smsTask2Update.getMessagesWithSmscStatus(
					SmsConst_SmscDeliveryStatus.ENROUTE).size() == 0);
			assertEquals(true, smsTask2Update.getMessagesWithStatus(
					SmsConst_DeliveryStatus.STATUS_PENDING).size() == 0);

		}
	}

	/**
	 * In this test the smsc (gateway) is not bound (disconnected). The task is
	 * executed 5 times to simulate the scheduler retrying and eventually
	 * failing.
	 */
	public void testProcessTaskFail() {
		smsSmppImpl.connectToGateway();
		SmsTask smsTask = smsCoreImpl.getPreliminaryTask("testProcessTaskFail",
				new Date(System.currentTimeMillis()),
				"testProcessTaskFailMessageBody",
				SmsHibernateConstants.SMS_DEV_DEFAULT_SAKAI_ID, null);
		smsCoreImpl.insertTask(smsTask);
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
		SmsTask smsTaskUpdate = HibernateLogicFactory.getTaskLogic()
				.getSmsTask(smsTask.getId());
		assertEquals(true, smsTaskUpdate.getStatusCode().equals(
				SmsConst_DeliveryStatus.STATUS_FAIL));
		assertEquals(true, smsTaskUpdate.getAttemptCount() == 5);
		assertEquals(true, smsTaskUpdate.getMessagesWithStatus(
				SmsConst_DeliveryStatus.STATUS_FAIL).size() == (smsTask
				.getSmsMessages().size()));

		HibernateLogicFactory.getTaskLogic().deleteSmsTask(smsTask);
		LOG.info("Reconnecting to server after fail test ");
		smsSmppImpl.connectToGateway();
	}

	public void testProcessIncomingMessage() {
		smsSmppImpl.connectToGateway();
		SmsMessage smsMessage = HibernateLogicFactory.getMessageLogic()
				.getNewTestSmsMessageInstance("Mobile number", "Message body");
		smsCoreImpl.processIncomingMessage(smsMessage);
	}

	/**
	 * In this test the updating of smsStatuses is tested. First a new task is
	 * created and populated with smsMessages.The total number of pending
	 * messages must equal 0 at the end.The total sent messages must equal the
	 * total messages on the task.Secondly a new task is created and the
	 * delivery report lister is switched off. After 1 min the core service must
	 * mark all the messages on the task as timedout.The test is successful if a
	 * timedout message is found.
	 */
	public void testTimeoutAndMessageStatusUpdate() {
		smsSmppImpl.connectToGateway();
		smsSmppImpl.setLogLevel(Level.ALL);
		if (smsCoreImpl.getSmsSmpp().getConnectionStatus()) {
			SmsTask statusUpdateTask = smsCoreImpl.getPreliminaryTask(
					"TestTimeoutAndMessageStatusUpdate-StatusUpdateTask",
					new Date(System.currentTimeMillis()),
					"TestTimeoutAndMessageStatusUpdate-StatusUpdateTask",
					SmsHibernateConstants.SMS_DEV_DEFAULT_SAKAI_ID, null);
			statusUpdateTask
					.setStatusCode(SmsConst_DeliveryStatus.STATUS_PENDING);
			statusUpdateTask.setAttemptCount(0);
			statusUpdateTask.setDateProcessed(new Date());
			statusUpdateTask.setSmsMessagesOnTask(smsCoreImpl
					.generateSmsMessages(statusUpdateTask));

			LOG.info("SMS-messages on task: "
					+ statusUpdateTask.getSmsMessages().size());
			LOG.info("SMS-messages Pending: "
					+ statusUpdateTask.getMessagesWithStatus(
							SmsConst_DeliveryStatus.STATUS_PENDING).size());
			LOG.info("Sending Messages To Gateway");
			statusUpdateTask
					.setMessageTypeId(SmsHibernateConstants.SMS_TASK_TYPE_PROCESS_NOW);
			smsCoreImpl.insertTask(statusUpdateTask);
			smsSmppImpl
					.sendMessagesToGateway(statusUpdateTask.getSmsMessages());
			LOG.info("SMS-messages Pending: "
					+ statusUpdateTask.getMessagesWithStatus(
							SmsConst_DeliveryStatus.STATUS_PENDING).size());
			LOG.info("SMS-messages STATUS_SENT: "
					+ statusUpdateTask.getMessagesWithStatus(
							SmsConst_DeliveryStatus.STATUS_SENT).size());
			assertEquals(true, statusUpdateTask.getMessagesWithStatus(
					SmsConst_DeliveryStatus.STATUS_PENDING).size() == 0);

			SmsTask timeOutTask = smsCoreImpl.getPreliminaryTask(
					"testTimeoutAndMessageStatusUpdate-TIMEOUT", new Date(),
					"testTimeoutAndMessageStatusUpdate-TIMEOUT",
					SmsHibernateConstants.SMS_DEV_DEFAULT_SAKAI_ID, null);
			timeOutTask.setDelReportTimeoutDuration(60);
			timeOutTask.setSmsMessagesOnTask(smsCoreImpl
					.generateSmsMessages(timeOutTask));
			smsCoreImpl.insertTask(timeOutTask);
			smsSmppImpl.getSession().setMessageReceiverListener(null);
			smsCoreImpl.processNextTask();

			try {
				Thread.sleep(60000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			smsCoreImpl.processTimedOutDeliveryReports();
			SmsTask smsTask3Update = HibernateLogicFactory.getTaskLogic()
					.getSmsTask(timeOutTask.getId());

			Set<SmsMessage> smsMessages = smsTask3Update.getSmsMessages();
			boolean timedOutMessagesFound = false;
			for (SmsMessage message : smsMessages) {
				if (message.getStatusCode().equals(
						SmsConst_DeliveryStatus.STATUS_TIMEOUT)) {
					timedOutMessagesFound = true;
					break;
				}

			}
			assertEquals(timedOutMessagesFound, true);

		}
	}

}
