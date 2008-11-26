package org.sakaiproject.sms.hibernate.test;

import java.sql.Timestamp;
import java.util.List;

import org.sakaiproject.sms.hibernate.logic.impl.SmsDataLogicImpl;
import org.sakaiproject.sms.hibernate.logic.impl.SmsTransactionLogicImpl;
import org.sakaiproject.sms.hibernate.model.BaseModel;
import org.sakaiproject.sms.hibernate.model.SmsTransaction;
import org.sakaiproject.sms.hibernate.model.SmsTransaction;

import junit.framework.TestCase;

public class SmsTransactionTest extends TestCase {
	
	private static SmsTransactionLogicImpl logic = null;
	private static SmsTransaction insertSmsTransaction;
	
	static {
		logic = new SmsTransactionLogicImpl();
		
		insertSmsTransaction = new SmsTransaction();
		insertSmsTransaction.setBalance(1.32f);
		insertSmsTransaction.setSakaiUserId("sakaiUserId");
		insertSmsTransaction.setSmsAccountId(1);
		insertSmsTransaction.setTransactionDate(new Timestamp(System.currentTimeMillis()));
		insertSmsTransaction.setTransactionTypeCode("TC");
		insertSmsTransaction.setTransactionCredits(666);
		insertSmsTransaction.setTransactionAmount(1000.00f);
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
		SmsTransaction getSmsTransaction = logic.getSmsTransaction(insertSmsTransaction.getId());
		assertTrue("Object not persisted", insertSmsTransaction.exists());
		assertNotNull(getSmsTransaction);
		assertEquals(insertSmsTransaction, getSmsTransaction);
	}

	public void testUpdateSmsTransaction() {
		SmsTransaction smsTransaction = logic.getSmsTransaction(insertSmsTransaction.getId());
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

	public void testDeleteSmsTransaction() {
		logic.deleteSmsCongif(insertSmsTransaction);
		SmsTransaction getSmsTransaction = logic.getSmsTransaction(insertSmsTransaction.getId());
		assertNull(getSmsTransaction);
		assertNull("Object not removed", getSmsTransaction);
	}

}
