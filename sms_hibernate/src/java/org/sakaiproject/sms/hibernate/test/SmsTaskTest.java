package org.sakaiproject.sms.hibernate.test;

import java.sql.Timestamp;
import java.util.List;

import junit.framework.TestCase;

import org.sakaiproject.sms.hibernate.logic.impl.SmsTaskLogicImpl;
import org.sakaiproject.sms.hibernate.model.SmsMessage;
import org.sakaiproject.sms.hibernate.model.SmsTask;

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
		insertTask.setSenderUserName("senderUserName");
		//
		insertMessage1 = new SmsMessage();
		insertMessage1.setMobileNumber("0721998919");
		insertMessage1.setSmscMessageId("smscMessageId1Task");
		insertMessage1.setSakaiUserId("sakaiUserId");
		insertMessage1.setStatusCode(-1);
		//
		insertMessage2 = new SmsMessage();
		insertMessage2.setMobileNumber("0823450983");
		insertMessage2.setSmscMessageId("smscMessageId2Task");
		insertMessage2.setSakaiUserId("sakaiUserId");
		insertMessage2.setStatusCode(-1);
	}

	public SmsTaskTest() {

	}

	public SmsTaskTest(String name) {
		super(name);
	}

	public void testInsertSmsTask() {
		logic.persistSmsTask(insertTask);
		assertTrue("Object not persisted", insertTask.exists());
	}

	public void testGetSmsTaskById() {
		SmsTask getSmsTask = logic.getSmsTask(insertTask.getId());
		assertNotNull(getSmsTask);
		assertEquals(insertTask, getSmsTask);
	}

	public void testUpdateSmsTask() {
		SmsTask smsTask = logic.getSmsTask(insertTask.getId());
		assertFalse(smsTask.getSakaiSiteId().equals("newSakaiSiteId"));
		smsTask.setSakaiSiteId("newSakaiSiteId");
		logic.persistSmsTask(smsTask);
		smsTask = logic.getSmsTask(insertTask.getId());
		assertEquals("newSakaiSiteId", smsTask.getSakaiSiteId());
	}

	public void testAddSmsMessagesToTask() {
		insertMessage1.setSmsTask(insertTask);
		insertMessage2.setSmsTask(insertTask);
		insertTask.getSmsMessages().add(insertMessage1);
		insertTask.getSmsMessages().add(insertMessage2);

		logic.persistSmsTask(insertTask);
		SmsTask getSmsTask = logic.getSmsTask(insertTask.getId());
		assertNotNull(insertTask);
		assertTrue("Collection size not correct", getSmsTask.getSmsMessages()
				.size() == 2);

	}

	public void testRemoveSmsMessagesFromTask() {
		SmsTask getSmsTask = logic.getSmsTask(insertTask.getId());
		assertTrue("Collection size not correct", getSmsTask.getSmsMessages()
				.size() == 2);
		getSmsTask.getSmsMessages().remove(insertMessage1);
		logic.persistSmsTask(getSmsTask);
		getSmsTask = logic.getSmsTask(insertTask.getId());
		assertTrue("Object not removed from collection", getSmsTask
				.getSmsMessages().size() == 1);
		// Check the right object was removed
		assertFalse("The expected object was not removed from the collection",
				getSmsTask.getSmsMessages().contains(insertMessage1));
		assertTrue("The incorrect object was removed from the collection",
				getSmsTask.getSmsMessages().contains(insertMessage2));
	}

	public void testGetSmsTasks() {
		List<SmsTask> tasks = logic.getAllSmsTask();
		assertNotNull("Returned list is null", tasks);
		assertTrue("No records returned", tasks.size() > 0);
	}

	public void testDeleteSmsTask() {
		logic.deleteSmsTask(insertTask);
		SmsTask getSmsTask = logic.getSmsTask(insertTask.getId());
		assertNull("Object not removed", getSmsTask);
	}
}
