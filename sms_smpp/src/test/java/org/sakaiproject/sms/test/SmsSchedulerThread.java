/***********************************************************************************
 * SmsSchedulerThread.java
 * Copyright (c) 2008 Sakai Project/Sakai Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.s
 *
 **********************************************************************************/
package org.sakaiproject.sms.test;

import java.util.Calendar;
import java.util.Date;

import net.sourceforge.groboutils.junit.v1.TestRunnable;

import org.apache.log4j.Level;
import org.sakaiproject.sms.hibernate.model.constants.SmsHibernateConstants;
import org.sakaiproject.sms.impl.SmsBillingImpl;
import org.sakaiproject.sms.impl.SmsCoreImpl;
import org.sakaiproject.sms.impl.SmsSchedulerImpl;
import org.sakaiproject.sms.impl.SmsSmppImpl;

/**
 * 
 * The Class SmsSchedulerThread. Used in the scheduler threading test.
 * 
 * @author Etienne@psybergate.co.za
 * 
 */
public class SmsSchedulerThread extends TestRunnable {

	private SmsCoreImpl smsCoreImpl = null;

	private SmsSchedulerImpl smsSchedulerImpl = null;

	private SmsSmppImpl smsSmppImpl = null;

	private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger
			.getLogger(SmsSchedulerThread.class);

	/** The session name. */
	private String sessionName;

	/**
	 * Sets up the required api's
	 * 
	 * @param sessionName
	 */
	public SmsSchedulerThread(String sessionName) {
		this.sessionName = sessionName;
		smsSchedulerImpl = new SmsSchedulerImpl();
		smsCoreImpl = new SmsCoreImpl();
		smsSmppImpl = new SmsSmppImpl();
		smsCoreImpl.setSmsBilling(new SmsBillingImpl());
		smsSmppImpl.init();
		smsCoreImpl.setSmsSmpp(smsSmppImpl);
		smsCoreImpl.enableDebugInformation(true);
		smsSchedulerImpl.setSmsCore(smsCoreImpl);
		LOG.setLevel(Level.ALL);
		smsSmppImpl.setLogLevel(Level.ALL);
		smsSchedulerImpl.init();
	}

	/**
	 * Inserts 3 new tasks to be processed. The test is successful if no more
	 * tasks exists to process.
	 */
	public void runTest() throws Throwable {
		LOG.info(sessionName + ": Inserting tasks ");
		smsSmppImpl.setLogLevel(Level.ALL);
		Calendar now = Calendar.getInstance();
		smsCoreImpl.insertTask(smsCoreImpl.getPreliminaryTask("SmsTask3"
				+ sessionName, new Date(now.getTimeInMillis()),
				"-ThreadingTest-SmsTask3MessageBody",
				SmsHibernateConstants.SMS_DEV_DEFAULT_SAKAI_ID, null));

		now.add(Calendar.MINUTE, -1);
		smsCoreImpl.insertTask(smsCoreImpl.getPreliminaryTask("SmsTask2"
				+ sessionName, new Date(now.getTimeInMillis()),
				"ThreadingTest-SmsTask2MessageBody",
				SmsHibernateConstants.SMS_DEV_DEFAULT_SAKAI_ID, null));

		now.add(Calendar.MINUTE, -3);
		smsCoreImpl.insertTask(smsCoreImpl.getPreliminaryTask("SmsTask1"
				+ sessionName, new Date(now.getTimeInMillis()),
				"ThreadingTest-SmsTask1MessageBody",
				SmsHibernateConstants.SMS_DEV_DEFAULT_SAKAI_ID, null));
		LOG.info(sessionName + ": Starting Sms Scheduler ");

		try {
			Thread.sleep(60000);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
		LOG.info(sessionName + ": 1 min passed ");
		assertTrue(smsCoreImpl.getNextSmsTask() == null);
		LOG.info(sessionName + ": Success ");
		smsSchedulerImpl.stopSmsScheduler();
		smsSmppImpl.disconnectGateWay();

	}
}