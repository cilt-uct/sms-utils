package org.sakaiproject.sms.hibernate.test;

import java.util.List;

import org.sakaiproject.sms.hibernate.logic.impl.SmsAccountLogicImpl;
import org.sakaiproject.sms.hibernate.logic.impl.SmsDataLogicImpl;
import org.sakaiproject.sms.hibernate.model.BaseModel;
import org.sakaiproject.sms.hibernate.model.SmsAccount;

import junit.framework.TestCase;

public class SmsAccountTest extends TestCase {
	private static SmsAccountLogicImpl logic = null;
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
	
	public SmsAccountTest() {
	}

	public SmsAccountTest(String name) {
		super(name);
	}
	
	public void testInsertSmsAccount() {
		logic.persistSmsAccount(insertSmsAccount);
		// Check the record was created on the DB... an id will be assigned.
		assertTrue("Object not persisted", insertSmsAccount.exists());
	}

	public void testGetSmsAccountById() {
		SmsAccount getSmsAccount = logic.getSmsAccount(insertSmsAccount.getId());
		assertTrue("Object not persisted", insertSmsAccount.exists());
		assertNotNull(getSmsAccount);
		assertEquals(insertSmsAccount, getSmsAccount);
	}

	public void testUpdateSmsAccount() {
		SmsAccount smsAccount = logic.getSmsAccount(insertSmsAccount.getId());
		smsAccount.setSakaiSiteId("newSakaiSiteId");
		logic.persistSmsAccount(smsAccount);
		smsAccount = logic.getSmsAccount(insertSmsAccount.getId());
		assertEquals("newSakaiSiteId", smsAccount.getSakaiSiteId());
	}

	public void testGetSmsAccounts() {
		List<SmsAccount> accounts = logic.getAllSmsAccounts();
		assertNotNull("Returnend collection is null", accounts);
		assertTrue("No records returned", accounts.size() > 0);
	}

	public void testDeleteSmsAccount() {
		logic.deleteSmsCongif(insertSmsAccount);
		SmsAccount getSmsAccount = logic.getSmsAccount(insertSmsAccount.getId());
		assertNull(getSmsAccount);
		assertNull("Object not removed", getSmsAccount);
	}
}
