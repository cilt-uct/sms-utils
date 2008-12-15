package org.sakaiproject.sms.hibernate.test;

import java.util.Date;

import org.hibernate.Session;
import org.sakaiproject.sms.hibernate.logic.impl.SmsMessageLogicImpl;
import org.sakaiproject.sms.hibernate.logic.impl.SmsTaskLogicImpl;
import org.sakaiproject.sms.hibernate.model.SmsMessage;
import org.sakaiproject.sms.hibernate.model.SmsTask;
import org.sakaiproject.sms.hibernate.model.constants.SmsConst_DeliveryStatus;
import org.sakaiproject.sms.hibernate.util.AbstractBaseTestCase;
import org.sakaiproject.sms.hibernate.util.HibernateUtil;

/**
 * The Class smsHibernateStressTest. We work with many messages to see how the
 * database performs. The following steps are executed: 1) Create a sms task 2)
 * create [messageCount] messages for the task 3) Read back the task with all
 * its messages, 4) read back one of the messages, 5)remove the task and its
 * messages
 */
public class SmsDatabaseStressTest extends AbstractBaseTestCase {

	static {
		HibernateUtil.createSchema();
	}

	/** The number of messages to insert, change as required. */
	private static int messageCount = 5000;

	/** The task logic. */
	private static SmsTaskLogicImpl taskLogic = null;

	/** The message logic. */
	private static SmsMessageLogicImpl messageLogic = null;

	/** The first message id. */
	private static long firstMessageID;

	/** The is of the new task. */
	private static long smsTaskID;

	/** The sms task. */
	private static SmsTask smsTask;

	/**
	 * we want to flush the hibernate cache
	 * 
	 */
	private static Session session;

	/**
	 * Instantiates a new sms hibernate stress test.
	 */
	public SmsDatabaseStressTest() {
		taskLogic = new SmsTaskLogicImpl();
		messageLogic = new SmsMessageLogicImpl();
		smsTask = new SmsTask();
		smsTask.setSakaiSiteId("sakaiSiteId");
		smsTask.setSmsAccountId(1);
		smsTask.setDateCreated(new Date(System.currentTimeMillis()));
		smsTask.setDateToSend(new Date(System.currentTimeMillis()));
		smsTask.setStatusCode("SC");
		smsTask.setAttemptCount(2);
		smsTask.setMessageBody("messageBody");
		smsTask.setSenderUserName("senderUserName");
		smsTask.setMaxTimeToLive(1);
		smsTask.setDelReportTimeoutDuration(1);
	}

	/**
	 * Test many messages insert.
	 */
	public void testInsertManyMessages() {

		for (int i = 0; i < messageCount; i++) {
			SmsMessage smsMessage = new SmsMessage();
			smsMessage.setMobileNumber("0823450983");
			smsMessage.setSmscMessageId("smscMessage_" + i);
			smsMessage.setSakaiUserId("sakaiUserId");
			smsMessage.setStatusCode(SmsConst_DeliveryStatus.STATUS_PENDING);
			smsMessage.setSmsTask(smsTask);
			smsTask.getSmsMessages().add(smsMessage);
		}
		taskLogic.persistSmsTask(smsTask);
		smsTaskID = smsTask.getId();
		assertTrue("Not all messages returned",
				smsTask.getSmsMessages().size() == messageCount);

	}

	/**
	 * Test get task messages.
	 */
	public void testGetTaskMessages() {
		SmsTask theSmsTask = taskLogic.getSmsTask(smsTaskID);
		firstMessageID = ((SmsMessage) theSmsTask.getSmsMessages().toArray()[0])
				.getId();
		assertNotNull(theSmsTask);
		assertTrue("Message size not correct", theSmsTask.getSmsMessages()
				.size() == messageCount);

	}

	// TODO: Careful, the attached task read all the messages attached to it, we
	// don't want this here!
	/**
	 * Test get task message.
	 */
	public void testGetTaskMessage() {

		SmsMessage theMessage = messageLogic.getSmsMessage(firstMessageID);
		assertNotNull(theMessage);
	}

	/**
	 * Test delete tasks.
	 */
	public void testDeleteTasks() {

		taskLogic.deleteSmsTask(smsTask);
		SmsTask getSmsTask = taskLogic.getSmsTask(smsTaskID);
		assertNull("Task not removed", getSmsTask);
		SmsMessage theMessage = messageLogic.getSmsMessage(firstMessageID);
		assertNull("Messages not removed", theMessage);
	}
}
