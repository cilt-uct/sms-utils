package org.sakaiproject.sms.hibernate.test;

import java.util.List;

import junit.framework.TestCase;

import org.sakaiproject.sms.hibernate.logic.impl.SmsAccountLogicImpl;
import org.sakaiproject.sms.hibernate.model.SmsAccount;

/**
 * The Class SmsAccountTest. Do some basic crud functions on the account table.
 */
public class SmsAccountTest extends TestCase {

	/** The logic. */
	private static SmsAccountLogicImpl logic = null;

	/** The insert sms account. */
	private static SmsAccount insertSmsAccount;
	static {
		logic = new SmsAccountLogicImpl();
		insertSmsAccount = new SmsAccount();
		insertSmsAccount.setSakaiUserId("SakaiUSerId");
		insertSmsAccount.setSakaiSiteId("SakaiSiteId");
		insertSmsAccount.setMessageTypeCode("12345");
		insertSmsAccount.setOverdraftLimit(10000.00f);
		insertSmsAccount.setBalance(5000.00f);
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
		logic.persistSmsAccount(insertSmsAccount);
		// Check the record was created on the DB... an id will be assigned.
		assertTrue("Object not persisted", insertSmsAccount.exists());
	}

	/**
	 * Test get sms account by id.
	 */
	public void testGetSmsAccountById() {
		SmsAccount getSmsAccount = logic
				.getSmsAccount(insertSmsAccount.getId());
		assertTrue("Object not persisted", insertSmsAccount.exists());
		assertNotNull(getSmsAccount);
		assertEquals(insertSmsAccount, getSmsAccount);
	}

	/**
	 * Test update sms account.
	 */
	public void testUpdateSmsAccount() {
		SmsAccount smsAccount = logic.getSmsAccount(insertSmsAccount.getId());
		smsAccount.setSakaiSiteId("newSakaiSiteId");
		logic.persistSmsAccount(smsAccount);
		smsAccount = logic.getSmsAccount(insertSmsAccount.getId());
		assertEquals("newSakaiSiteId", smsAccount.getSakaiSiteId());
	}

	/**
	 * Test get sms accounts.
	 */
	public void testGetSmsAccounts() {
		List<SmsAccount> accounts = logic.getAllSmsAccounts();
		assertNotNull("Returnend collection is null", accounts);
		assertTrue("No records returned", accounts.size() > 0);
	}

	/**
	 * Test delete sms account.
	 */
	public void testDeleteSmsAccount() {
		logic.deleteSmsCongif(insertSmsAccount);
		SmsAccount getSmsAccount = logic
				.getSmsAccount(insertSmsAccount.getId());
		assertNull(getSmsAccount);
		assertNull("Object not removed", getSmsAccount);
	}
}
