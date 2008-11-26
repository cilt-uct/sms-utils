package org.sakaiproject.sms.hibernate.test;

import java.sql.Timestamp;

import org.sakaiproject.sms.hibernate.logic.impl.SmsDataLogicImpl;
import org.sakaiproject.sms.hibernate.logic.impl.SmsMessageLogicImpl;
import org.sakaiproject.sms.hibernate.logic.impl.SmsTaskLogicImpl;
import org.sakaiproject.sms.hibernate.model.BaseModel;
import org.sakaiproject.sms.hibernate.model.SmsMessage;
import org.sakaiproject.sms.hibernate.model.SmsTask;
import org.sakaiproject.sms.hibernate.model.SmsTransaction;

import junit.framework.TestCase;

public class SmsMessageTest extends TestCase {
	private static SmsMessageLogicImpl logic = null;
	private static SmsTask insertTask;
	private static SmsMessage insertMessage1;
	private static SmsMessage insertMessage2;
	private static SmsTaskLogicImpl taskLogic;
	static {
		logic = new SmsMessageLogicImpl();
		
		
		
		insertTask = new SmsTask();
		insertTask.setSakaiSiteId("sakaiSiteId");
		insertTask.setSmsAccountId(1);
		insertTask.setDateCreated(new Timestamp(System.currentTimeMillis()));
		insertTask.setDateToSend(new Timestamp(System.currentTimeMillis()));
		insertTask.setStatusCode("SC");
		insertTask.setRetryCount(2);
		insertTask.setMessageBody("messageBody");
		insertTask.setSenderUserName("senderUserName");
		taskLogic = new SmsTaskLogicImpl();
		//Insert the task so we can play with messages
		taskLogic.persistSmsTask(insertTask);
		
		insertMessage1 = new SmsMessage();
		insertMessage1.setMobileNumber("0721998919");
		insertMessage1.setSmscMessageId("smscMessageId1");
		insertMessage1.setSakaiUserId("sakaiUserId");
		insertMessage1.setStatusCode("SC");

		insertMessage2 = new SmsMessage();
		insertMessage2.setMobileNumber("0823450983");
		insertMessage2.setSmscMessageId("smscMessageId1");
		insertMessage2.setSakaiUserId("sakaiUserId");
		insertMessage2.setStatusCode("SC");
	}
	
	public SmsMessageTest() {
	}

	public SmsMessageTest(String name) {
		super(name);
	}
	
	public void testInsertSmsMessage(){
		assertTrue("Task for message not created", insertTask.exists());
		insertMessage1.setSmsTask(insertTask);
		logic.persistSmsMessage(insertMessage1);
		assertTrue("Object not persisted", insertMessage1.exists());
		insertTask.getSmsMessages().add(insertMessage1);
		taskLogic.persistSmsTask(insertTask);
		assertTrue("", insertTask.getSmsMessages().contains(insertMessage1));
	}
	
	public void testUpdateSmsMessage(){
		
	}
	
	public void testGetSmsMessages(){
		
	}
	
	public void testGetSmsMessageById(){
		
	}
	
	public void testDeleteSmsMessage(){
		
		//Delete the associated task too
		taskLogic.deleteSmsTask(insertTask);
		SmsTask getSmsTask = taskLogic.getSmsTask(insertTask.getId());
		assertNull("Object not removed", getSmsTask);
	}
	
	

}
