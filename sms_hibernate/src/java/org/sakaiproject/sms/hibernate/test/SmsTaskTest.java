package org.sakaiproject.sms.hibernate.test;

import java.sql.Timestamp;
import java.util.List;

import junit.framework.TestCase;

import org.sakaiproject.sms.hibernate.logic.impl.SmsTaskLogicImpl;
import org.sakaiproject.sms.hibernate.model.SmsMessage;
import org.sakaiproject.sms.hibernate.model.SmsTask;
import org.sakaiproject.sms.hibernate.model.constants.SmsConst_DeliveryStatus;

/**
 * The Class SmsTaskTest.
 */
public class SmsTaskTest extends TestCase {

	/** The logic. */
	private static SmsTaskLogicImpl logic = null;

	/** The insert task. */
	private static SmsTask insertTask;

	/** The insert message1. */
	private static SmsMessage insertMessage1;

	/** The insert message2. */
	private static SmsMessage insertMessage2;

	static {
		logic = new SmsTaskLogicImpl();

		insertTask = new SmsTask();
		insertTask.setSakaiSiteId("sakaiSiteId");
		insertTask.setSmsAccountId(1);
		insertTask.setDateCreated(new Timestamp(System.currentTimeMillis()));
		insertTask.setDateToSend(new Timestamp(System.currentTimeMillis()));
		insertTask.setStatusCode(SmsConst_DeliveryStatus.STATUS_PENDING);
		insertTask.setAttemptCount(2);
		insertTask.setMessageBody("messageBody");
		insertTask.setSenderUserName("senderUserName");
		//
		insertMessage1 = new SmsMessage();
		insertMessage1.setMobileNumber("0721998919");
		insertMessage1.setSmscMessageId("smscMessageId1Task");
		insertMessage1.setSakaiUserId("sakaiUserId");
		insertMessage1.setStatusCode("P");
		//
		insertMessage2 = new SmsMessage();
		insertMessage2.setMobileNumber("0823450983");
		insertMessage2.setSmscMessageId("smscMessageId2Task");
		insertMessage2.setSakaiUserId("sakaiUserId");
		insertMessage2.setStatusCode("P");
	}

	/**
	 * Instantiates a new sms task test.
	 */
	public SmsTaskTest() {

	}

	/**
	 * Instantiates a new sms task test.
	 * 
	 * @param name
	 *            the name
	 */
	public SmsTaskTest(String name) {
		super(name);
	}

	/**
	 * Test insert sms task.
	 */
	public void testInsertSmsTask() {
		logic.persistSmsTask(insertTask);
		assertTrue("Object not persisted", insertTask.exists());
	}

	/**
	 * Test get sms task by id.
	 */
	public void testGetSmsTaskById() {
		SmsTask getSmsTask = logic.getSmsTask(insertTask.getId());
		assertNotNull(getSmsTask);
		assertEquals(insertTask, getSmsTask);
	}

	/**
	 * Test update sms task.
	 */
	public void testUpdateSmsTask() {
		SmsTask smsTask = logic.getSmsTask(insertTask.getId());
		assertFalse(smsTask.getSakaiSiteId().equals("newSakaiSiteId"));
		smsTask.setSakaiSiteId("newSakaiSiteId");
		logic.persistSmsTask(smsTask);
		smsTask = logic.getSmsTask(insertTask.getId());
		assertEquals("newSakaiSiteId", smsTask.getSakaiSiteId());
	}

	/**
	 * Test add sms messages to task.
	 */
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

	/**
	 * Test get sms tasks filtered by message status.
	 */
	public void testGetSmsTasksFilteredByMessageStatus() {
		List<SmsTask> tasks = logic.getSmsTasksFilteredByMessageStatus(
				SmsConst_DeliveryStatus.STATUS_PENDING,
				SmsConst_DeliveryStatus.STATUS_INCOMPLETE);
		for (SmsTask task : tasks) {
			for (SmsMessage smsMessage : task.getSmsMessages()) {
				assertTrue("Objetc found with incorrect value", smsMessage
						.getStatusCode().equals(
								SmsConst_DeliveryStatus.STATUS_PENDING));
			}
		}
	}

	/**
	 * Test remove sms messages from task.
	 */
	public void testRemoveSmsMessagesFromTask() {
		SmsTask getSmsTask = logic.getSmsTask(insertTask.getId());
		assertTrue("Collection size not correct", getSmsTask.getSmsMessages()
				.size() == 2);
		getSmsTask.setSakaiSiteId("oldSakaiSiteId");
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

	/**
	 * Test get sms tasks.
	 */
	public void testGetSmsTasks() {
		List<SmsTask> tasks = logic.getAllSmsTask();
		assertNotNull("Returned list is null", tasks);
		assertTrue("No records returned", tasks.size() > 0);
	}

	/**
	 * Test get next sms task.
	 */
	public void testGetNextSmsTask() {
		SmsTask nextTask = logic.getNextSmsTask();
		assertNotNull("Required record not found", nextTask);
		List<SmsTask> tasks = logic.getAllSmsTask();
		Timestamp t = null;

		// Get the oldest date to send from the list;
		for (SmsTask task : tasks) {
			if (t == null) {
				t = task.getDateToSend();
			}
			if (task.getDateToSend() != null
					&& task.getDateToSend().getTime() < t.getTime()) {
				t = task.getDateToSend();
				break;
			}
		}
		assertNotNull("No records found", t);
		assertTrue("Did not get the correct task to be processed", nextTask
				.getDateToSend().getTime() == t.getTime());
	}

	/**
	 * Test delete sms task.
	 */
	public void testDeleteSmsTask() {
		logic.deleteSmsTask(insertTask);
		SmsTask getSmsTask = logic.getSmsTask(insertTask.getId());
		assertNull("Object not removed", getSmsTask);
	}

}
