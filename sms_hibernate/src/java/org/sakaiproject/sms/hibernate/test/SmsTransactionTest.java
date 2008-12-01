package org.sakaiproject.sms.hibernate.test;

import java.sql.Timestamp;
import java.util.List;

import junit.framework.TestCase;

import org.sakaiproject.sms.hibernate.logic.impl.SmsTransactionLogicImpl;
import org.sakaiproject.sms.hibernate.model.SmsTransaction;

public class SmsTransactionTest extends TestCase {

	private static SmsTransactionLogicImpl logic = null;
	private static SmsTransaction insertSmsTransaction;

	static {
		logic = new SmsTransactionLogicImpl();

		insertSmsTransaction = new SmsTransaction();
		insertSmsTransaction.setBalance(1.32f);
		insertSmsTransaction.setSakaiUserId("sakaiUserId");
		insertSmsTransaction.setSmsAccountId(1);
		insertSmsTransaction.setTransactionDate(new Timestamp(System
				.currentTimeMillis()));
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
		fail("test not implemeted yet");
		/*SmsTask insertTask = new SmsTask();
		insertTask.setSakaiSiteId("sakaiSiteId");
		insertTask.setSmsAccountId(1);
		insertTask.setDateCreated(new Timestamp(System.currentTimeMillis()));
		insertTask.setDateToSend(new Timestamp(System.currentTimeMillis()));
		insertTask.setStatusCode(SmsConst_DeliveryStatus.STATUS_PENDING);
		insertTask.setAttemptCount(2);
		insertTask.setMessageBody("messageCrit");
		insertTask.setSenderUserName("messageCrit");
		insertTask.setSakaiToolName("sakaiToolName");
		
		SmsMessage insertMessage = new SmsMessage();
		insertMessage.setMobileNumber("0721998919");
		insertMessage.setSmscMessageId("criterai");
		insertMessage.setSakaiUserId("criterai");
		insertMessage.setStatusCode(SmsConst_DeliveryStatus.STATUS_ERROR);
		
		insertMessage.setSmsTask(insertTask);
		insertTask.getSmsMessages().add(insertMessage);
		
		try {
			taskLogic.persistSmsTask(insertTask);
			
			SearchFilterBean bean = new SearchFilterBean();
			bean.setStatus(insertMessage.getStatusCode());
			bean.setDateFrom("12/01/2008");
			bean.setDateTo("12/01/2008");
			bean.setToolName(insertTask.getSakaiToolName());
			bean.setSender(insertTask.getSenderUserName());
			bean.setMobileNumber(insertMessage.getMobileNumber());
		
			List<SmsMessage> messages = logic.getSmsMessagesForCriteria(bean);
			assertTrue("Collection returned has no objects", messages.size() > 0);
		
			for(SmsMessage message : messages) {
				//We know that only one message should be returned becuase
				//we only added one with status ERROR.
				assertEquals(message, insertMessage);
			}
		}catch(SmsSearchException se) {
			fail(se.getMessage());
		}finally {
			taskLogic.deleteSmsTask(insertTask);
		}*/
	}

	public void testDeleteSmsTransaction() {
		logic.deleteSmsCongif(insertSmsTransaction);
		SmsTransaction getSmsTransaction = logic
				.getSmsTransaction(insertSmsTransaction.getId());
		assertNull(getSmsTransaction);
		assertNull("Object not removed", getSmsTransaction);
	}

}
