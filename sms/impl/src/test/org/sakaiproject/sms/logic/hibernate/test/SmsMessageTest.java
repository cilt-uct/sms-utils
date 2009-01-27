package org.sakaiproject.sms.logic.hibernate.test;

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
import org.sakaiproject.sms.hibernate.util.HibernateUtil;
import org.sakaiproject.sms.util.AbstractBaseTestCase;

/**
 * The Class SmsMessageTest.
 */
public class SmsMessageTest extends AbstractBaseTestCase {

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
		insertTask.setSmsAccountId(1l);
		insertTask.setDateCreated(new Date(System.currentTimeMillis()));
		insertTask.setDateToSend(new Date(System.currentTimeMillis()));
		insertTask.setStatusCode(SmsConst_DeliveryStatus.STATUS_PENDING);
		insertTask.setAttemptCount(2);
		insertTask.setMessageBody("messageBody");
		insertTask.setSenderUserName("senderUserName");
		insertTask.setMaxTimeToLive(1);
		insertTask.setDelReportTimeoutDuration(1);
		// Insert the task so we can play with messages

		insertMessage1 = new SmsMessage();
		insertMessage1.setMobileNumber("0721998919");
		insertMessage1.setSmscMessageId("smscMessageId1");
		insertMessage1.setSmscId(SmsHibernateConstants.SMSC_ID);
		insertMessage1.setSakaiUserId("sakaiUserId");
		insertMessage1.setStatusCode(SmsConst_DeliveryStatus.STATUS_DELIVERED);

		insertMessage2 = new SmsMessage();
		insertMessage2.setMobileNumber("0823450983");
		insertMessage2.setSmscMessageId("smscMessageId2");
		insertMessage2.setSmscId(SmsHibernateConstants.SMSC_ID);
		insertMessage2.setSakaiUserId("sakaiUserId");
		insertMessage2.setStatusCode(SmsConst_DeliveryStatus.STATUS_PENDING);
	}

	/**
	 * Instantiates a new sms message test.
	 */
	public SmsMessageTest() {
	}

	/**
	 * Instantiates a new sms message test.
	 * 
	 * @param name
	 *            the name
	 */
	public SmsMessageTest(String name) {
		super(name);
	}

	/**
	 * Test insert sms message.
	 */
	public void testInsertSmsMessage() {
		HibernateLogicFactory.getTaskLogic().persistSmsTask(insertTask);
		assertTrue("Task for message not created", insertTask.exists());
		insertMessage1.setSmsTask(insertTask);
		insertMessage2.setSmsTask(insertTask);
		HibernateLogicFactory.getMessageLogic().persistSmsMessage(
				insertMessage1);
		HibernateLogicFactory.getMessageLogic().persistSmsMessage(
				insertMessage2);
		assertTrue("Object not persisted", insertMessage1.exists());
		assertTrue("Object not persisted", insertMessage2.exists());
		Set<SmsMessage> smsMessages = new HashSet<SmsMessage>();
		smsMessages.add(insertMessage2);
		smsMessages.add(insertMessage1);
		insertTask.setSmsMessagesOnTask(smsMessages);
		assertTrue("", insertTask.getSmsMessages().contains(insertMessage1));
		assertTrue("", insertTask.getSmsMessages().contains(insertMessage2));
		HibernateLogicFactory.getTaskLogic().persistSmsTask(insertTask);

	}

	/**
	 * Test get sms message by id.
	 */
	public void testGetSmsMessageById() {
		SmsMessage getSmsMessage = HibernateLogicFactory.getMessageLogic()
				.getSmsMessage(insertMessage1.getId());
		assertTrue("Object not persisted", insertMessage1.exists());
		assertNotNull(getSmsMessage);
		assertEquals(insertMessage1, getSmsMessage);

	}

	/**
	 * Test update sms message.
	 */
	public void testUpdateSmsMessage() {
		SmsMessage smsMessage = HibernateLogicFactory.getMessageLogic()
				.getSmsMessage(insertMessage1.getId());
		smsMessage.setSakaiUserId("newSakaiUserId");
		HibernateLogicFactory.getMessageLogic().persistSmsMessage(smsMessage);
		smsMessage = HibernateLogicFactory.getMessageLogic().getSmsMessage(
				insertMessage1.getId());
		assertEquals("newSakaiUserId", smsMessage.getSakaiUserId());
	}

	/**
	 * Tests the getMessagesForCriteria method
	 */
	public void testGetMessagesForCriteria() {

		SmsTask insertTask = new SmsTask();
		insertTask.setSakaiSiteId("sakaiSiteId");
		insertTask.setSmsAccountId(1l);
		insertTask.setDateCreated(new Date(System.currentTimeMillis()));
		insertTask.setDateToSend(new Date(System.currentTimeMillis()));
		insertTask.setStatusCode(SmsConst_DeliveryStatus.STATUS_PENDING);
		insertTask.setAttemptCount(2);
		insertTask.setMessageBody("messageCrit");
		insertTask.setSenderUserName("messageCrit");
		insertTask.setSakaiToolName("sakaiToolName");
		insertTask.setMaxTimeToLive(1);
		insertTask.setDelReportTimeoutDuration(1);

		SmsMessage insertMessage = new SmsMessage();
		insertMessage.setMobileNumber("0721998919");
		insertMessage.setSmscMessageId("criterai");
		insertMessage.setSakaiUserId("criterai");
		insertMessage.setDateDelivered(new Date(System.currentTimeMillis()));
		insertMessage.setStatusCode(SmsConst_DeliveryStatus.STATUS_ERROR);

		insertMessage.setSmsTask(insertTask);
		insertTask.getSmsMessages().add(insertMessage);

		try {
			HibernateLogicFactory.getTaskLogic().persistSmsTask(insertTask);

			SearchFilterBean bean = new SearchFilterBean();
			bean.setStatus(insertMessage.getStatusCode());
			bean.setDateFrom((new Date()));
			bean.setDateTo((new Date()));
			bean.setToolName(insertTask.getSakaiToolName());
			bean.setSender(insertTask.getSenderUserName());
			bean.setNumber(insertMessage.getMobileNumber());
			bean.setCurrentPage(1);

			SearchResultContainer<SmsMessage> con = HibernateLogicFactory
					.getMessageLogic().getPagedSmsMessagesForCriteria(bean);
			List<SmsMessage> messages = con.getPageResults();
			assertTrue("Collection returned has no objects",
					messages.size() > 0);

			for (SmsMessage message : messages) {
				// We know that only one message should be returned becuase
				// we only added one with status ERROR.
				message.equals(insertMessage);
				assertEquals(message, insertMessage);
			}
		} catch (SmsSearchException se) {
			fail(se.getMessage());
		} finally {
			SmsTask task = HibernateLogicFactory.getTaskLogic().getSmsTask(
					insertTask.getId());
			HibernateLogicFactory.getTaskLogic().deleteSmsTask(task);
		}
	}

	/**
	 * Tests the getMessagesForCriteria method
	 */
	public void testGetMessagesForCriteria_Paging() {

		int recordsToInsert = 93;

		SmsTask insertTask = new SmsTask();
		insertTask.setSakaiSiteId("sakaiSiteId");
		insertTask.setSmsAccountId(1l);
		insertTask.setDateCreated(new Date(System.currentTimeMillis()));
		insertTask.setDateToSend(new Date(System.currentTimeMillis()));
		insertTask.setStatusCode(SmsConst_DeliveryStatus.STATUS_PENDING);
		insertTask.setAttemptCount(2);
		insertTask.setMessageBody("messageCrit");
		insertTask.setSenderUserName("messageCrit");
		insertTask.setSakaiToolName("sakaiToolName");
		insertTask.setMaxTimeToLive(1);
		insertTask.setDelReportTimeoutDuration(1);

		for (int i = 0; i < recordsToInsert; i++) {
			SmsMessage insertMessage = new SmsMessage();
			insertMessage.setMobileNumber("0721998919");
			insertMessage.setSmscMessageId("criterai");
			insertMessage.setSakaiUserId("crit");
			insertMessage.setStatusCode(SmsConst_DeliveryStatus.STATUS_ERROR);
			insertMessage.setSmsTask(insertTask);
			insertTask.getSmsMessages().add(insertMessage);
			insertMessage.setSmscMessageId("" + i);// To make unique
			insertMessage
					.setDateDelivered(new Date(System.currentTimeMillis()));
		}

		try {
			HibernateLogicFactory.getTaskLogic().persistSmsTask(insertTask);

			SearchFilterBean bean = new SearchFilterBean();
			bean.setStatus(SmsConst_DeliveryStatus.STATUS_ERROR);
			bean.setDateFrom(new Date(System.currentTimeMillis() - 10000));
			bean.setDateTo(new Date());
			bean.setToolName(insertTask.getSakaiToolName());
			bean.setSender(insertTask.getSenderUserName());
			bean.setNumber("0721998919");

			bean.setCurrentPage(2);

			SearchResultContainer<SmsMessage> con = HibernateLogicFactory
					.getMessageLogic().getPagedSmsMessagesForCriteria(bean);
			List<SmsMessage> messages = con.getPageResults();
			assertTrue("Incorrect collection size returned",
					messages.size() == SmsHibernateConstants.DEFAULT_PAGE_SIZE);

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

			con = HibernateLogicFactory.getMessageLogic()
					.getPagedSmsMessagesForCriteria(bean);
			messages = con.getPageResults();
			int lastPageRecordCount = recordsToInsert
					- (pages * SmsHibernateConstants.DEFAULT_PAGE_SIZE);
			assertTrue("Incorrect collection size returned",
					messages.size() == lastPageRecordCount);

		} catch (SmsSearchException se) {
			fail(se.getMessage());
		} finally {
			SmsTask task = HibernateLogicFactory.getTaskLogic().getSmsTask(
					insertTask.getId());
			HibernateLogicFactory.getTaskLogic().deleteSmsTask(task);
		}
	}

	/**
	 * Test get new test sms message instance.
	 */
	public void testGetNewTestSmsMessageInstance() {
		SmsMessage message = HibernateLogicFactory.getMessageLogic()
				.getNewTestSmsMessageInstance("0721999988", "Message body");
		HibernateLogicFactory.getTaskLogic().persistSmsTask(
				message.getSmsTask());
		assertNotNull("Message returned was null", message);
		assertNotNull("", message.getSmsTask());
		assertTrue("Associated SmsTask not created successfully", message
				.getSmsTask().exists());
	}

	/**
	 * Test get sms message by smsc message id.
	 */
	public void testGetSmsMessageBySmscMessageId() {
		SmsMessage smsMessage = HibernateLogicFactory.getMessageLogic()
				.getSmsMessageBySmscMessageId(
						insertMessage2.getSmscMessageId(),
						SmsHibernateConstants.SMSC_ID);
		assertTrue(smsMessage.equals(insertMessage2));
		assertEquals(smsMessage.getSmscMessageId(), insertMessage2
				.getSmscMessageId());
	}

	/**
	 * Test get sms messages.
	 */
	public void testGetSmsMessages() {
		List<SmsMessage> messages = HibernateLogicFactory.getMessageLogic()
				.getAllSmsMessages();
		assertNotNull("Returned collection is null", messages);
		assertTrue("No records returned", messages.size() > 0);
	}

	/**
	 * Tests getSmsMessagesWithStatus returns only messages with the specifed
	 * status codes
	 */
	public void testGetSmsMessagesWithStatus() {

		// Assert that messages exist for this task that have a status other
		// than PENDING
		SmsTask task = HibernateLogicFactory.getTaskLogic().getSmsTask(
				insertTask.getId());
		task.getSmsMessages().size();
		boolean otherStatusFound = false;
		for (SmsMessage message : task.getSmsMessages()) {
			if (message.getStatusCode().equals(
					SmsConst_DeliveryStatus.STATUS_PENDING)) {
				otherStatusFound = true;
				break;
			}
		}
		assertTrue(
				"This test requires that messages exist for this task that have a status other than PENDING",
				otherStatusFound);

		List<SmsMessage> messages = HibernateLogicFactory.getMessageLogic()
				.getSmsMessagesWithStatus(insertTask.getId(),
						SmsConst_DeliveryStatus.STATUS_PENDING);

		// We expect some records to be returned
		assertTrue("Expected objects in collection", messages.size() > 0);

		// We know there are messages for this task that have status codes other
		// than the one specifies above
		// So assert that the method only returned ones with the specified
		// status.
		for (SmsMessage message : messages) {
			assertTrue("Incorrect value returned for object", message
					.getStatusCode().equals(
							SmsConst_DeliveryStatus.STATUS_PENDING));
		}
	}

	/**
	 * Test delete sms message.
	 */
	public void testDeleteSmsMessage() {
		// Delete the associated task too
		HibernateLogicFactory.getTaskLogic().deleteSmsTask(insertTask);
		SmsTask getSmsTask = HibernateLogicFactory.getTaskLogic().getSmsTask(
				insertTask.getId());
		assertNull("Object not removed", getSmsTask);
	}

}