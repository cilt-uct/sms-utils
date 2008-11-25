package org.sakaiproject.sms.hibernate.test;

import java.sql.Timestamp;

import org.sakaiproject.sms.hibernate.logic.impl.SmsDataLogicImpl;
import org.sakaiproject.sms.hibernate.model.BaseModel;
import org.sakaiproject.sms.hibernate.model.SmsMessage;
import org.sakaiproject.sms.hibernate.model.SmsTask;
import org.sakaiproject.sms.hibernate.model.SmsTransaction;

import junit.framework.TestCase;

public class SmsTaskTest extends TestCase {
	private SmsDataLogicImpl logic = null;
	
	private SmsTask insertTask;
	private SmsMessage insertMessage1;
	private SmsMessage insertMessage2;
	
	{
		logic = new SmsDataLogicImpl();
	}
	
	public SmsTaskTest() {
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

	public SmsTaskTest(String name) {
		super(name);
	}
	
	public void testInsertSmsTask(){
		insertTask.getSmsMessages().add(insertMessage1);
		insertTask.getSmsMessages().add(insertMessage2);
		insertMessage1.setSmsTask(insertTask);
		insertMessage2.setSmsTask(insertTask);
		//logic.persist(insertTask);
		System.out.println("");
	}
	
	public void testUpdateSmsTask(){
		
	}
	
	public void testGetSmsTasks(){
		
	}
	
	public void testGetSmsTaskById(){
		
	}
	
	public void testDeleteSmsTask(){
		
	}
	
	public void testGetSmsTaskMessages(){
		
	}

}
