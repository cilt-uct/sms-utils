/***********************************************************************************
 * SmsCoreImpl.java
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
 * limitations under the License.
 *
 **********************************************************************************/
package org.sakaiproject.sms.impl;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Level;
import org.sakaiproject.sms.api.SmsCore;
import org.sakaiproject.sms.api.SmsSmpp;
import org.sakaiproject.sms.hibernate.logic.SmsTaskLogic;
import org.sakaiproject.sms.hibernate.model.SmsMessage;
import org.sakaiproject.sms.hibernate.model.SmsTask;
import org.sakaiproject.sms.hibernate.model.constants.SmsConst_DeliveryStatus;

public class SmsCoreImpl implements SmsCore {

	public static final int MAX_RETRY = 5;

	public static final int RESCHEDULE_TIMEOUT = 15;

	private final static org.apache.log4j.Logger LOG = org.apache.log4j.Logger
			.getLogger(SmsCoreImpl.class);

	public SmsSmpp smsSmpp = null;

	public SmsTaskLogic smsTaskLogic = null;

	public void setSmsSmpp(SmsSmpp smsSmpp) {
		this.smsSmpp = smsSmpp;
	}

	public void setSmsTaskLogic(SmsTaskLogic smsTaskLogic) {
		this.smsTaskLogic = smsTaskLogic;
	}

	public SmsSmpp getSmsSmpp() {
		return smsSmpp;
	}

	public SmsTaskLogic getSmsTaskLogic() {
		return smsTaskLogic;
	}

	/**
	 * Get the group list from Sakai
	 */
	public Set<SmsMessage> getDeliveryGroup(String sakaiSiteID,
			String sakaiGroupID, SmsTask smsTask) {
		return getDummyDeliveryGroup(smsTask);
	}

	/**
	 * For now we just generate the list. Will get it from Sakai later on. So we
	 * generate a random number of users with random mobile numbers.
	 * 
	 * @param smsTask
	 * @return
	 */
	private Set<SmsMessage> getDummyDeliveryGroup(SmsTask smsTask) {
		Set<SmsMessage> messages = new HashSet<SmsMessage>();
		String users[] = new String[100];
		String celnumbers[] = new String[100];
		for (int i = 0; i < users.length; i++) {
			users[i] = "SakaiUser" + i;
			celnumbers[i] = "+2773"
					+ (int) Math.round(Math.random() * 10000000);
		}
		for (int i = 0; i < (int) Math.round(Math.random() * 100); i++) {

			SmsMessage message = new SmsMessage();
			message.setMobileNumber(celnumbers[(int) Math
					.round(Math.random() * 99)]);
			message.setSakaiUserId(users[(int) Math.round(Math.random() * 99)]);
			message.setSmsTask(smsTask);
			messages.add(message);
		}
		return messages;
	}

	/**
	 * Find the next sms task to process from the task queue. Determine tasks
	 * with highest priority. Priority is based on message age and type.
	 */
	public SmsTask getNextSmsTask() {
		return smsTaskLogic.getNextSmsTask();

	}

	/**
	 * Get Sakai user's mobile number from member profile. Return the mobile
	 * number, null if not found.
	 * 
	 * @param sakaiUserID
	 */
	public String getSakaiMobileNumber(String sakaiUserID) {
		// TODO Auto-generated method stub
		return null;
	}

	public void insertIntoDebugLog() {
		// TODO Auto-generated method stub

	}

	/**
	 * Add a new task to the sms task list, for eg. send message to all
	 * administrators at 10:00, or get latest announcements and send to mobile
	 * numbers of Sakai group x (phase II).
	 */
	public void insertNewTask(SmsTask smsTask) {
		smsTaskLogic.persistSmsTask(smsTask);

	}

	/**
	 * Try to process an incoming message in real-time by inserting it into the
	 * queue and calling processMessage immediately. If unable to process, then
	 * leave in the queue for the job scheduler to handle. Incoming messages are
	 * for later development.
	 * 
	 * @param messageID
	 */
	public void processIncomingMessage(SmsMessage smsMessage) {
		// TODO Auto-generated method stub

	}

	/**
	 * Gets the next task to process. Based on specific criteria like status and
	 * date to sent.
	 */
	public void processNextTask() {
		SmsTask smsTask = smsTaskLogic.getNextSmsTask();
		if (smsTask != null) {
			this.processTask(smsTask);
		}
	}

	/**
	 * Process is specific task. A task can be retried if a previous send
	 * attempt was unsuccessful due to gateway connection problems. A retry will
	 * be re-scheduled some time in the future. When the max retry attempts are
	 * reached or if credits are insufficient, the task is marked as failed.
	 * 
	 * The task will also fail if it cannot be processed in a specified time.
	 * See http://jira.sakaiproject.org/jira/browse/SMS-9
	 */
	public void processTask(SmsTask smsTask) {
		smsTask.setStatusCode(SmsConst_DeliveryStatus.STATUS_BUSY);
		smsTask.setAttemptCount((smsTask.getAttemptCount()) + 1);
		if (smsTask.getAttemptCount() < MAX_RETRY) {
			if (smsTask.getAttemptCount() <= 1) {
				smsTask.setSmsMessagesOnTask(this.getDeliveryGroup(smsTask
						.getSakaiSiteId(), smsTask.getDeliveryGroupId(),
						smsTask));
				LOG.info("Total messages on task:="
						+ smsTask.getSmsMessages().size());
				smsTaskLogic.persistSmsTask(smsTask);
			}
			String submissionStatus = smsSmpp
					.sendMessagesToGateway(smsTask
							.getMessagesWithStatus(SmsConst_DeliveryStatus.STATUS_PENDING));
			smsTask.setStatusCode(submissionStatus);
			if (smsTask.getStatusCode().equals(
					SmsConst_DeliveryStatus.STATUS_INCOMPLETE)
					|| smsTask.getStatusCode().equals(
							SmsConst_DeliveryStatus.STATUS_RETRY)) {
				Calendar now = Calendar.getInstance();
				now.add(Calendar.MINUTE, +(RESCHEDULE_TIMEOUT));
				smsTask.rescheduleDateToSend(new Timestamp(now
						.getTimeInMillis()));
			}

		} else {
			smsTask.setStatusCode(SmsConst_DeliveryStatus.STATUS_FAIL);
			smsTask.setStatusForMessages(
					SmsConst_DeliveryStatus.STATUS_PENDING,
					SmsConst_DeliveryStatus.STATUS_FAIL);
		}
		smsTaskLogic.persistSmsTask(smsTask);
	}

	/**
	 * Enables or disables the debug Information
	 * 
	 * @param debug
	 */
	public void enableDebugInformation(boolean debug) {
		if (debug) {
			LOG.setLevel(Level.ALL);
		} else {
			LOG.setLevel(Level.OFF);
		}
	}
}
