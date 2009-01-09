/***********************************************************************************
 * SmsBillingTest.java
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
import java.util.Date;

import org.sakaiproject.sms.hibernate.logic.impl.SmsAccountLogicImpl;
import org.sakaiproject.sms.hibernate.logic.impl.SmsMessageLogicImpl;
import org.sakaiproject.sms.hibernate.logic.impl.SmsTaskLogicImpl;
import org.sakaiproject.sms.hibernate.model.SmsAccount;
import org.sakaiproject.sms.hibernate.model.SmsMessage;
import org.sakaiproject.sms.hibernate.model.SmsTask;
import org.sakaiproject.sms.hibernate.model.constants.SmsConst_DeliveryStatus;
import org.sakaiproject.sms.hibernate.util.AbstractBaseTestCase;
import org.sakaiproject.sms.hibernate.util.HibernateUtil;
import org.sakaiproject.sms.impl.SmsBillingImpl;
import org.sakaiproject.sms.impl.SmsSmppImpl;

// TODO: Auto-generated Javadoc
/**
 * Tests the billing mthods.
 */
public class SmsBillingTest extends AbstractBaseTestCase {

	/** The sms smpp impl. */
	private static SmsSmppImpl smsSmppImpl = null;

	/** The sms billing impl. */
	private static SmsBillingImpl smsBillingImpl = null;

	/** The sms task logic impl. */
	private static SmsTaskLogicImpl smsTaskLogicImpl = null;

	/** The account logic. */
	private static SmsAccountLogicImpl accountLogic;

	/** The account. */
	public static SmsAccount account;

	/**
	 * Instantiates a new sms billing test.
	 */
	public SmsBillingTest() {
	}

	/**
	 * Instantiates a new sms billing test.
	 * 
	 * @param name
	 *            the name
	 */
	public SmsBillingTest(String name) {
		super(name);
	}

	static {
		HibernateUtil.createSchema();
		accountLogic = new SmsAccountLogicImpl();
		smsBillingImpl = new SmsBillingImpl();
		smsTaskLogicImpl = new SmsTaskLogicImpl();
		smsSmppImpl = new SmsSmppImpl();
		smsSmppImpl.setSmsMessageLogic(new SmsMessageLogicImpl());
		smsSmppImpl.setSmsTaskLogic(new SmsTaskLogicImpl());
		smsSmppImpl.init();

		account = new SmsAccount();
		account.setSakaiSiteId("sakaiSiteId");
		account.setMessageTypeCode("");
		account.setBalance(10f);
		account.setAccountName("account name");
		account.setStartdate(new Date());
		account.setAccountEnabled(true);
		accountLogic.persistSmsAccount(account);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
	}

	/**
	 * Test check sufficient credits are available.
	 */
	public void testCheckSufficientCredits_True() {

		int creditsRequired = 5;

		SmsMessage msg = new SmsMessage();
		SmsTask smsTask = new SmsTask();
		smsTask.setSakaiSiteId("sakaiSiteId");
		smsTask.setSmsAccountId(account.getId().intValue());
		smsTask.setDateCreated(new Timestamp(System.currentTimeMillis()));
		smsTask.setDateToSend(new Timestamp(System.currentTimeMillis()));
		smsTask.setStatusCode(SmsConst_DeliveryStatus.STATUS_PENDING);
		smsTask.setAttemptCount(2);
		smsTask.setMessageBody("messageBody");
		smsTask.setSenderUserName("senderUserName");
		smsTask.setMaxTimeToLive(1);
		smsTask.setDelReportTimeoutDuration(1);
		smsTask.getSmsMessages().add(msg);
		smsTask.setCreditEstimate(creditsRequired);
		msg.setSmsTask(smsTask);

		boolean sufficientCredits = smsBillingImpl.checkSufficientCredits(
				account.getId().intValue(), creditsRequired);
		assertTrue("Expected sufficient credit", sufficientCredits);
	}

	/**
	 * Test check sufficient credits are not available.
	 */
	public void testCheckSufficientCredits_False() {

		int creditsRequired = 11;

		SmsMessage msg = new SmsMessage();
		SmsTask smsTask = new SmsTask();
		smsTask.setSakaiSiteId("sakaiSiteId");
		smsTask.setSmsAccountId(account.getId().intValue());
		smsTask.setDateCreated(new Timestamp(System.currentTimeMillis()));
		smsTask.setDateToSend(new Timestamp(System.currentTimeMillis()));
		smsTask.setStatusCode(SmsConst_DeliveryStatus.STATUS_PENDING);
		smsTask.setAttemptCount(2);
		smsTask.setMessageBody("messageBody");
		smsTask.setSenderUserName("senderUserName");
		smsTask.setMaxTimeToLive(1);
		smsTask.setDelReportTimeoutDuration(1);
		smsTask.getSmsMessages().add(msg);
		smsTask.setCreditEstimate(creditsRequired);
		msg.setSmsTask(smsTask);

		boolean sufficientCredits = smsBillingImpl.checkSufficientCredits(
				account.getId().intValue(), creditsRequired);
		assertFalse("Expected insufficient credit", sufficientCredits);
	}

	/**
	 * Test check sufficient credits are available from overdraft.
	 */
	public void testCheckSufficientCredits_OverdraftFacility() {

		int creditsRequired = 11;

		SmsMessage msg = new SmsMessage();
		SmsTask smsTask = new SmsTask();
		smsTask.setSakaiSiteId("sakaiSiteId");
		smsTask.setSmsAccountId(account.getId().intValue());
		smsTask.setDateCreated(new Timestamp(System.currentTimeMillis()));
		smsTask.setDateToSend(new Timestamp(System.currentTimeMillis()));
		smsTask.setStatusCode(SmsConst_DeliveryStatus.STATUS_PENDING);
		smsTask.setAttemptCount(2);
		smsTask.setMessageBody("messageBody");
		smsTask.setSenderUserName("senderUserName");
		smsTask.setMaxTimeToLive(1);
		smsTask.setDelReportTimeoutDuration(1);
		smsTask.getSmsMessages().add(msg);
		smsTask.setCreditEstimate(creditsRequired);
		msg.setSmsTask(smsTask);

		boolean sufficientCredits = smsBillingImpl.checkSufficientCredits(
				account.getId().intValue(), creditsRequired);
		assertFalse("Expected insufficient credit", sufficientCredits);

		// Add overdraft to account
		account.setOverdraftLimit(10f);
		accountLogic.persistSmsAccount(account);

		sufficientCredits = smsBillingImpl.checkSufficientCredits(account
				.getId().intValue(), creditsRequired);
		assertTrue("Expected sufficient credit", sufficientCredits);
	}

	/**
	 * Test count billable messages.
	 */
	public void testCountBillableMessages() {
		SmsTask task = new SmsTask();
		task.setSakaiSiteId("sakaiSiteId");
		task.setSmsAccountId(account.getId().intValue());
		task.setDateCreated(new Date(System.currentTimeMillis()));
		task.setDateToSend(new Date(System.currentTimeMillis()));
		task.setStatusCode(SmsConst_DeliveryStatus.STATUS_DELIVERED);
		task.setAttemptCount(2);
		task.setMessageBody("messageCrit");
		task.setSenderUserName("messageCrit");
		task.setSakaiToolName("sakaiToolName");
		task.setMaxTimeToLive(1);
		task.setDelReportTimeoutDuration(1);
		SmsMessage message1 = new SmsMessage();
		message1.setMobileNumber("0721998919");
		message1.setSmscMessageId("billable");
		message1.setSakaiUserId("billable");
		message1.setDateDelivered(new Date(System.currentTimeMillis()));
		message1.setStatusCode(SmsConst_DeliveryStatus.STATUS_DELIVERED);
		SmsMessage message2 = new SmsMessage();
		message2.setMobileNumber("0721998919");
		message2.setSmscMessageId("billable2");
		message2.setSakaiUserId("billable2");
		message2.setDateDelivered(new Date(System.currentTimeMillis()));
		message2.setStatusCode(SmsConst_DeliveryStatus.STATUS_DELIVERED);
		message1.setSmsTask(task);
		message2.setSmsTask(task);
		task.getSmsMessages().add(message1);
		task.getSmsMessages().add(message2);
		smsTaskLogicImpl.persistSmsTask(task);

		int billableMessageCount = smsBillingImpl.countBillableMessages(task
				.getSakaiSiteId(), task.getDeliveryUserId(), task
				.getDeliveryGroupId(), 1);
		assertTrue("Expected two billable messages", billableMessageCount == 2);

	}

}
