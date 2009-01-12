package org.sakaiproject.sms.hibernate.test;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.sakaiproject.sms.hibernate.bean.SearchFilterBean;
import org.sakaiproject.sms.hibernate.bean.SearchResultContainer;
import org.sakaiproject.sms.hibernate.logic.impl.HibernateLogicFactory;
import org.sakaiproject.sms.hibernate.logic.impl.exception.SmsSearchException;
import org.sakaiproject.sms.hibernate.model.SmsMessage;
import org.sakaiproject.sms.hibernate.model.SmsTask;
import org.sakaiproject.sms.hibernate.model.constants.SmsConst_DeliveryStatus;
import org.sakaiproject.sms.hibernate.model.constants.SmsHibernateConstants;
import org.sakaiproject.sms.hibernate.util.AbstractBaseTestCase;
import org.sakaiproject.sms.hibernate.util.HibernateUtil;

// TODO: Auto-generated Javadoc
/**
 * A task consists of a series of rules and must be executed on the scheduled
 * date-time. For example: Send message x to group y at time z. When the task is
 * executed (real-time or scheduled), the corresponding messages will be
 * inserted into SMS_MESSAGE with status PENDING. As delivery reports come in,
 * the message statuses will be updated.
 */
public class SmsTaskTest extends AbstractBaseTestCase {

	/** The insert task. */
	private static SmsTask insertTask;

	/** The insert message1. */
	private static SmsMessage insertMessage1;

	/** The insert message2. */
	private static SmsMessage insertMessage2;

	static {

		HibernateUtil.createSchema();

		insertTask = new SmsTask();
		insertTask.setSakaiSiteId("sakaiSiteId");
		insertTask.setSmsAccountId(1);
		insertTask.setDateCreated(new Date(System.currentTimeMillis()));
		insertTask.setDateToSend(new Date(System.currentTimeMillis()));
		insertTask.setStatusCode(SmsConst_DeliveryStatus.STATUS_PENDING);
		insertTask.setAttemptCount(2);
		insertTask.setMessageBody("messageBody");
		insertTask.setSenderUserName("senderUserName");
		insertTask.setMaxTimeToLive(1);
		insertTask.setDelReportTimeoutDuration(1);

		insertMessage1 = new SmsMessage();
		insertMessage1.setMobileNumber("0721998919");
		insertMessage1.setSmscMessageId("smscMessageId1Task");
		insertMessage1.setSakaiUserId("sakaiUserId");
		insertMessage1.setStatusCode(SmsConst_DeliveryStatus.STATUS_PENDING);

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
		HibernateLogicFactory.getTaskLogic().persistSmsTask(insertTask);
		assertTrue("Object not persisted", insertTask.exists());
	}

	/**
	 * Test get sms task by id.
	 */
	public void testGetSmsTaskById() {
		SmsTask getSmsTask = HibernateLogicFactory.getTaskLogic().getSmsTask(
				insertTask.getId());
		assertNotNull(getSmsTask);
		assertEquals(insertTask, getSmsTask);
	}

	/**
	 * Test update sms task.
	 */
	public void testUpdateSmsTask() {
		SmsTask smsTask = HibernateLogicFactory.getTaskLogic().getSmsTask(
				insertTask.getId());
		assertFalse(smsTask.getSakaiSiteId().equals("newSakaiSiteId"));
		smsTask.setSakaiSiteId("newSakaiSiteId");
		HibernateLogicFactory.getTaskLogic().persistSmsTask(smsTask);
		smsTask = HibernateLogicFactory.getTaskLogic().getSmsTask(
				insertTask.getId());
		assertEquals("newSakaiSiteId", smsTask.getSakaiSiteId());
	}

	/**
	 * Test add sms messages to task_set messages.
	 */
	public void testAddSmsMessagesToTask_setMessages() {

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

		insertMessage1.setSmsTask(insertTask);
		insertMessage2.setSmsTask(insertTask);

		Set<SmsMessage> messages = new HashSet<SmsMessage>();
		messages.add(insertMessage1);
		messages.add(insertMessage2);

		insertTask.setSmsMessagesOnTask(messages);
		HibernateLogicFactory.getTaskLogic().persistSmsTask(insertTask);
	}

	/**
	 * Test add sms messages to task.
	 */
	public void testAddSmsMessagesToTask() {
		insertMessage1.setSmsTask(insertTask);
		insertMessage2.setSmsTask(insertTask);
		insertTask.getSmsMessages().add(insertMessage1);
		insertTask.getSmsMessages().add(insertMessage2);

		HibernateLogicFactory.getTaskLogic().persistSmsTask(insertTask);
		SmsTask getSmsTask = HibernateLogicFactory.getTaskLogic().getSmsTask(
				insertTask.getId());
		assertNotNull(insertTask);
		assertTrue("Collection size not correct", getSmsTask.getSmsMessages()
				.size() == 4);

	}

	/**
	 * Test remove sms messages from task.
	 */
	public void testRemoveSmsMessagesFromTask() {
		insertTask.setSakaiSiteId("oldSakaiSiteId");
		insertTask.getSmsMessages().remove(insertMessage1);
		HibernateLogicFactory.getTaskLogic().persistSmsTask(insertTask);
		SmsTask getSmsTask = HibernateLogicFactory.getTaskLogic().getSmsTask(
				insertTask.getId());
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
		List<SmsTask> tasks = HibernateLogicFactory.getTaskLogic()
				.getAllSmsTask();
		assertNotNull("Returned list is null", tasks);
		assertTrue("No records returned", tasks.size() > 0);
	}

	/**
	 * Test get next sms task. Checks if it is the oldest task to process (Being
	 * the next task to process)
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
	 * Tests the getMessagesForCriteria method.
	 */
	public void testGetTasksForCriteria() {
		SmsTask insertTask = new SmsTask();
		insertTask.setSakaiSiteId("sakaiSiteId");
		insertTask.setSmsAccountId(1);
		insertTask.setDateCreated(new Date(System.currentTimeMillis()));
		insertTask.setDateToSend(new Date(System.currentTimeMillis()));
		insertTask.setStatusCode(SmsConst_DeliveryStatus.STATUS_PENDING);
		insertTask.setAttemptCount(2);
		insertTask.setMessageBody("taskCrit");
		insertTask.setSenderUserName("taskCrit");
		insertTask.setSakaiToolName("sakaiToolName");
		insertTask.setMaxTimeToLive(1);
		insertTask.setDelReportTimeoutDuration(1);

		try {
			HibernateLogicFactory.getTaskLogic().persistSmsTask(insertTask);

			SearchFilterBean bean = new SearchFilterBean();
			bean.setStatus(insertTask.getStatusCode());
			bean.setDateFrom(new Date());
			bean.setDateTo(new Date());
			bean.setToolName(insertTask.getSakaiToolName());
			bean.setSender(insertTask.getSenderUserName());

			List<SmsTask> tasks = HibernateLogicFactory.getTaskLogic()
					.getSmsTasksForCriteria(bean).getPageResults();
			assertTrue("Collection returned has no objects", tasks.size() > 0);

			for (SmsTask task : tasks) {
				// We know that only one task should be returned
				assertEquals(task, insertTask);
			}
		} catch (SmsSearchException se) {
			fail(se.getMessage());
		} finally {
			HibernateLogicFactory.getTaskLogic().deleteSmsTask(insertTask);
		}
	}

	/**
	 * Test get tasks for criteria_ paging.
	 */
	public void testGetTasksForCriteria_Paging() {

		int recordsToInsert = 93;

		for (int i = 0; i < recordsToInsert; i++) {
			SmsTask insertTask = new SmsTask();
			insertTask.setSakaiSiteId("sakaiSiteId");
			insertTask.setSmsAccountId(1);
			insertTask.setDateCreated(new Date(System.currentTimeMillis()));
			insertTask.setDateToSend(new Date(System.currentTimeMillis()));
			insertTask.setStatusCode(SmsConst_DeliveryStatus.STATUS_FAIL);
			insertTask.setAttemptCount(2);
			insertTask.setMessageBody("taskCrit");
			insertTask.setSenderUserName("taskCrit");
			insertTask.setSakaiToolName("sakaiToolName");
			insertTask.setMaxTimeToLive(1);
			insertTask.setDelReportTimeoutDuration(i);
			HibernateLogicFactory.getTaskLogic().persistSmsTask(insertTask);
		}

		try {

			SearchFilterBean bean = new SearchFilterBean();
			bean.setStatus(SmsConst_DeliveryStatus.STATUS_FAIL);
			bean.setDateFrom(new Date());
			bean.setDateTo(new Date());
			bean.setToolName("sakaiToolName");
			bean.setSender("taskCrit");

			bean.setCurrentPage(2);

			SearchResultContainer<SmsTask> con = HibernateLogicFactory
					.getTaskLogic().getSmsTasksForCriteria(bean);
			List<SmsTask> tasks = con.getPageResults();
			assertTrue("Incorrect collection size returned",
					tasks.size() == SmsHibernateConstants.DEFAULT_PAGE_SIZE);

			// Test last page. We know there are 124 records to this should
			// return a list of 4

			int pages = recordsToInsert
					/ SmsHibernateConstants.DEFAULT_PAGE_SIZE;
			// set to last page
			if (recordsToInsert % SmsHibernateConstants.DEFAULT_PAGE_SIZE == 0) {
				bean.setCurrentPage(pages);
			} else {
				bean.setCurrentPage(pages + 1);
			}

			con = HibernateLogicFactory.getTaskLogic().getSmsTasksForCriteria(
					bean);
			tasks = con.getPageResults();
			int lastPageRecordCount = recordsToInsert
					- (pages * SmsHibernateConstants.DEFAULT_PAGE_SIZE);
			assertTrue("Incorrect collection size returned",
					tasks.size() == lastPageRecordCount);

		} catch (Exception se) {
			fail(se.getMessage());
		}
	}

	/**
	 * Test delete sms task.
	 */
	public void testDeleteSmsTask() {
		HibernateLogicFactory.getTaskLogic().deleteSmsTask(insertTask);
		SmsTask getSmsTask = HibernateLogicFactory.getTaskLogic().getSmsTask(
				insertTask.getId());
		assertNull("Object not removed", getSmsTask);
	}
}
