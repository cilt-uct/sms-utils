package org.sakaiproject.sms.hibernate.test;

import java.util.Date;
import java.util.List;

import org.sakaiproject.sms.hibernate.logic.impl.HibernateLogicFactory;
import org.sakaiproject.sms.hibernate.logic.impl.exception.DuplicateUniqueFieldException;
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
		HibernateUtil.setTestConfiguration(true);
		HibernateUtil.createSchema();

		insertSmsAccount = new SmsAccount();
		insertSmsAccount.setSakaiUserId("1");
		insertSmsAccount.setSakaiSiteId("1");
		insertSmsAccount.setMessageTypeCode("12345");
		insertSmsAccount.setOverdraftLimit(10000.00f);
		insertSmsAccount.setBalance(5000.00f);
		insertSmsAccount.setAccountName("accountName");
		insertSmsAccount.setAccountEnabled(true);

		insertSmsTransaction1 = new SmsTransaction();
		insertSmsTransaction1.setBalance(100.00f);
		insertSmsTransaction1.setSakaiUserId("5");
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
	 * Test insertion of account with duplicate site id
	 */
	public void testInsertSmsAccountDuplicateSiteId() {
		HibernateLogicFactory.getAccountLogic().persistSmsAccount(
				insertSmsAccount);
		// Check the record was created on the DB... an id will be assigned.
		assertTrue("Object not persisted", insertSmsAccount.exists());

		SmsAccount newSmsAccount = new SmsAccount();
		newSmsAccount.setSakaiSiteId("1");

		try {
			HibernateLogicFactory.getAccountLogic().persistSmsAccount(
					newSmsAccount);
			fail("DuplicateUniqueFieldException should be caught");
		} catch (DuplicateUniqueFieldException due) {
			assertEquals("sakaiSiteId", due.getField());
		}
	}

	/**
	 * Test insertion of account with duplicate user id
	 */
	public void testInsertSmsAccountDuplicateUserId() {
		HibernateLogicFactory.getAccountLogic().persistSmsAccount(
				insertSmsAccount);
		// Check the record was created on the DB... an id will be assigned.
		assertTrue("Object not persisted", insertSmsAccount.exists());

		SmsAccount newSmsAccount = new SmsAccount();
		newSmsAccount.setSakaiUserId("1");

		try {
			HibernateLogicFactory.getAccountLogic().persistSmsAccount(
					newSmsAccount);
			fail("DuplicateUniqueFieldException should be caught");
		} catch (DuplicateUniqueFieldException due) {
			assertEquals("sakaiUserId", due.getField());
		}
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

	/**
	 * Test get sms account by sakai site id.
	 */
	public void testGetSmsAccount_sakaiSiteId() {
		SmsAccount account = new SmsAccount();
		account.setSakaiSiteId("sakSitId");
		account.setMessageTypeCode("12345");
		account.setOverdraftLimit(10000.00f);
		account.setBalance(5000.00f);
		account.setAccountName("accountName");
		account.setAccountEnabled(true);
		HibernateLogicFactory.getAccountLogic().persistSmsAccount(account);
		SmsAccount retAccount = HibernateLogicFactory.getAccountLogic()
				.getSmsAccount(account.getSakaiSiteId(), "");
		assertNotNull(retAccount);
		assertEquals(retAccount, account);
	}

	/**
	 * Test get sms account byt sakai user id.
	 */
	public void testGetSmsAccount_sakaiUserId() {
		SmsAccount account = new SmsAccount();
		account.setSakaiUserId("testGetSmsAccount_sakaiUserId");
		account.setSakaiSiteId("testGetSmsAccount_sakaiSiteId");
		account.setMessageTypeCode("12345");
		account.setOverdraftLimit(10000.00f);
		account.setBalance(5000.00f);
		account.setAccountName("accountName");
		account.setAccountEnabled(true);
		HibernateLogicFactory.getAccountLogic().persistSmsAccount(account);
		SmsAccount retAccount = HibernateLogicFactory.getAccountLogic()
				.getSmsAccount("testGetSmsAccount_sakaiSiteId",
						account.getSakaiUserId());
		assertNotNull(retAccount);
		assertEquals(retAccount, account);
	}
}
