package org.sakaiproject.sms.hibernate.test;

import java.util.Date;
import java.util.List;

import org.sakaiproject.sms.hibernate.logic.impl.HibernateLogicFactory;
import org.sakaiproject.sms.hibernate.model.SmsAccount;
import org.sakaiproject.sms.hibernate.model.SmsTransaction;
import org.sakaiproject.sms.hibernate.util.AbstractBaseTestCase;
import org.sakaiproject.sms.hibernate.util.HibernateUtil;

/**
 * The Class SmsAccountTest. Do some basic crud functions on the account table.
 */
public class SmsAccountTest extends AbstractBaseTestCase {

	/** The insert sms account. */
	private static SmsAccount insertSmsAccount;

	/** The insert sms transaction1. */
	private static SmsTransaction insertSmsTransaction1;

	/** The insert sms transaction2. */
	private static SmsTransaction insertSmsTransaction2;

	static {

		HibernateUtil.createSchema();

		insertSmsAccount = createTestAccount();

		insertSmsTransaction1 = new SmsTransaction();
		insertSmsTransaction1.setBalance(100.00f);
		insertSmsTransaction1.setSakaiUserId("SakaiUserId1");
		insertSmsTransaction1.setTransactionAmount(100.00f);
		insertSmsTransaction1.setTransactionCredits(100);
		insertSmsTransaction1.setTransactionDate(new Date(System
				.currentTimeMillis()));
		insertSmsTransaction1.setTransactionTypeCode("TTC");
		insertSmsTransaction1.setSmsTaskId(1L);

		insertSmsTransaction2 = new SmsTransaction();
		insertSmsTransaction2.setBalance(100.00f);
		insertSmsTransaction2.setSakaiUserId("SakaiUserId2");
		insertSmsTransaction2.setTransactionAmount(100.00f);
		insertSmsTransaction2.setTransactionCredits(100);
		insertSmsTransaction2.setTransactionDate(new Date(System
				.currentTimeMillis()));
		insertSmsTransaction2.setTransactionTypeCode("TTC");
		insertSmsTransaction2.setSmsTaskId(1L);
	}

	public static SmsAccount createTestAccount() {
		SmsAccount insertSmsAccount = new SmsAccount();
		insertSmsAccount.setSakaiUserId("SakaiUSerId");
		insertSmsAccount.setSakaiSiteId("SakaiSiteId");
		insertSmsAccount.setMessageTypeCode("12345");
		insertSmsAccount.setOverdraftLimit(10000.00f);
		insertSmsAccount.setBalance(5000.00f);
		insertSmsAccount.setAccountName("accountName");
		insertSmsAccount.setAccountEnabled(true);
		return insertSmsAccount;
	}

	/**
	 * Instantiates a new sms account test.
	 */
	public SmsAccountTest() {
	}

	/**
	 * Instantiates a new sms account test.
	 * 
	 * @param name
	 *            the name
	 */
	public SmsAccountTest(String name) {
		super(name);
	}

	/**
	 * Test insert sms account.
	 */
	public void testInsertSmsAccount() {
		HibernateLogicFactory.getAccountLogic().persistSmsAccount(
				insertSmsAccount);
		// Check the record was created on the DB... an id will be assigned.
		assertTrue("Object not persisted", insertSmsAccount.exists());
	}

	/**
	 * Test get sms account by id.
	 */
	public void testGetSmsAccountById() {
		SmsAccount getSmsAccount = HibernateLogicFactory.getAccountLogic()
				.getSmsAccount(insertSmsAccount.getId());
		assertTrue("Object not persisted", insertSmsAccount.exists());
		assertNotNull(getSmsAccount);
		assertEquals(insertSmsAccount, getSmsAccount);
	}

	/**
	 * Test update sms account.
	 */
	public void testUpdateSmsAccount() {
		SmsAccount smsAccount = HibernateLogicFactory.getAccountLogic()
				.getSmsAccount(insertSmsAccount.getId());
		smsAccount.setSakaiSiteId("newSakaiSiteId");
		HibernateLogicFactory.getAccountLogic().persistSmsAccount(smsAccount);
		smsAccount = HibernateLogicFactory.getAccountLogic().getSmsAccount(
				insertSmsAccount.getId());
		assertEquals("newSakaiSiteId", smsAccount.getSakaiSiteId());
	}

	/**
	 * Test add transactions to account.
	 */
	public void testAddTransactionsToAccount() {
		insertSmsTransaction1.setSmsAccount(insertSmsAccount);
		insertSmsTransaction2.setSmsAccount(insertSmsAccount);
		insertSmsAccount.getSmsTransactions().add(insertSmsTransaction1);
		insertSmsAccount.getSmsTransactions().add(insertSmsTransaction2);

		HibernateLogicFactory.getAccountLogic().persistSmsAccount(
				insertSmsAccount);

		assertTrue("Object not persisted", insertSmsTransaction1.exists());
		assertTrue("Object not persisted", insertSmsTransaction2.exists());
		SmsAccount account = HibernateLogicFactory.getAccountLogic()
				.getSmsAccount(insertSmsAccount.getId());
		assertNotNull("No object returned", account);
		assertEquals("Incorrect object returned", insertSmsAccount, account);
		assertTrue("Returnend collection is incorreclt size", account
				.getSmsTransactions().size() == 2);
	}

	/**
	 * Test get sms accounts.
	 */
	public void testGetSmsAccounts() {
		List<SmsAccount> accounts = HibernateLogicFactory.getAccountLogic()
				.getAllSmsAccounts();
		assertNotNull("Returnend collection is null", accounts);
		assertTrue("No records returned", accounts.size() > 0);
	}

	/**
	 * Test delete sms account.
	 */
	public void testDeleteSmsAccount() {
		HibernateLogicFactory.getAccountLogic().deleteSmsAccount(
				HibernateLogicFactory.getAccountLogic().getSmsAccount(
						insertSmsAccount.getId()));
		SmsAccount getSmsAccount = HibernateLogicFactory.getAccountLogic()
				.getSmsAccount(insertSmsAccount.getId());
		assertNull(getSmsAccount);
		assertNull("Object not removed", getSmsAccount);

	}
}
