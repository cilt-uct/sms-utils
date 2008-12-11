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
package org.sakaiproject.test;

import java.sql.Timestamp;
import java.util.Calendar;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.log4j.Level;
import org.sakaiproject.sms.hibernate.logic.impl.SmsMessageLogicImpl;
import org.sakaiproject.sms.hibernate.logic.impl.SmsTaskLogicImpl;
import org.sakaiproject.sms.hibernate.model.SmsTask;
import org.sakaiproject.sms.hibernate.model.constants.SmsConst_DeliveryStatus;
import org.sakaiproject.sms.hibernate.model.constants.SmsConst_SmscDeliveryStatus;
import org.sakaiproject.sms.impl.SmsCoreImpl;
import org.sakaiproject.sms.impl.SmsSmppImpl;

public class SmsCoreTest extends TestCase {

	static SmsSmppImpl smsSmppImpl = null;
	static SmsCoreImpl smsCoreImpl = null;
	private final static org.apache.log4j.Logger LOG = org.apache.log4j.Logger
			.getLogger(SmsCoreTest.class);

	// The setup method instanciates the smsCoreImpl once. The teardown
	// method safely calls disconnectGateWay at the end of the test
	public static Test suite() {

		TestSetup setup = new TestSetup(new TestSuite(SmsCoreTest.class)) {

			protected void setUp() throws Exception {
				smsCoreImpl = new SmsCoreImpl();
				smsSmppImpl = new SmsSmppImpl();
				smsSmppImpl.setSmsMessageLogic(new SmsMessageLogicImpl());
				smsSmppImpl.init();
				smsCoreImpl.setSmsSmpp(smsSmppImpl);
				smsCoreImpl.setSmsTaskLogic(new SmsTaskLogicImpl());
				smsCoreImpl.enableDebugInformation(true);
				LOG.setLevel(Level.ALL);
			}

			protected void tearDown() throws Exception {
				smsSmppImpl.disconnectGateWay();
			}

		};
		return setup;
	}

	/*
	 * This is an helper method to insert a dummy smsTask into the Database. The
	 * sakaiID is used to identify the temp task.
	 */
	public SmsTask insertNewTask(String sakaiID, String status,
			Timestamp dateToSend, int attemptCount) {
		SmsTask insertTask = new SmsTask();
		insertTask.setSakaiSiteId(sakaiID);
		insertTask.setSmsAccountId(0);
		insertTask.setDateCreated(new Timestamp(System.currentTimeMillis()));
		insertTask.setDateToSend(dateToSend);
		insertTask.setStatusCode(status);
		insertTask.setAttemptCount(0);
		insertTask.setMessageBody("testing1234567");
		insertTask.setSenderUserName("administrator");
		smsCoreImpl.getSmsTaskLogic().persistSmsTask(insertTask);
		return insertTask;
	}

	/*
	 * In this test the populating of the task messages is tested. The test
	 * succeeds if the smsTask's message count is > 0.
	 */
	public void testGetDeliveryGroup() {

		SmsTask smsTask = new SmsTask();
		smsTask.setMessageBody("tesing sms");
		smsTask.setSmsMessagesOnTask(smsCoreImpl.getDeliveryGroup("1234566789",
				"group1", smsTask));
		assertEquals(true, smsTask.getSmsMessages() != null
				&& smsTask.getSmsMessages().size() > 0);

	}

	/*
	 * In this test the smsc (gateway) is not bound (disconnected). The task is
	 * executed 5 times to simulate the scheduler retrying and eventually
	 * failing.
	 */
	public void testProcessTaskFail() {
		SmsTask smsTask = insertNewTask("testProcessTaskFail",
				SmsConst_DeliveryStatus.STATUS_PENDING, new Timestamp(System
						.currentTimeMillis()), 1);
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
		assertEquals(true, smsTask.getStatusCode().equals(
				SmsConst_DeliveryStatus.STATUS_FAIL));
		assertEquals(true, smsTask.getAttemptCount() == 5);
		smsCoreImpl.getSmsTaskLogic().deleteSmsTask(smsTask);
		LOG.info("Reconnecting to server after fail test ");
		smsSmppImpl.connectToGateway();
	}

	/*
	 * In this test the updating of smsStatuses is tested. First a new task is
	 * created and populated with smsMessages.The total number of pending
	 * messages must equal 0 at the end.The total sent messages must equal the
	 * total messages on the task.
	 */
	public void testMessageStatusUpdate() {
		smsSmppImpl.setLogLevel(Level.OFF);
		if (smsCoreImpl.getSmsSmpp().getConnectionStatus()) {
			SmsTask smsTask = insertNewTask("testMessageStatusUpdate",
					SmsConst_DeliveryStatus.STATUS_PENDING, new Timestamp(
							System.currentTimeMillis()), 0);
			smsTask.setSmsMessagesOnTask(smsCoreImpl.getDeliveryGroup(
					"1234566789", "group1", smsTask));
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

	/*
	 * In this test the ProcessNextTask method is tested. 4 smsTasks are created
	 * with different sending times and statuses.The ProcessNextTask method must
	 * pick up the oldest SmsTask with an (pending/incomplete/reply) status. The
	 * test succeeds if the Smstasks are returned in the proper order.
	 * 
	 * NOTE: Make sure that the SMS_TASK table is empty before running this
	 * test, else it will fail.
	 */
	public void testProcessNextTask() {
		smsSmppImpl.setLogLevel(Level.ALL);
		if (smsCoreImpl.getSmsSmpp().getConnectionStatus()) {

			Calendar now = Calendar.getInstance();
			SmsTask smsTask3 = insertNewTask("smsTask3",
					SmsConst_DeliveryStatus.STATUS_PENDING, new Timestamp(now
							.getTimeInMillis()), 0);
			now.add(Calendar.MINUTE, -15);
			SmsTask smsTask2 = insertNewTask("smsTask2",
					SmsConst_DeliveryStatus.STATUS_INCOMPLETE, new Timestamp(
							now.getTimeInMillis()), 0);
			now.add(Calendar.MINUTE, -55);
			SmsTask smsTask1 = insertNewTask("smsTask1",
					SmsConst_DeliveryStatus.STATUS_PENDING, new Timestamp(now
							.getTimeInMillis()), 0);
			now.add(Calendar.MINUTE, 120);
			SmsTask smsTask4 = insertNewTask("smsTask4",
					SmsConst_DeliveryStatus.STATUS_RETRY, new Timestamp(now
							.getTimeInMillis()), 0);
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
				Thread.sleep(25000);

			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			SmsTask SmsTask1Test = smsCoreImpl.smsTaskLogic.getSmsTask(smsTask1
					.getId());
			SmsTask SmsTask2Test = smsCoreImpl.smsTaskLogic.getSmsTask(smsTask2
					.getId());
			SmsTask SmsTask3Test = smsCoreImpl.smsTaskLogic.getSmsTask(smsTask3
					.getId());
			SmsTask SmsTask4Test = smsCoreImpl.smsTaskLogic.getSmsTask(smsTask4
					.getId());

			assertEquals(true, SmsTask1Test.getMessagesWithSmscStatus(
					SmsConst_SmscDeliveryStatus.ENROUTE).size() == 0);
			assertEquals(true, SmsTask1Test.getMessagesWithStatus(
					SmsConst_DeliveryStatus.STATUS_PENDING).size() == 0);
			assertEquals(true, SmsTask2Test.getMessagesWithSmscStatus(
					SmsConst_SmscDeliveryStatus.ENROUTE).size() == 0);
			assertEquals(true, SmsTask2Test.getMessagesWithStatus(
					SmsConst_DeliveryStatus.STATUS_PENDING).size() == 0);
			assertEquals(true, SmsTask3Test.getMessagesWithSmscStatus(
					SmsConst_SmscDeliveryStatus.ENROUTE).size() == 0);
			assertEquals(true, SmsTask3Test.getMessagesWithStatus(
					SmsConst_DeliveryStatus.STATUS_PENDING).size() == 0);

			smsCoreImpl.getSmsTaskLogic().deleteSmsTask(SmsTask1Test);
			smsCoreImpl.getSmsTaskLogic().deleteSmsTask(SmsTask2Test);
			smsCoreImpl.getSmsTaskLogic().deleteSmsTask(SmsTask3Test);
			smsCoreImpl.getSmsTaskLogic().deleteSmsTask(SmsTask4Test);
		}
	}
}
