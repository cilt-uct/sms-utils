package org.sakaiproject.sms.hibernate.test;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestSuite;

import org.sakaiproject.sms.hibernate.bean.SearchFilterBean;
import org.sakaiproject.sms.hibernate.logic.SmsAccountLogic;
import org.sakaiproject.sms.hibernate.logic.SmsTransactionLogic;
import org.sakaiproject.sms.hibernate.logic.impl.SmsAccountLogicImpl;
import org.sakaiproject.sms.hibernate.logic.impl.SmsTransactionLogicImpl;
import org.sakaiproject.sms.hibernate.logic.impl.exception.SmsSearchException;
import org.sakaiproject.sms.hibernate.model.SmsAccount;
import org.sakaiproject.sms.hibernate.model.SmsTransaction;
import org.sakaiproject.sms.hibernate.util.DateUtil;
import org.sakaiproject.sms.hibernate.util.HibernateUtil;

public class SmsTransactionTest extends AbstractBaseTestCase {

	/**
	 * This is used for one time setup and tear down per test case.
	 * 
	 * @return the test
	 */
	public static Test suite() {

		TestSetup setup = new TestSetup(new TestSuite(SmsTransactionTest.class)) {

			protected void setUp() throws Exception {
				HibernateUtil.createSchema();
			}

			protected void tearDown() throws Exception {

			}
		};
		return setup;
	}

	private static SmsTransactionLogic logic = null;

	private static SmsAccountLogic accountLogic;

	private static SmsTransaction insertSmsTransaction;

	private static SmsAccount insertSmsAccount;

	static {
		logic = new SmsTransactionLogicImpl();
		accountLogic = new SmsAccountLogicImpl();

		insertSmsAccount = new SmsAccount();
		insertSmsAccount.setSakaiUserId("SakaiUSerId");
		insertSmsAccount.setSakaiSiteId("SakaiSiteId");
		insertSmsAccount.setMessageTypeCode("12345");
		insertSmsAccount.setOverdraftLimit(10000.00f);
		insertSmsAccount.setBalance(5000.00f);
		accountLogic.persistSmsAccount(insertSmsAccount);

		insertSmsTransaction = new SmsTransaction();
		insertSmsTransaction.setBalance(1.32f);
		insertSmsTransaction.setSakaiUserId("sakaiUserId");
		insertSmsTransaction.setTransactionDate(new Timestamp(System
				.currentTimeMillis()));
		insertSmsTransaction.setTransactionTypeCode("TC");
		insertSmsTransaction.setTransactionCredits(666);
		insertSmsTransaction.setTransactionAmount(1000.00f);

		insertSmsTransaction.setSmsAccount(insertSmsAccount);
	}

	public SmsTransactionTest() {
	}

	public SmsTransactionTest(String name) {
		super(name);
	}

	public void testInsertSmsTransaction() {
		logic.persistSmsTransaction(insertSmsTransaction);
		// Check the record was created on the DB... an id will be assigned.
		assertTrue("Object not persisted", insertSmsTransaction.exists());
	}

	public void testGetSmsTransactionById() {
		SmsTransaction getSmsTransaction = logic
				.getSmsTransaction(insertSmsTransaction.getId());
		assertTrue("Object not persisted", insertSmsTransaction.exists());
		assertNotNull(getSmsTransaction);
		assertEquals(insertSmsTransaction, getSmsTransaction);
	}

	public void testUpdateSmsTransaction() {
		SmsTransaction smsTransaction = logic
				.getSmsTransaction(insertSmsTransaction.getId());
		smsTransaction.setSakaiUserId("newSakaiUserId");
		logic.persistSmsTransaction(smsTransaction);
		smsTransaction = logic.getSmsTransaction(insertSmsTransaction.getId());
		assertEquals("newSakaiUserId", smsTransaction.getSakaiUserId());
	}

	public void testGetSmsTransactions() {
		List<SmsTransaction> transactions = logic.getAllSmsTransactions();
		assertNotNull("Returnend collection is null", transactions);
		assertTrue("No records returned", transactions.size() > 0);
	}

	/**
	 * Tests the getMessagesForCriteria method
	 */
	public void testGetTransactionsForCriteria() {

		SmsAccount insertSmsAccount = new SmsAccount();
		insertSmsAccount.setSakaiUserId("SakaiUSerId");
		insertSmsAccount.setSakaiSiteId("SakaiSiteId");
		insertSmsAccount.setMessageTypeCode("12345");
		insertSmsAccount.setOverdraftLimit(10000.00f);
		insertSmsAccount.setBalance(5000.00f);

		SmsTransaction insertSmsTransaction = new SmsTransaction();
		insertSmsTransaction.setBalance(1.32f);
		insertSmsTransaction.setSakaiUserId("sakaiUserId");

		insertSmsTransaction.setTransactionDate(new Timestamp(System
				.currentTimeMillis()));
		insertSmsTransaction.setTransactionTypeCode("TTC");
		insertSmsTransaction.setTransactionCredits(666);
		insertSmsTransaction.setTransactionAmount(1000.00f);

		insertSmsTransaction.setSmsAccount(insertSmsAccount);
		insertSmsAccount.getSmsTransactions().add(insertSmsTransaction);
		try {
			accountLogic.persistSmsAccount(insertSmsAccount);
			assertTrue("Object not created successfullyu", insertSmsTransaction
					.exists());

			SearchFilterBean bean = new SearchFilterBean();
			bean.setTransactionType(insertSmsTransaction
					.getTransactionTypeCode());
			bean.setAccountNumber(insertSmsTransaction.getSmsAccount().getId());
			bean.setDateFrom(DateUtil.getDateString(new Date()));
			bean.setDateTo(DateUtil.getDateString(new Date()));
			bean.setSender(insertSmsTransaction.getSakaiUserId());

			List<SmsTransaction> transactions = logic
					.getSmsTransactionsForCriteria(bean);
			assertTrue("Collection returned has no objects", transactions
					.size() > 0);

			for (SmsTransaction transaction : transactions) {
				// We know that only one transaction should be returned
				assertEquals(transaction, insertSmsTransaction);
			}
		} catch (SmsSearchException se) {
			fail(se.getMessage());
		} finally {
			accountLogic.deleteSmsAccount(insertSmsAccount);
		}
	}

	public void testDeleteSmsTransaction() {
		insertSmsTransaction.setSmsAccount(null);
		logic.deleteSmsTransaction(insertSmsTransaction);
		SmsTransaction getSmsTransaction = logic
				.getSmsTransaction(insertSmsTransaction.getId());
		assertNull(getSmsTransaction);
		assertNull("Object not removed", getSmsTransaction);
	}

}
