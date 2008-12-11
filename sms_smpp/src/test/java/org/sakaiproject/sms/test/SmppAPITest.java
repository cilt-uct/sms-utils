/***********************************************************************************
 * SmppAPITest.java
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

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import org.sakaiproject.sms.hibernate.logic.impl.SmsMessageLogicImpl;
import org.sakaiproject.sms.hibernate.logic.impl.SmsTaskLogicImpl;
import org.sakaiproject.sms.hibernate.model.SmsMessage;
import org.sakaiproject.sms.hibernate.model.SmsTask;
import org.sakaiproject.sms.hibernate.model.constants.SmsConst_DeliveryStatus;
import org.sakaiproject.sms.hibernate.model.constants.SmsConst_SmscDeliveryStatus;
import org.sakaiproject.sms.hibernate.util.AbstractBaseTestCase;
import org.sakaiproject.sms.hibernate.util.HibernateUtil;
import org.sakaiproject.sms.impl.SmsSmppImpl;

/**
 * Test some api function on the smpp api. For example successful connect and
 * disconnect to the remote gateway.Both multiple and single message sending is
 * tested.
 */
public class SmppAPITest extends AbstractBaseTestCase {

	private static SmsSmppImpl smsSmppImpl = null;
	private static SmsTaskLogicImpl smsTaskLogicImpl = null;

	public SmppAPITest() {
	}

	public SmppAPITest(String name) {
		super(name);
	}

	static {
		System.out.println("static setUp");
		HibernateUtil.createSchema();
		smsSmppImpl = new SmsSmppImpl();
		smsSmppImpl.setSmsMessageLogic(new SmsMessageLogicImpl());
		smsSmppImpl.init();

	}

	/*
	 * The tearDown method safely calls disconnectGateWay at the end of every
	 * test.
	 */
	protected void tearDown() throws Exception {
		smsSmppImpl.disconnectGateWay();

	}

	/**
	 * This is an helper method to insert a dummy smsTask into the Database. The
	 * sakaiID is used to identify the temp task.
	 * 
	 * @param sakaiID
	 * @param status
	 * @param dateToSend
	 * @param attemptCount
	 * @return
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
		smsTaskLogicImpl = new SmsTaskLogicImpl();
		smsTaskLogicImpl.persistSmsTask(insertTask);
		return insertTask;
	}

	/**
	 * Test to send 10 smsMessages to the SMSC The test succeeds if the returned
	 * status is STATUS_SENT and all the delivery reports are returned.
	 */
	public void testSendMessagesToGateway() {
		smsSmppImpl.connectToGateway();
		Set<SmsMessage> smsMessages = new HashSet<SmsMessage>();

		SmsTask insertTask = insertNewTask("testSendMessagesToGateway",
				SmsConst_DeliveryStatus.STATUS_PENDING, new Timestamp(System
						.currentTimeMillis()), 0);
		assertTrue("Task for message not created", insertTask.exists());

		for (int i = 0; i < 10; i++) {
			SmsMessage message = new SmsMessage();
			message.setMobileNumber("072199891" + i);
			message.setSakaiUserId("sakaiUserId");
			message.setStatusCode(SmsConst_DeliveryStatus.STATUS_PENDING);
			message.setSmsTask(insertTask);
			smsMessages.add(message);

		}
		insertTask.setSmsMessagesOnTask(smsMessages);
		smsTaskLogicImpl.persistSmsTask(insertTask);
		assertEquals(true, smsSmppImpl.sendMessagesToGateway(smsMessages)
				.equals(SmsConst_DeliveryStatus.STATUS_SENT));
		try {
			Thread.sleep(15000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		SmsTask insertTask1update = smsTaskLogicImpl.getSmsTask(insertTask
				.getId());
		assertEquals(true, insertTask1update.getMessagesWithSmscStatus(
				SmsConst_SmscDeliveryStatus.ENROUTE).size() == 0);
		smsTaskLogicImpl.deleteSmsTask(insertTask1update);
	}

	/**
	 * Test to send an single smsMessage to the SMSC.The test succeeds if an
	 * SmscID is populated on the message and a delivery report is returned.
	 */

	public void testSendMessageToGateway() {
		smsSmppImpl.connectToGateway();
		SmsTask insertTask1 = insertNewTask("testSendMessageToGateway2",
				SmsConst_DeliveryStatus.STATUS_PENDING, new Timestamp(System
						.currentTimeMillis()), 0);
		Set<SmsMessage> smsMessages = new HashSet<SmsMessage>();
		SmsMessage insertMessage1 = new SmsMessage();
		insertMessage1.setMobileNumber("0731998919");
		insertMessage1.setSakaiUserId("sakaiUserId");
		insertMessage1.setStatusCode(SmsConst_DeliveryStatus.STATUS_PENDING);
		insertMessage1.setSmsTask(insertTask1);
		smsMessages.add(insertMessage1);
		insertTask1.setSmsMessagesOnTask(smsMessages);
		smsTaskLogicImpl.persistSmsTask(insertTask1);
		assertEquals(true, smsSmppImpl.sendMessageToGateway(insertMessage1)
				.getSmscMessageId() != null);
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		SmsTask insertTask1update = smsTaskLogicImpl.getSmsTask(insertTask1
				.getId());
		assertEquals(true, insertTask1update.getMessagesWithSmscStatus(
				SmsConst_SmscDeliveryStatus.ENROUTE).size() == 0);
		smsTaskLogicImpl.deleteSmsTask(insertTask1update);

	}

	/**
	 * The gateway return some information to us.
	 */
	public void testGetGatewayInfo() {
		smsSmppImpl.connectToGateway();
		System.out.println(smsSmppImpl.getGatewayInfo());
	}

	/**
	 * Testing the connect and disconnecting from the smsc. The test succeeds if
	 * the correct connectionStatus is returned.
	 */

	public void testGetConnectionStatus() {

		smsSmppImpl.disconnectGateWay();
		assertEquals(true, (!smsSmppImpl.getConnectionStatus()));

		smsSmppImpl.connectToGateway();
		assertEquals(true, (smsSmppImpl.getConnectionStatus()));

	}

}
