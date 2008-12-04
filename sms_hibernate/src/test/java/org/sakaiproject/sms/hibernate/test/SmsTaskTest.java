package org.sakaiproject.sms.hibernate.test;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.sakaiproject.sms.hibernate.bean.SearchFilterBean;
import org.sakaiproject.sms.hibernate.dao.HibernateUtil;
import org.sakaiproject.sms.hibernate.logic.impl.SmsMessageLogicImpl;
import org.sakaiproject.sms.hibernate.logic.impl.SmsTaskLogicImpl;
import org.sakaiproject.sms.hibernate.logic.impl.exception.SmsSearchException;
import org.sakaiproject.sms.hibernate.model.SmsMessage;
import org.sakaiproject.sms.hibernate.model.SmsTask;
import org.sakaiproject.sms.hibernate.model.constants.SmsConst_DeliveryStatus;
import org.sakaiproject.sms.hibernate.util.DateUtil;

/**
 * The Class SmsTaskTest.
 */
public class SmsTaskTest extends TestCase {

	/**
	 * This is used for one time setup and tear down per test case.
	 * 
	 * @return the test
	 */
	public static Test suite() {

		TestSetup setup = new TestSetup(new TestSuite(SmsTaskTest.class)) {

			protected void setUp() throws Exception {
				HibernateUtil.createSchema();
			}

			protected void tearDown() throws Exception {

			}
		};
		return setup;
	}

	/** The logic. */
	private static SmsTaskLogicImpl logic = null;

	/** The insert task. */
	private static SmsTask insertTask;

	/** The insert message1. */
	private static SmsMessage insertMessage1;

	/** The insert message2. */
	private static SmsMessage insertMessage2;

	/** The message logic */
	private static SmsMessageLogicImpl messageLogic = null;

	static {
		messageLogic = new SmsMessageLogicImpl();
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
		insertMessage1.setStatusCode(SmsConst_DeliveryStatus.STATUS_PENDING);
		//
		insertMessage2 = new SmsMessage();
		insertMessage2.setMobileNumber("0823450983");
		insertMessage2.setSmscMessageId("smscMessageId2Task");
		insertMessage2.setSakaiUserId("sakaiUserId");
		insertMessage2.setStatusCode(SmsConst_DeliveryStatus.STATUS_INCOMPLETE);
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

	public void testAddSmsMessagesToTask_setMessages() {
		Set<SmsMessage> messages = getSmsMessages(insertTask);
		insertTask.setSmsMessagesOnTask(messages);
		logic.persistSmsTask(insertTask);
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
				.size() == 4);

	}

	/**
	 * Test remove sms messages from task.
	 */
	public void testRemoveSmsMessagesFromTask() {
		/*
		 * SmsTask getSmsTask = logic.getSmsTask(insertTask.getId());
		 * assertTrue("Collection size not correct", getSmsTask.getSmsMessages()
		 * .size() == 4);
		 */
		insertTask.setSakaiSiteId("oldSakaiSiteId");
		insertTask.getSmsMessages().remove(insertMessage1);
		logic.persistSmsTask(insertTask);
		SmsTask getSmsTask = logic.getSmsTask(insertTask.getId());
		assertTrue("Object not removed from collection", getSmsTask
				.getSmsMessages().size() == 3);
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
	 * Test get next sms task. Checks if it is the oldest task to process
	 * (beeing the next task to process)
	 */
	public void testGetNextSmsTask() {
		/*
		 * SmsTask nextTask = logic.getNextSmsTask();
		 * assertNotNull("Required record not found", nextTask);
		 * List<SmsMessage> messages =
		 * messageLogic.getSmsMessagesWithStatus(null,
		 * SmsConst_DeliveryStatus.STATUS_PENDING,
		 * SmsConst_DeliveryStatus.STATUS_INCOMPLETE);
		 * 
		 * Timestamp t = null; // Get the oldest date to send from the list; for
		 * (SmsMessage message : messages) { if (t == null) { t =
		 * message.getSmsTask().getDateToSend(); } if
		 * (message.getSmsTask().getDateToSend() != null &&
		 * message.getSmsTask().getDateToSend().getTime() < t .getTime()) { t =
		 * message.getSmsTask().getDateToSend(); break; } }
		 * assertNotNull("No records found", t);
		 * assertTrue("Did not get the correct task to be processed", nextTask
		 * .getDateToSend().getTime() == t.getTime());
		 */

	}

	/**
	 * Tests the getMessagesForCriteria method
	 */
	public void testGetTasksForCriteria() {
		SmsTask insertTask = new SmsTask();
		insertTask.setSakaiSiteId("sakaiSiteId");
		insertTask.setSmsAccountId(1);
		insertTask.setDateCreated(new Timestamp(System.currentTimeMillis()));
		insertTask.setDateToSend(new Timestamp(System.currentTimeMillis()));
		insertTask.setStatusCode(SmsConst_DeliveryStatus.STATUS_PENDING);
		insertTask.setAttemptCount(2);
		insertTask.setMessageBody("taskCrit");
		insertTask.setSenderUserName("taskCrit");
		insertTask.setSakaiToolName("sakaiToolName");

		try {
			logic.persistSmsTask(insertTask);

			SearchFilterBean bean = new SearchFilterBean();
			bean.setStatus(insertTask.getStatusCode());
			bean.setDateFrom(DateUtil.getDateString(new Date()));
			bean.setDateTo(DateUtil.getDateString(new Date()));
			bean.setToolName(insertTask.getSakaiToolName());
			bean.setSender(insertTask.getSenderUserName());

			List<SmsTask> tasks = logic.getSmsTasksForCriteria(bean);
			assertTrue("Collection returned has no objects", tasks.size() > 0);

			for (SmsTask task : tasks) {
				// We know that only one task should be returned
				assertEquals(task, insertTask);
			}
		} catch (SmsSearchException se) {
			fail(se.getMessage());
		} finally {
			logic.deleteSmsTask(insertTask);
		}
	}

	/**
	 * Test delete sms task.
	 */
	public void testDeleteSmsTask() {
		logic.deleteSmsTask(insertTask);
		SmsTask getSmsTask = logic.getSmsTask(insertTask.getId());
		assertNull("Object not removed", getSmsTask);
	}

	// ///////////////////////////////////////////////////////////////////////////////////
	// Helper methods
	// ///////////////////////////////////////////////////////////////////////////////////

	private Set<SmsMessage> getSmsMessages(SmsTask task) {
		SmsMessage insertMessage1 = new SmsMessage();
		insertMessage1.setMobileNumber("0721998919");
		insertMessage1.setSmscMessageId("smscGetID1");
		insertMessage1.setSakaiUserId("sakaiUserId");
		insertMessage1.setStatusCode(SmsConst_DeliveryStatus.STATUS_DELIVERED);
		//
		SmsMessage insertMessage2 = new SmsMessage();
		insertMessage2.setMobileNumber("0823450983");
		insertMessage2.setSmscMessageId("smscGetID2");
		insertMessage2.setSakaiUserId("sakaiUserId");
		insertMessage2.setStatusCode(SmsConst_DeliveryStatus.STATUS_DELIVERED);

		insertMessage1.setSmsTask(task);
		insertMessage2.setSmsTask(task);

		Set<SmsMessage> messages = new HashSet<SmsMessage>();
		messages.add(insertMessage1);
		messages.add(insertMessage2);
		return messages;
	}

}
