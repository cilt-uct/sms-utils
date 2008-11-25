package org.sakaiproject.sms.hibernate.test;

import java.sql.Timestamp;

import org.sakaiproject.sms.hibernate.logic.impl.SmsDataLogicImpl;
import org.sakaiproject.sms.hibernate.logic.impl.SmsTaskLogicImpl;
import org.sakaiproject.sms.hibernate.model.BaseModel;
import org.sakaiproject.sms.hibernate.model.SmsConfig;
import org.sakaiproject.sms.hibernate.model.SmsMessage;
import org.sakaiproject.sms.hibernate.model.SmsTask;
import org.sakaiproject.sms.hibernate.model.SmsTransaction;

import junit.framework.TestCase;

public class SmsTaskTest extends TestCase {
	private static SmsTaskLogicImpl logic = null;
	
	private static SmsTask insertTask;
	private static SmsMessage insertMessage1;
	private static SmsMessage insertMessage2;
	
	static {
		logic = new SmsTaskLogicImpl();
		
		insertTask = new SmsTask();
		insertTask.setSakaiSiteId("sakaiSiteId");
		insertTask.setSmsAccountId(1);
		insertTask.setDateCreated(new Timestamp(System.currentTimeMillis()));
		insertTask.setDateToSend(new Timestamp(System.currentTimeMillis()));
		insertTask.setStatusCode("SC");
		insertTask.setRetryCount(2);
		insertTask.setMessageBody("messageBody");
		//insertTask.setSenderUserName("senderUserName");
		//
		insertMessage1 = new SmsMessage();
		insertMessage1.setMobileNumber("0721998919");
		insertMessage1.setSmscMessageId("smscMessageId1");
		insertMessage1.setSakaiUserId("sakaiUserId");
		insertMessage1.setStatusCode("SC");
		//
		insertMessage2 = new SmsMessage();
		insertMessage2.setMobileNumber("0823450983");
		insertMessage2.setSmscMessageId("smscMessageId1");
		insertMessage2.setSakaiUserId("sakaiUserId");
		insertMessage2.setStatusCode("SC");
	}
	
	public SmsTaskTest() {
		
	}

	public SmsTaskTest(String name) {
		super(name);
	}
	
	public void testInsertSmsTask(){
		logic.persistSmsTask(insertTask);
		assertTrue("Object not persisted", insertTask.exists());
	}
	
	public void testUpdateSmsTask(){	
		SmsTask smsTask = logic.getSmsTask(insertTask.getId());
		assertFalse(smsTask.getSakaiSiteId().equals("newSakaiSiteId"));
		smsTask.setSakaiSiteId("newSakaiSiteId");
		logic.persistSmsTask(smsTask);
		smsTask = logic.getSmsTask(insertTask.getId());
		assertEquals("newSakaiSiteId", smsTask.getSakaiSiteId());
	}
	
	public void testAddSmsMessagesToTask() {
		insertTask.getSmsMessages().add(insertMessage1);
		insertTask.getSmsMessages().add(insertMessage2);
		insertMessage1.setSmsTask(insertTask);
		insertMessage2.setSmsTask(insertTask);
		logic.persistSmsTask(insertTask);
		SmsTask getTask = logic.getSmsTask(insertTask.getId());
		assertNotNull(insertTask);
		assertTrue("Collection size not correct", getTask.getSmsMessages().size() == 2);
	}
	
	public void testGetSmsTasks(){
		
	}
	
	public void testGetSmsTaskById(){
		
	}
	
	
	public void testGetSmsTaskMessages(){
		
	}
	
	public void testDeleteSmsTask(){
		
	}
	

}
