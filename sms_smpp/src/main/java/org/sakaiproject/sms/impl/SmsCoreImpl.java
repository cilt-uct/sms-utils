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

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Level;
import org.sakaiproject.sms.api.SmsBilling;
import org.sakaiproject.sms.api.SmsCore;
import org.sakaiproject.sms.api.SmsSmpp;
import org.sakaiproject.sms.hibernate.logic.impl.HibernateLogicFactory;
import org.sakaiproject.sms.hibernate.model.SmsConfig;
import org.sakaiproject.sms.hibernate.model.SmsMessage;
import org.sakaiproject.sms.hibernate.model.SmsTask;
import org.sakaiproject.sms.hibernate.model.constants.SmsConst_DeliveryStatus;
import org.sakaiproject.sms.hibernate.model.constants.SmsHibernateConstants;
import org.sakaiproject.sms.hibernate.util.DateUtil;

/**
 * Handle all logic regarding SMPP gateway communication.
 * 
 * @author etienne@psybergate.co.za
 * 
 */
public class SmsCoreImpl implements SmsCore {

	private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger
			.getLogger(SmsCoreImpl.class);

	public SmsSmpp smsSmpp = null;

	public SmsBilling smsBilling = null;

	public SmsBilling getSmsBilling() {
		return smsBilling;
	}

	public void setSmsBilling(SmsBilling smsBilling) {
		this.smsBilling = smsBilling;
	}

	public void setSmsSmpp(SmsSmpp smsSmpp) {
		this.smsSmpp = smsSmpp;
	}

	public SmsSmpp getSmsSmpp() {
		return smsSmpp;
	}

	public void init() {

	}

	/**
	 * Get the group list from Sakai
	 */
	// TODO Why pass three args when they can all come from the SmsTask?? Only 1
	// arg needed.. smsTask
	public Set<SmsMessage> generateSmsMessages(SmsTask smsTask,
			Set<String> sakaiUserIDs) {
		return generateDummySmsMessages(smsTask, sakaiUserIDs);
		// TODO must make a Sakai call here
	}

	/**
	 * For now we just generate the list. Will get it from Sakai later on. So we
	 * generate a random number of users with random mobile numbers.
	 * 
	 * @param smsTask
	 * @return
	 */
	private Set<SmsMessage> generateDummySmsMessages(SmsTask smsTask,
			Set<String> sakaiUserIDs) {
		Set<SmsMessage> messages = new HashSet<SmsMessage>();

		String[] users;
		int numberOfMessages = (int) Math.round(Math.random() * 100);
		if (sakaiUserIDs != null) {
			users = sakaiUserIDs.toArray(new String[0]);
			numberOfMessages = users.length;
		} else {
			users = new String[100];
		}

		String[] celnumbers = new String[100];
		for (int i = 0; i < users.length; i++) {
			if (sakaiUserIDs == null) {
				users[i] = "SakaiUser" + i;
			}
			celnumbers[i] = "+2773"
					+ (int) Math.round(Math.random() * 10000000);
		}
		for (int i = 0; i < numberOfMessages; i++) {

			SmsMessage message = new SmsMessage();
			message.setMobileNumber(celnumbers[(int) Math
					.round(Math.random() * 99)]);
			if (sakaiUserIDs != null) {
				message.setSakaiUserId(users[i]);

			} else {
				message.setSakaiUserId(users[(int) Math
						.round(Math.random() * 99)]);
			}
			message.setSmsTask(smsTask);
			messages.add(message);
		}
		return messages;
	}

	public SmsTask getNextSmsTask() {
		return HibernateLogicFactory.getTaskLogic().getNextSmsTask();

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

	public SmsTask getPreliminaryTask(String deliverGroupId, Date dateToSend,
			String messageBody, String sakaiToolId) {
		return getPreliminaryTask(deliverGroupId, null, null, dateToSend,
				messageBody, sakaiToolId);
	}

	public SmsTask getPreliminaryTask(Set<String> sakaiUserIds,
			Date dateToSend, String messageBody, String sakaiToolId) {

		return getPreliminaryTask(null, null, sakaiUserIds, dateToSend,
				messageBody, sakaiToolId);

	}

	/**
	 * Add a new task to the sms task list, for eg. send message to all
	 * administrators at 10:00, or get latest announcements and send to mobile
	 * numbers of Sakai group x (phase II).
	 * 
	 * @param deliverGroupId
	 * @param mobileNumbers
	 * @param sakaiUserIds
	 * @param dateToSend
	 * @param messageBody
	 * @param sakaiToolId
	 * @return
	 */
	public synchronized SmsTask insertTask(SmsTask smsTask) {
		smsTask.setDateCreated(DateUtil.getCurrentDate());
		smsBilling.reserveCredits(smsBilling.getAccountID(smsTask
				.getSakaiSiteId(), smsTask.getSenderUserName(), smsTask
				.getSmsAccountId()), smsTask.getSmsMessages().size());

		HibernateLogicFactory.getTaskLogic().persistSmsTask(smsTask);
		tryProcessTaskRealTime(smsTask);
		return smsTask;
	}

	// TODO Why does this not process all the messages for a group
	public void processIncomingMessage(SmsMessage smsMessage) {

		// insert into queue
		HibernateLogicFactory.getTaskLogic().persistSmsTask(
				smsMessage.getSmsTask());

		// TODO Check with Louis about this functionality
		// TODO Check what the status codes should be set to for task and
		// messages
		if (smsMessage.getSmsTask().getDateToSend().getTime() <= System
				.currentTimeMillis()) {
			// Send to gateway
			smsMessage = smsSmpp.sendMessageToGateway(smsMessage);

			// Check if there were any problems sending the message
			if (!smsMessage.getStatusCode().equals(
					SmsConst_DeliveryStatus.STATUS_SENT)) {
				// Problem experienced, set as incomplete and leave for the
				// scheduler to process
				smsMessage.getSmsTask().setStatusCode(
						SmsConst_DeliveryStatus.STATUS_INCOMPLETE);
				smsMessage.getSmsTask().setMessageTypeId(
						SmsHibernateConstants.SMS_TASK_TYPE_PROCESS_SCHEDULED);
				HibernateLogicFactory.getTaskLogic().persistSmsTask(
						smsMessage.getSmsTask());
			}
		} else {
			// Add to the que for the scheduler to process
			smsMessage.getSmsTask().setStatusCode(
					SmsConst_DeliveryStatus.STATUS_PENDING);
			smsMessage.getSmsTask().setMessageTypeId(
					SmsHibernateConstants.SMS_TASK_TYPE_PROCESS_SCHEDULED);
			HibernateLogicFactory.getTaskLogic().persistSmsTask(
					smsMessage.getSmsTask());
		}
	}

	/**
	 * Get the next task to process. Based on specific criteria like status and
	 * date to sent.
	 */
	public synchronized void processNextTask() {
		SmsTask smsTask = HibernateLogicFactory.getTaskLogic().getNextSmsTask();
		if (smsTask != null) {
			this.processTask(smsTask);
		}
	}

	public void tryProcessTaskRealTime(SmsTask smsTask) {

		// TODO also check number of process threads
		if (smsTask.getDateToSend().getTime() <= System.currentTimeMillis()) {
			this.processTask(smsTask);
		}
	}

	public void processTask(SmsTask smsTask) {
		SmsConfig config = HibernateLogicFactory.getConfigLogic()
				.getOrCreateSmsConfigBySakaiSiteId(smsTask.getSakaiSiteId());
		smsTask.setDateProcessed(new Date());
		smsTask.setAttemptCount((smsTask.getAttemptCount()) + 1);
		Calendar cal = Calendar.getInstance();
		cal.setTime(smsTask.getDateToSend());
		cal.add(Calendar.SECOND, smsTask.getMaxTimeToLive());

		if (cal.getTime().before(new Date())) {
			smsTask.setStatusCode(SmsConst_DeliveryStatus.STATUS_EXPIRE);
			smsTask.setStatusForMessages(
					SmsConst_DeliveryStatus.STATUS_PENDING,
					SmsConst_DeliveryStatus.STATUS_EXPIRE);
			HibernateLogicFactory.getTaskLogic().persistSmsTask(smsTask);
			return;
		}

		smsTask.setStatusCode(SmsConst_DeliveryStatus.STATUS_BUSY);
		if (smsTask.getAttemptCount() < config.getSmsRetryMaxCount()) {
			if (smsTask.getAttemptCount() <= 1) {

				// TODO: we need to generate messages based on a list of userIDs
				// or mobileNumbers
				smsTask.setSmsMessagesOnTask(this.generateSmsMessages(smsTask,
						null));
				LOG.info("Total messages on task:="
						+ smsTask.getSmsMessages().size());
				HibernateLogicFactory.getTaskLogic().persistSmsTask(smsTask);
			}
			String submissionStatus = smsSmpp
					.sendMessagesToGateway(smsTask
							.getMessagesWithStatus(SmsConst_DeliveryStatus.STATUS_PENDING));
			smsTask = HibernateLogicFactory.getTaskLogic().getSmsTask(
					smsTask.getId());
			smsTask.setStatusCode(submissionStatus);

			if (smsTask.getStatusCode().equals(
					SmsConst_DeliveryStatus.STATUS_INCOMPLETE)
					|| smsTask.getStatusCode().equals(
							SmsConst_DeliveryStatus.STATUS_RETRY)) {
				Calendar now = Calendar.getInstance();
				now.add(Calendar.MINUTE,
						+(config.getSmsRetryScheduleInterval()));
				smsTask.rescheduleDateToSend(new Date(now.getTimeInMillis()));
			}

		} else {
			smsTask.setStatusCode(SmsConst_DeliveryStatus.STATUS_FAIL);
			smsTask.setStatusForMessages(
					SmsConst_DeliveryStatus.STATUS_PENDING,
					SmsConst_DeliveryStatus.STATUS_FAIL);
		}
		HibernateLogicFactory.getTaskLogic().persistSmsTask(smsTask);
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

	/**
	 * Updates the messages status to timedout which did not receive a delivery
	 * report within the valid period.As determined by
	 * DEL_REPORT_TIMEOUT_DURATION on the task.
	 */
	public void processTimedOutDeliveryReports() {
		List<SmsMessage> smsMessages = HibernateLogicFactory.getMessageLogic()
				.getSmsMessagesWithStatus(null,
						SmsConst_DeliveryStatus.STATUS_SENT);

		if (smsMessages != null) {
			for (SmsMessage message : smsMessages) {
				SmsTask task = message.getSmsTask();
				Calendar cal = Calendar.getInstance();
				cal.setTime(task.getDateProcessed());
				cal.add(Calendar.SECOND, task.getDelReportTimeoutDuration());
				if (cal.getTime().before(new Date())) {
					message
							.setStatusCode(SmsConst_DeliveryStatus.STATUS_TIMEOUT);
					HibernateLogicFactory.getMessageLogic().persistSmsMessage(
							message);
				}

			}
		}

	}

	public SmsTask getPreliminaryTask(String deliverGroupId,
			Set<String> mobileNumbers, Set<String> sakaiUserIds,
			Date dateToSend, String messageBody, String sakaiToolId) {
		SmsTask smsTask = new SmsTask();

		smsTask.setSmsAccountId(smsBilling.getAccountID("1", "1", 1));
		smsTask.setStatusCode(SmsConst_DeliveryStatus.STATUS_PENDING);
		smsTask.setSakaiSiteId(SmsHibernateConstants.SMS_DEV_DEFAULT_SAKAI_ID);
		// TODO Populate from Sakai
		smsTask
				.setMessageTypeId(SmsHibernateConstants.SMS_TASK_TYPE_PROCESS_SCHEDULED);
		smsTask.setSakaiToolId(sakaiToolId);// TODO Populate from Sakai
		smsTask.setSenderUserName("senderUserName");// TODO Populate from Sakai
		smsTask.setDeliveryGroupName(deliverGroupId);// TODO Populate from Sakai
		smsTask.setDateToSend(dateToSend);
		smsTask.setAttemptCount(0);
		smsTask.setMessageBody(messageBody);
		Set<SmsMessage> deliverGroupMessages = generateSmsMessages(smsTask,
				sakaiUserIds);
		smsTask.setGroupSizeEstimate(smsTask.getSmsMessages().size());
		smsTask.setCreditEstimate(deliverGroupMessages.size());
		smsTask.setMaxTimeToLive(300000);
		smsTask.setDelReportTimeoutDuration(300000);
		return smsTask;
	}
}
