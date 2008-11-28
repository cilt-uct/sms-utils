package org.sakaiproject.test;

import java.sql.Timestamp;
import java.util.Calendar;

import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.sakaiproject.sms.hibernate.logic.impl.SmsTaskLogicImpl;
import org.sakaiproject.sms.hibernate.model.SmsTask;
import org.sakaiproject.sms.hibernate.model.constants.SmsConst_DeliveryStatus;
import org.sakaiproject.sms.impl.SmsCoreImpl;
import org.sakaiproject.sms.impl.SmsSmppImpl;

public class SmsCoreTest extends TestCase {
	SmsCoreImpl smsCoreImpl = new SmsCoreImpl();
	private final static org.apache.log4j.Logger LOG = org.apache.log4j.Logger
			.getLogger(SmsCoreTest.class);

	protected void setUp() throws Exception {
		LOG.setLevel(Level.ALL);
	}

	/*
	 * This is an helper method to insert a dummy smsTask into the Database. The
	 * sakaiID is used to identify the temp task.
	 */
	public SmsTask insertNewTask(String sakaiID, String status,
			Timestamp dateToSend, int attemptCount) {
		SmsTask insertTask = new SmsTask();
		insertTask.setSakaiSiteId(sakaiID);
		insertTask.setSmsAccountId(0);
		insertTask.setDateCreated(new Timestamp(System.currentTimeMillis()));
		insertTask.setDateToSend(dateToSend);
		insertTask.setStatusCode(status);
		insertTask.setAttemptCount(0);
		insertTask.setMessageBody("testing1234567");
		insertTask.setSenderUserName("administrator");
		smsCoreImpl.setSmsTaskLogic(new SmsTaskLogicImpl());
		smsCoreImpl.getSmsTaskLogic().persistSmsTask(insertTask);
		return insertTask;

	}

	/*
	 * In this test the populating of SmsMessages is tested.The test succeeds if
	 * the smsTask's messages are bigger then 0 .
	 */
	public void testGetDeliveryGroup() {

		SmsTask smsTask = new SmsTask();
		smsTask.setMessageBody("tesing sms");
		smsTask.setSmsMessagesOnTask(smsCoreImpl.getDeliveryGroup("1234566789",
				"group1", smsTask));
		assertEquals(true, smsTask.getSmsMessages() != null
				&& smsTask.getSmsMessages().size() > 0);

	}

	/*
	 * In this test the smsc is not bound.The task is executed 5 times to
	 * simulate the scheduler retrying and eventually failing.
	 */
	public void testProcessTaskFail() {
		SmsTask smsTask = insertNewTask("testProcessTaskFail",
				SmsConst_DeliveryStatus.STATUS_FAIL, new Timestamp(System
						.currentTimeMillis()), 1);
		SmsSmppImpl smsSmppImpl = new SmsSmppImpl();
		smsSmppImpl.setLogLevel(Level.OFF);
		smsCoreImpl.setSmsSmpp(smsSmppImpl);
		smsCoreImpl.setSmsTaskLogic(new SmsTaskLogicImpl());

		for (int i = 0; i < 5; i++) {
			smsCoreImpl.processTask(smsTask);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
		assertEquals(true, smsTask.getStatusCode().equals(
				SmsConst_DeliveryStatus.STATUS_FAIL));
		smsCoreImpl.getSmsTaskLogic().deleteSmsTask(smsTask);
	}

	/*
	 * In this test the updating of smsStatuses is tested. First a new task is
	 * created and populated with smsMessages.The total number of pending
	 * messages must equal 0 at the end.The total sent messages must equal the
	 * total messages on the task.
	 */
	public void testMessageStatusUpdate() {

		SmsSmppImpl smsSmppImpl = new SmsSmppImpl();
		smsSmppImpl.init();
		smsSmppImpl.setLogLevel(Level.OFF);
		smsCoreImpl.setSmsSmpp(smsSmppImpl);
		smsCoreImpl.setSmsTaskLogic(new SmsTaskLogicImpl());
		if (smsCoreImpl.getSmsSmpp().getConnectionStatus()) {
			SmsTask smsTask = insertNewTask("testMessageStatusUpdate",
					SmsConst_DeliveryStatus.STATUS_PENDING, new Timestamp(
							System.currentTimeMillis()), 0);
			smsTask.setSmsMessagesOnTask(smsCoreImpl.getDeliveryGroup(
					"1234566789", "group1", smsTask));
			LOG
					.info("SMS-messages on task: "
							+ smsTask.getSmsMessages().size());
			LOG.info("SMS-messages Pending: "
					+ smsTask.getMessagesWithStatus(
							SmsConst_DeliveryStatus.STATUS_PENDING).size());
			LOG.info("sending Messages To Gateway");
			smsSmppImpl.sendMessagesToGateway(smsTask.getSmsMessages());
			LOG.info("SMS-messages Pending: "
					+ smsTask.getMessagesWithStatus(
							SmsConst_DeliveryStatus.STATUS_PENDING).size());
			LOG.info("SMS-messages STATUS_SENT: "
					+ smsTask.getMessagesWithStatus(
							SmsConst_DeliveryStatus.STATUS_SENT).size());

			assertEquals(true, smsTask.getMessagesWithStatus(
					SmsConst_DeliveryStatus.STATUS_PENDING).size() == 0);
			assertEquals(true, smsTask.getMessagesWithStatus(
					SmsConst_DeliveryStatus.STATUS_SENT).size() == smsTask
					.getSmsMessages().size());
			smsCoreImpl.getSmsTaskLogic().deleteSmsTask(smsTask);
		}
	}

	/*
	 * In this test the ProcessNextTask method is tested. 4 smsTasks are created
	 * with different sending times and statuses.The ProcessNextTask method must
	 * pick up the oldest SmsTask with an (pending/incomplete/reply) status. The
	 * test succeeds if the Smstasks are returned in the proper order.
	 */
	public void testProcessNextTask() {

		SmsSmppImpl smsSmppImpl = new SmsSmppImpl();
		smsSmppImpl.init();
		smsSmppImpl.setLogLevel(Level.OFF);
		smsCoreImpl.setSmsSmpp(smsSmppImpl);
		smsCoreImpl.setSmsTaskLogic(new SmsTaskLogicImpl());

		if (smsCoreImpl.getSmsSmpp().getConnectionStatus()) {

			Calendar now = Calendar.getInstance();
			SmsTask smsTask3 = insertNewTask("smsTask3",
					SmsConst_DeliveryStatus.STATUS_PENDING, new Timestamp(now
							.getTimeInMillis()), 0);
			now.add(Calendar.MINUTE, -15);
			SmsTask smsTask2 = insertNewTask("smsTask2",
					SmsConst_DeliveryStatus.STATUS_INCOMPLETE, new Timestamp(
							now.getTimeInMillis()), 0);
			now.add(Calendar.MINUTE, -55);
			SmsTask smsTask1 = insertNewTask("smsTask1",
					SmsConst_DeliveryStatus.STATUS_PENDING, new Timestamp(now
							.getTimeInMillis()), 0);
			now.add(Calendar.MINUTE, 120);
			SmsTask smsTask4 = insertNewTask("smsTask4",
					SmsConst_DeliveryStatus.STATUS_RETRY, new Timestamp(now
							.getTimeInMillis()), 0);

			assertEquals(true, smsTask1.getId() == smsCoreImpl.getNextSmsTask()
					.getId());
			smsCoreImpl.processNextTask();
			assertEquals(true, smsTask2.getId() == smsCoreImpl.getNextSmsTask()
					.getId());
			smsCoreImpl.processNextTask();
			assertEquals(true, smsTask3.getId() == smsCoreImpl.getNextSmsTask()
					.getId());
			smsCoreImpl.processNextTask();
			assertEquals(true, smsCoreImpl.getNextSmsTask() == null);
			smsCoreImpl.getSmsTaskLogic().deleteSmsTask(smsTask1);
			smsCoreImpl.getSmsTaskLogic().deleteSmsTask(smsTask2);
			smsCoreImpl.getSmsTaskLogic().deleteSmsTask(smsTask3);
			smsCoreImpl.getSmsTaskLogic().deleteSmsTask(smsTask4);
		}
	}
}
