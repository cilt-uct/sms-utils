package org.sakaiproject.sms.test;

import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Level;
import org.sakaiproject.sms.hibernate.logic.impl.SmsConfigLogicImpl;
import org.sakaiproject.sms.hibernate.logic.impl.SmsMessageLogicImpl;
import org.sakaiproject.sms.hibernate.logic.impl.SmsTaskLogicImpl;
import org.sakaiproject.sms.hibernate.model.SmsTask;
import org.sakaiproject.sms.hibernate.util.AbstractBaseTestCase;
import org.sakaiproject.sms.hibernate.util.HibernateUtil;
import org.sakaiproject.sms.impl.SmsBillingImpl;
import org.sakaiproject.sms.impl.SmsCoreImpl;
import org.sakaiproject.sms.impl.SmsSchedulerImpl;
import org.sakaiproject.sms.impl.SmsSmppImpl;

/**
 * SmsScheduler Junit.This class will test various scheduling related scenarios.
 * 
 * @author Etienne@psybergate.co.za
 * 
 */
public class SmsScheduler extends AbstractBaseTestCase {

	static SmsCoreImpl smsCoreImpl = null;
	static SmsSchedulerImpl smsSchedulerImpl = null;
	static SmsSmppImpl smsSmppImpl = null;

	private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger
			.getLogger(SmsCoreTest.class);

	static {
		HibernateUtil.createSchema();
		smsSchedulerImpl = new SmsSchedulerImpl();
		smsCoreImpl = new SmsCoreImpl();
		smsSmppImpl = new SmsSmppImpl();
		smsCoreImpl.setSmsBilling(new SmsBillingImpl());
		smsCoreImpl.setSmsMessageLogic(new SmsMessageLogicImpl());
		smsSmppImpl.setSmsMessageLogic(new SmsMessageLogicImpl());
		smsSmppImpl.setSmsTaskLogic(new SmsTaskLogicImpl());
		smsSmppImpl.init();
		smsCoreImpl.setSmsSmpp(smsSmppImpl);
		smsCoreImpl.setSmsTaskLogic(new SmsTaskLogicImpl());
		smsCoreImpl.setSmsConfigLogic(new SmsConfigLogicImpl());
		smsCoreImpl.enableDebugInformation(true);
		smsSchedulerImpl.setSmsCore(smsCoreImpl);
		smsSchedulerImpl.setSmsConfigLogic(new SmsConfigLogicImpl());
		smsSchedulerImpl.init();

		LOG.setLevel(Level.ALL);
	}

	/**
	 * This tests will insert 3 tasks to to processed.The test succeeds if no
	 * tasks remain after 1 min.
	 */
	public void testTaskProcessing() {
		Calendar now = Calendar.getInstance();
		SmsTask smsTask3 = smsCoreImpl.insertNewTask("smsTask3", new Date(now
				.getTimeInMillis()), "smsTask3", null);

		now.add(Calendar.MINUTE, -1);
		SmsTask smsTask2 = smsCoreImpl.insertNewTask("smsTask2", new Date(now
				.getTimeInMillis()), "smsTask2MessageBody", null);

		now.add(Calendar.MINUTE, -3);
		SmsTask smsTask1 = smsCoreImpl.insertNewTask("smsTask1", new Date(now
				.getTimeInMillis()), "smsTask1MessageBody", null);

		try {
			Thread.sleep(60000);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
		assertTrue(smsCoreImpl.getNextSmsTask() == null);
	}

	@Override
	protected void tearDown() throws Exception {
		smsSchedulerImpl.stopSmsScheduler();
	}
}
