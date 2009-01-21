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
import java.util.List;

import org.sakaiproject.sms.hibernate.logic.impl.HibernateLogicFactory;
import org.sakaiproject.sms.hibernate.model.SmsAccount;
import org.sakaiproject.sms.hibernate.model.SmsMessage;
import org.sakaiproject.sms.hibernate.model.SmsTask;
import org.sakaiproject.sms.hibernate.model.SmsTransaction;
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

	/** The account. */
	public static SmsAccount account;

	static {
		HibernateUtil.createSchema();
		smsBillingImpl = new SmsBillingImpl();
		smsSmppImpl = new SmsSmppImpl();
		smsSmppImpl.init();

		account = new SmsAccount();
		account.setSakaiSiteId("sakaiSiteId");
		account.setMessageTypeCode("");
		account.setBalance(10f);
		account.setAccountName("account name");
		account.setStartdate(new Date());
		account.setAccountEnabled(true);
		HibernateLogicFactory.getAccountLogic().persistSmsAccount(account);

	}

	/** The test amount. */
	public final Float testAmount = 100F;

	/** The test credits. */
	public final int testCredits = 100;

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

	@Override
	protected void tearDown() throws Exception {
	}

	/**
	 * Test check sufficient credits are not available.
	 */
	public void testCheckSufficientCredits_False() {

		int creditsRequired = 11;

		SmsMessage msg = new SmsMessage();
		SmsTask smsTask = new SmsTask();
		smsTask.setSakaiSiteId("sakaiSiteId");
		smsTask.setSmsAccountId(account.getId());
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
				account.getId(), creditsRequired);
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
		smsTask.setSmsAccountId(account.getId());
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
				account.getId(), creditsRequired);
		assertFalse("Expected insufficient credit", sufficientCredits);

		// Add overdraft to account
		account.setOverdraftLimit(10f);
		HibernateLogicFactory.getAccountLogic().persistSmsAccount(account);

		sufficientCredits = smsBillingImpl.checkSufficientCredits(account
				.getId(), creditsRequired);
		assertTrue("Expected sufficient credit", sufficientCredits);
	}

	/**
	 * Test check sufficient credits are available.
	 */
	public void testCheckSufficientCredits_True() {

		int creditsRequired = 5;

		SmsMessage msg = new SmsMessage();
		SmsTask smsTask = new SmsTask();
		smsTask.setSakaiSiteId("sakaiSiteId");
		smsTask.setSmsAccountId(account.getId());
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
				account.getId(), creditsRequired);
		assertTrue("Expected sufficient credit", sufficientCredits);
	}

	/**
	 * Test convert amount to credits.
	 */
	public void testConvertAmountToCredits() {
		float amount = smsBillingImpl.convertCreditsToAmount(testCredits);
		int credits = smsBillingImpl.convertAmountToCredits(amount);
		assertTrue(credits == testCredits);
	}

	/**
	 * Test convert credits to amount.
	 */
	public void testConvertCreditsToAmount() {
		int credits = smsBillingImpl.convertAmountToCredits(testAmount);
		// Not sure how to test this properly.
	}

	/**
	 * Test recalculate account balance.
	 */
	public void testRecalculateAccountBalance() {
		SmsAccount smsAccount = new SmsAccount();
		smsAccount.setSakaiUserId("1");
		smsAccount.setSakaiSiteId("1");
		smsAccount.setMessageTypeCode("1");
		smsAccount.setOverdraftLimit(10000.00f);
		smsAccount.setBalance(10f);
		smsAccount.setAccountName("accountname");
		smsAccount.setAccountEnabled(true);
		HibernateLogicFactory.getAccountLogic().persistSmsAccount(smsAccount);
		insertTestTransactionsForAccount(smsAccount);

		assertTrue(smsAccount.exists());

		List<SmsTransaction> transactions = HibernateLogicFactory
				.getTransactionLogic().getSmsTransactionsForAccountId(
						smsAccount.getId());

		assertNotNull(transactions);
		assertTrue(transactions.size() > 0);

		smsBillingImpl.recalculateAccountBalance(smsAccount.getId());

		SmsAccount recalculatedAccount = HibernateLogicFactory
				.getAccountLogic().getSmsAccount(smsAccount.getId());
		assertNotNull(recalculatedAccount);
		assertTrue(recalculatedAccount.getBalance() == 0);
	}

	/**
	 * Test recalculate account balances.
	 */
	public void testRecalculateAccountBalances() {
		// Recalculate all the account balances
		smsBillingImpl.recalculateAccountBalances();
		List<SmsAccount> smsAccounts = HibernateLogicFactory.getAccountLogic()
				.getAllSmsAccounts();
		assertNotNull(smsAccounts);
		assertTrue(smsAccounts.size() > 0);
		for (SmsAccount account : smsAccounts) {
			// We know that the only accounts exist are the ones created in this
			// test case
			// and they should all have a balance of 0 after recalculation.
			assertTrue(account.getBalance() == 0);
		}
	}

	// /////////////////////////////////////////////////////////////////////////////////////////////
	// HELPER METHODS
	// /////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Gets the sms transactions.
	 * 
	 * @param smsAccount
	 *            the sms account
	 * 
	 * @return the sms transactions
	 */
	private void insertTestTransactionsForAccount(SmsAccount smsAccount) {

		int g = 10000;
		for (int i = 0; i < 10; i++) {
			SmsTransaction smsTransaction = new SmsTransaction();
			smsTransaction.setBalance(1.32f);
			smsTransaction.setSakaiUserId("sakaiUserId" + i);
			smsTransaction.setTransactionDate(new Date(System
					.currentTimeMillis()
					+ g));
			smsTransaction.setTransactionTypeCode("TC");
			smsTransaction.setTransactionCredits(666);

			if (i % 2 == 0) {
				smsTransaction.setTransactionAmount(1000.00f);
			} else {
				smsTransaction.setTransactionAmount(-1000.00f);
			}
			smsTransaction.setSmsAccount(smsAccount);
			smsTransaction.setSmsTaskId(1L);
			g += 1000;
			HibernateLogicFactory.getTransactionLogic()
					.insertCreditTransaction(smsTransaction);
		}
	}

	public void testReserveCredits() {
		fail();
	}

	public void testSettleCreditDifference() {
		fail();
	}

}
