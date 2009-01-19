package org.sakaiproject.sms.hibernate.test;

import java.util.Date;
import java.util.List;

import org.sakaiproject.sms.hibernate.bean.SearchFilterBean;
import org.sakaiproject.sms.hibernate.bean.SearchResultContainer;
import org.sakaiproject.sms.hibernate.logic.impl.HibernateLogicFactory;
import org.sakaiproject.sms.hibernate.logic.impl.exception.SmsAccountNotFoundException;
import org.sakaiproject.sms.hibernate.model.SmsAccount;
import org.sakaiproject.sms.hibernate.model.SmsTransaction;
import org.sakaiproject.sms.hibernate.model.constants.SmsHibernateConstants;
import org.sakaiproject.sms.hibernate.util.AbstractBaseTestCase;
import org.sakaiproject.sms.hibernate.util.HibernateUtil;

/**
 * The Class SmsTransactionTest.
 */
public class SmsTransactionTest extends AbstractBaseTestCase {

	/** The insert sms transaction. */
	private static SmsTransaction insertSmsTransaction;

	/** The insert sms account. */
	private static SmsAccount insertSmsAccount;

	static {
		HibernateUtil.createSchema();

		insertSmsAccount = new SmsAccount();
		insertSmsAccount.setSakaiUserId("SakaiUSerId");
		insertSmsAccount.setSakaiSiteId("SakaiSiteId");
		insertSmsAccount.setMessageTypeCode("12345");
		insertSmsAccount.setOverdraftLimit(10000.00f);
		insertSmsAccount.setBalance(5000.00f);
		insertSmsAccount.setAccountName("accountname");
		insertSmsAccount.setAccountEnabled(true);

		insertSmsTransaction = new SmsTransaction();
		insertSmsTransaction.setBalance(1.32f);
		insertSmsTransaction.setSakaiUserId("sakaiUserId");
		insertSmsTransaction.setTransactionDate(new Date(System
				.currentTimeMillis()));
		insertSmsTransaction.setTransactionTypeCode("TC");
		insertSmsTransaction.setTransactionCredits(666);
		insertSmsTransaction.setTransactionAmount(1000.00f);

		insertSmsTransaction.setSmsAccount(insertSmsAccount);
		insertSmsTransaction.setSmsTaskId(1L);
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
		HibernateLogicFactory.getAccountLogic().persistSmsAccount(
				insertSmsAccount);
		HibernateLogicFactory.getTransactionLogic().persistSmsTransaction(
				insertSmsTransaction);
		// Check the record was created on the DB... an id will be assigned.
		assertTrue("Object not persisted", insertSmsTransaction.exists());
	}

	/**
	 * Test get sms transaction by id.
	 */
	public void testGetSmsTransactionById() {
		SmsTransaction getSmsTransaction = HibernateLogicFactory
				.getTransactionLogic().getSmsTransaction(
						insertSmsTransaction.getId());
		assertTrue("Object not persisted", insertSmsTransaction.exists());
		assertNotNull(getSmsTransaction);
		assertEquals(insertSmsTransaction, getSmsTransaction);
	}

	/**
	 * Test update sms transaction.
	 */
	public void testUpdateSmsTransaction() {
		SmsTransaction smsTransaction = HibernateLogicFactory
				.getTransactionLogic().getSmsTransaction(
						insertSmsTransaction.getId());
		smsTransaction.setSakaiUserId("newSakaiUserId");
		HibernateLogicFactory.getTransactionLogic().persistSmsTransaction(
				smsTransaction);
		smsTransaction = HibernateLogicFactory.getTransactionLogic()
				.getSmsTransaction(insertSmsTransaction.getId());
		assertEquals("newSakaiUserId", smsTransaction.getSakaiUserId());
	}

	/**
	 * Test get sms transactions.
	 */
	public void testGetSmsTransactions() {
		List<SmsTransaction> transactions = HibernateLogicFactory
				.getTransactionLogic().getAllSmsTransactions();
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
		insertSmsAccount.setAccountEnabled(true);

		SmsTransaction insertSmsTransaction = new SmsTransaction();
		insertSmsTransaction.setBalance(1.32f);
		insertSmsTransaction.setSakaiUserId("sakaiUserId");
		insertSmsTransaction.setTransactionDate(new Date(System
				.currentTimeMillis()));
		insertSmsTransaction.setTransactionTypeCode("TTC");
		insertSmsTransaction.setTransactionCredits(666);
		insertSmsTransaction.setTransactionAmount(1000.00f);
		insertSmsTransaction.setSmsTaskId(1L);

		insertSmsTransaction.setSmsAccount(insertSmsAccount);
		insertSmsAccount.getSmsTransactions().add(insertSmsTransaction);

		try {
			HibernateLogicFactory.getAccountLogic().persistSmsAccount(
					insertSmsAccount);
			assertTrue("Object not created successfully", insertSmsTransaction
					.exists());

			SearchFilterBean bean = new SearchFilterBean();
			bean.setTransactionType(insertSmsTransaction
					.getTransactionTypeCode());
			bean.setNumber(insertSmsTransaction.getSmsAccount().getId()
					.toString());
			bean.setDateFrom(new Date());
			bean.setDateTo(new Date());
			bean.setSender(insertSmsTransaction.getSakaiUserId());

			List<SmsTransaction> transactions = HibernateLogicFactory
					.getTransactionLogic().getPagedSmsTransactionsForCriteria(bean)
					.getPageResults();
			assertTrue("Collection returned has no objects", transactions
					.size() > 0);

			for (SmsTransaction transaction : transactions) {
				// We know that only one transaction should be returned
				assertEquals(transaction, insertSmsTransaction);
			}
		} catch (Exception se) {
			fail(se.getMessage());
		}
		SmsAccount account = HibernateLogicFactory.getAccountLogic()
				.getSmsAccount(insertSmsAccount.getId());
		HibernateLogicFactory.getAccountLogic().deleteSmsAccount(account);
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
		smsAccount.setAccountEnabled(true);
		HibernateLogicFactory.getAccountLogic().persistSmsAccount(smsAccount);

		for (int i = 0; i < recordsToInsert; i++) {

			SmsTransaction smsTransaction = new SmsTransaction();
			smsTransaction.setBalance(1.32f);
			smsTransaction.setSakaiUserId("sakaiUserId");
			smsTransaction.setTransactionDate(new Date(System
					.currentTimeMillis()));
			smsTransaction.setTransactionTypeCode("TC");
			smsTransaction.setTransactionCredits(i);
			smsTransaction.setTransactionAmount(1000.00f);
			smsTransaction.setSmsAccount(smsAccount);
			smsTransaction.setSmsTaskId(1L);

			HibernateLogicFactory.getTransactionLogic().persistSmsTransaction(
					smsTransaction);
		}
		try {

			SearchFilterBean bean = new SearchFilterBean();
			bean.setNumber(smsAccount.getId().toString());
			bean.setDateFrom(new Date());
			bean.setDateTo(new Date());
			bean.setTransactionType("TC");
			bean.setSender("sakaiUserId");

			bean.setCurrentPage(2);

			SearchResultContainer<SmsTransaction> con = HibernateLogicFactory
					.getTransactionLogic().getPagedSmsTransactionsForCriteria(bean);
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

			con = HibernateLogicFactory.getTransactionLogic()
					.getPagedSmsTransactionsForCriteria(bean);
			tasks = con.getPageResults();
			// int lastPageRecordCount = recordsToInsert % pages;
			int lastPageRecordCount = recordsToInsert
					- (pages * SmsHibernateConstants.DEFAULT_PAGE_SIZE);
			assertTrue("Incorrect collection size returned",
					tasks.size() == lastPageRecordCount);

		} catch (Exception se) {
			fail(se.getMessage());
		}
	}
	
	public void testCreateReserveCreditsTransactionNoAccountFound() throws Exception {
		
		try{			
			HibernateLogicFactory.getTransactionLogic().reserveCredits(123L, 123L, 110);
			fail("Insert should fail since there is no account with id 123");
		}
		catch (SmsAccountNotFoundException expected) {
		}
		catch (Exception notExpected) {
			fail("An account not found exception should be thrown");
		}
	}	
		
	public void testCreateReserveCreditsTask(){
	
		SmsAccount testAccount = SmsAccountTest.createTestAccount();
		testAccount.setBalance(1000f);
		HibernateLogicFactory.getAccountLogic().persistSmsAccount(testAccount);
		
		try{			
			HibernateLogicFactory.getTransactionLogic().reserveCredits(100L,  testAccount.getId(), 110);
		}
		catch (Exception notExpected) {
			fail("Transaction should save successfully" + notExpected);
		}
	}

	public void testCreateCancelTransaction() throws Exception {
	
		SmsAccount testAccount = SmsAccountTest.createTestAccount();	
		HibernateLogicFactory.getAccountLogic().persistSmsAccount(testAccount);
	
		try{			
			HibernateLogicFactory.getTransactionLogic().cancelTransaction(101L,  testAccount.getId());
		}
		catch (Exception notExpected) {
			fail("Transaction should save successfully" + notExpected);
		}
		
	
	}

		/**
	 * Test delete sms transaction.
	 */
	public void testDeleteSmsTransaction() {
		HibernateLogicFactory.getTransactionLogic().deleteSmsTransaction(
				insertSmsTransaction);
		SmsTransaction getSmsTransaction = HibernateLogicFactory
				.getTransactionLogic().getSmsTransaction(
						insertSmsTransaction.getId());
		assertNull(getSmsTransaction);
		assertNull("Object not removed", getSmsTransaction);
	}

}
