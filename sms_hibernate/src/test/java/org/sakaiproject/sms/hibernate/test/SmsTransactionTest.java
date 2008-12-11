package org.sakaiproject.sms.hibernate.test;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.sakaiproject.sms.hibernate.bean.SearchFilterBean;
import org.sakaiproject.sms.hibernate.bean.SearchResultContainer;
import org.sakaiproject.sms.hibernate.logic.SmsAccountLogic;
import org.sakaiproject.sms.hibernate.logic.SmsTransactionLogic;
import org.sakaiproject.sms.hibernate.logic.impl.SmsAccountLogicImpl;
import org.sakaiproject.sms.hibernate.logic.impl.SmsTransactionLogicImpl;
import org.sakaiproject.sms.hibernate.model.SmsAccount;
import org.sakaiproject.sms.hibernate.model.SmsTransaction;
import org.sakaiproject.sms.hibernate.model.constants.SmsHibernateConstants;
import org.sakaiproject.sms.hibernate.util.HibernateUtil;

/**
 * The Class SmsTransactionTest.
 */
public class SmsTransactionTest extends AbstractBaseTestCase {

	/** The logic. */
	private static SmsTransactionLogic logic = null;

	/** The account logic. */
	private static SmsAccountLogic accountLogic;

	/** The insert sms transaction. */
	private static SmsTransaction insertSmsTransaction;

	/** The insert sms account. */
	private static SmsAccount insertSmsAccount;

	static {
		HibernateUtil.createSchema();

		logic = new SmsTransactionLogicImpl();
		accountLogic = new SmsAccountLogicImpl();

		insertSmsAccount = new SmsAccount();
		insertSmsAccount.setSakaiUserId("SakaiUSerId");
		insertSmsAccount.setSakaiSiteId("SakaiSiteId");
		insertSmsAccount.setMessageTypeCode("12345");
		insertSmsAccount.setOverdraftLimit(10000.00f);
		insertSmsAccount.setBalance(5000.00f);
		insertSmsAccount.setAccountName("accountname");

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

	/**
	 * Instantiates a new sms transaction test.
	 */
	public SmsTransactionTest() {
	}

	/**
	 * Instantiates a new sms transaction test.
	 * 
	 * @param name
	 *            the name
	 */
	public SmsTransactionTest(String name) {
		super(name);
	}

	/**
	 * Test insert sms transaction.
	 */
	public void testInsertSmsTransaction() {
		accountLogic.persistSmsAccount(insertSmsAccount);
		logic.persistSmsTransaction(insertSmsTransaction);
		// Check the record was created on the DB... an id will be assigned.
		assertTrue("Object not persisted", insertSmsTransaction.exists());
	}

	/**
	 * Test get sms transaction by id.
	 */
	public void testGetSmsTransactionById() {
		SmsTransaction getSmsTransaction = logic
				.getSmsTransaction(insertSmsTransaction.getId());
		assertTrue("Object not persisted", insertSmsTransaction.exists());
		assertNotNull(getSmsTransaction);
		assertEquals(insertSmsTransaction, getSmsTransaction);
	}

	/**
	 * Test update sms transaction.
	 */
	public void testUpdateSmsTransaction() {
		SmsTransaction smsTransaction = logic
				.getSmsTransaction(insertSmsTransaction.getId());
		smsTransaction.setSakaiUserId("newSakaiUserId");
		logic.persistSmsTransaction(smsTransaction);
		smsTransaction = logic.getSmsTransaction(insertSmsTransaction.getId());
		assertEquals("newSakaiUserId", smsTransaction.getSakaiUserId());
	}

	/**
	 * Test get sms transactions.
	 */
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
		insertSmsAccount.setAccountName("accountName");

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
			assertTrue("Object not created successfully", insertSmsTransaction
					.exists());

			SearchFilterBean bean = new SearchFilterBean();
			bean.setTransactionType(insertSmsTransaction
					.getTransactionTypeCode());
			bean.setNumber(insertSmsTransaction.getSmsAccount().getId().toString());
			bean.setDateFrom(new Date());
			bean.setDateTo(new Date());
			bean.setSender(insertSmsTransaction.getSakaiUserId());

			List<SmsTransaction> transactions = logic
					.getSmsTransactionsForCriteria(bean).getPageResults();
			assertTrue("Collection returned has no objects", transactions
					.size() > 0);

			for (SmsTransaction transaction : transactions) {
				// We know that only one transaction should be returned
				assertEquals(transaction, insertSmsTransaction);
			}
		} catch (Exception se) {
			fail(se.getMessage());
		}
		SmsAccount account = accountLogic.getSmsAccount(insertSmsAccount
				.getId());
		accountLogic.deleteSmsAccount(account);
		// accountLogic.deleteSmsAccount(insertSmsAccount);
	}

	/**
	 * Test get tasks for criteria_ paging.
	 */
	public void testGetTasksForCriteria_Paging() {

		int recordsToInsert = 93;

		SmsAccount smsAccount = new SmsAccount();
		smsAccount.setSakaiUserId("SakaiUSerId");
		smsAccount.setSakaiSiteId("SakaiSiteId");
		smsAccount.setMessageTypeCode("12345");
		smsAccount.setOverdraftLimit(10000.00f);
		smsAccount.setBalance(5000.00f);
		smsAccount.setAccountName("accountname");
		accountLogic.persistSmsAccount(smsAccount);

		for (int i = 0; i < recordsToInsert; i++) {

			SmsTransaction smsTransaction = new SmsTransaction();
			smsTransaction.setBalance(1.32f);
			smsTransaction.setSakaiUserId("sakaiUserId");
			smsTransaction.setTransactionDate(new Timestamp(System
					.currentTimeMillis()));
			smsTransaction.setTransactionTypeCode("TC");
			smsTransaction.setTransactionCredits(i);
			smsTransaction.setTransactionAmount(1000.00f);
			smsTransaction.setSmsAccount(smsAccount);

			logic.persistSmsTransaction(smsTransaction);
		}

		try {

			SearchFilterBean bean = new SearchFilterBean();
			bean.setNumber(smsAccount.getId().toString());
			bean.setDateFrom(new Date());
			bean.setDateTo(new Date());
			bean.setTransactionType("TC");
			bean.setSender("sakaiUserId");

			bean.setCurrentPage(2);

			SearchResultContainer<SmsTransaction> con = logic
					.getSmsTransactionsForCriteria(bean);
			List<SmsTransaction> tasks = con.getPageResults();
			assertTrue("Incorrect collection size returned",
					tasks.size() == SmsHibernateConstants.DEFAULT_PAGE_SIZE);

			// Test last page. We know there are 124 records to this should
			// return a list of 4
			int pages = recordsToInsert
					/ SmsHibernateConstants.DEFAULT_PAGE_SIZE;
			// set to last page
			if (recordsToInsert % SmsHibernateConstants.DEFAULT_PAGE_SIZE == 0) {
				bean.setCurrentPage(pages);
			} else {
				bean.setCurrentPage(pages + 1);
			}

			con = logic.getSmsTransactionsForCriteria(bean);
			tasks = con.getPageResults();
			int lastPageRecordCount = recordsToInsert % pages;
			assertTrue("Incorrect collection size returned",
					tasks.size() == lastPageRecordCount);

		} catch (Exception se) {
			fail(se.getMessage());
		}
	}

	/**
	 * Test delete sms transaction.
	 */
	public void testDeleteSmsTransaction() {
		logic.deleteSmsTransaction(insertSmsTransaction);
		SmsTransaction getSmsTransaction = logic
				.getSmsTransaction(insertSmsTransaction.getId());
		assertNull(getSmsTransaction);
		assertNull("Object not removed", getSmsTransaction);
	}

}
