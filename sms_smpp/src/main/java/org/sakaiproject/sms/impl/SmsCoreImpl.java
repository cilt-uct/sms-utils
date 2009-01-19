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
import org.sakaiproject.sms.hibernate.model.SmsAccount;
import org.sakaiproject.sms.hibernate.model.SmsConfig;
import org.sakaiproject.sms.hibernate.model.SmsMessage;
import org.sakaiproject.sms.hibernate.model.SmsTask;
import org.sakaiproject.sms.hibernate.model.constants.SmsConst_DeliveryStatus;
import org.sakaiproject.sms.hibernate.model.constants.SmsHibernateConstants;
import org.sakaiproject.sms.hibernate.util.DateUtil;
import org.sakaiproject.sms.smpp.util.MessageCatelog;

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

	public SmsTask calculateEstimatedGroupSize(SmsTask smsTask) {
		Set<SmsMessage> deliverGroupMessages = generateSmsMessages(smsTask);
		int groupSize = deliverGroupMessages.size();
		smsTask.setGroupSizeEstimate(groupSize);
		smsTask.setCreditEstimate(groupSize);
		smsTask.setCostEstimate(smsBilling.convertCreditsToAmount(groupSize)
				.doubleValue());// TODO Change to Float?
		return smsTask;
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
	 * For now we just generate the list. Will get it from Sakai later on. So we
	 * generate a random number of users with random mobile numbers.
	 * 
	 * @param smsTask
	 * @return
	 */
	private Set<SmsMessage> generateDummySmsMessages(SmsTask smsTask) {
		Set<SmsMessage> messages = new HashSet<SmsMessage>();

		String[] users;
		int numberOfMessages = (int) Math.round(Math.random() * 100);

		users = new String[100];

		String[] celnumbers = new String[100];
		for (int i = 0; i < users.length; i++) {

			users[i] = "SakaiUser" + i;

			celnumbers[i] = "+2773"
					+ (int) Math.round(Math.random() * 10000000);
		}
		for (int i = 0; i < numberOfMessages; i++) {

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
	 * Get the group list from Sakai
	 */
	// TODO Why pass three args when they can all come from the SmsTask?? Only 1
	// arg needed.. smsTask
	public Set<SmsMessage> generateSmsMessages(SmsTask smsTask) {
		return generateDummySmsMessages(smsTask);
		// TODO must make a Sakai call here
	}

	public SmsTask getNextSmsTask() {
		return HibernateLogicFactory.getTaskLogic().getNextSmsTask();

	}

	public SmsTask getPreliminaryTask(Set<String> sakaiUserIds,
			Date dateToSend, String messageBody, String sakaiSiteID,
			String sakaiToolId, String sakaiSenderID) {

		return getPreliminaryTask(null, null, sakaiUserIds, dateToSend,
				messageBody, sakaiSiteID, sakaiToolId, sakaiSenderID);
	}

	public SmsTask getPreliminaryTask(String deliverGroupId, Date dateToSend,
			String messageBody, String sakaiSiteID, String sakaiToolId,
			String sakaiSenderID) {
		return getPreliminaryTask(deliverGroupId, null, null, dateToSend,
				messageBody, sakaiSiteID, sakaiToolId, sakaiSenderID);
	}

	private SmsTask getPreliminaryTask(String deliverGroupId,
			Set<String> mobileNumbers, Set<String> sakaiUserIds,
			Date dateToSend, String messageBody, String sakaiSiteID,
			String sakaiToolId, String sakaiSenderID) {
		SmsConfig config = HibernateLogicFactory.getConfigLogic()
				.getOrCreateSystemSmsConfig();
		SmsTask smsTask = new SmsTask();
		smsTask.setSmsAccountId(smsBilling.getAccountID(sakaiSiteID,
				sakaiSenderID, 1));
		smsTask.setStatusCode(SmsConst_DeliveryStatus.STATUS_PENDING);
		smsTask.setSakaiSiteId(sakaiSiteID);
		smsTask
				.setMessageTypeId(SmsHibernateConstants.SMS_TASK_TYPE_PROCESS_SCHEDULED);
		smsTask.setSakaiToolId(sakaiToolId);
		smsTask.setSenderUserName(sakaiSenderID);
		smsTask.setDeliveryGroupName(deliverGroupId);
		smsTask.setDeliveryGroupId(deliverGroupId);
		smsTask.setDateCreated(new Date());
		smsTask.setDateToSend(dateToSend);
		smsTask.setAttemptCount(0);
		smsTask.setMessageBody(messageBody);
		smsTask.setMaxTimeToLive(config.getSmsTaskMaxLifeTime());
		smsTask.setDelReportTimeoutDuration(config
				.getDelReportTimeoutDuration());
		return smsTask;
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

	public SmsBilling getSmsBilling() {
		return smsBilling;
	}

	public SmsSmpp getSmsSmpp() {
		return smsSmpp;
	}

	public void init() {

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
		HibernateLogicFactory.getTaskLogic().persistSmsTask(smsTask);
		smsBilling.reserveCredits(smsBilling.getAccountID(smsTask
				.getSakaiSiteId(), smsTask.getSenderUserName(), smsTask
				.getSmsAccountId()), smsTask.getCreditEstimateInt());

		tryProcessTaskRealTime(smsTask);
		return smsTask;
	}

	// TODO Why does this not process all the messages for a group
	public void processIncomingMessage(SmsMessage smsMessage) {
		// TODO For phase 2
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

	public void processTask(SmsTask smsTask) {
		SmsConfig systemConfig = HibernateLogicFactory.getConfigLogic()
				.getOrCreateSystemSmsConfig();
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
			sendTaskNotification(smsTask,
					SmsHibernateConstants.TASK_NOTIFICATION_FAILED);
			HibernateLogicFactory.getTaskLogic().persistSmsTask(smsTask);
			return;
		}

		smsTask.setStatusCode(SmsConst_DeliveryStatus.STATUS_BUSY);
		if (smsTask.getAttemptCount() < systemConfig.getSmsRetryMaxCount()) {
			if (smsTask.getAttemptCount() <= 1) {

				// TODO: we need to generate messages based on a list of userIDs
				// or mobileNumbers
				smsTask.setSmsMessagesOnTask(this.generateSmsMessages(smsTask));
				LOG.info("Total messages on task:="
						+ smsTask.getSmsMessages().size());
				smsTask.setGroupSizeActual(smsTask.getSmsMessages().size());
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
				now.add(Calendar.MINUTE, +(systemConfig
						.getSmsRetryScheduleInterval()));
				smsTask.rescheduleDateToSend(new Date(now.getTimeInMillis()));
			}

		} else {
			smsTask.setStatusCode(SmsConst_DeliveryStatus.STATUS_FAIL);
			smsTask.setStatusForMessages(
					SmsConst_DeliveryStatus.STATUS_PENDING,
					SmsConst_DeliveryStatus.STATUS_FAIL);
			sendTaskNotification(smsTask,
					SmsHibernateConstants.TASK_NOTIFICATION_FAILED);
		}
		HibernateLogicFactory.getTaskLogic().persistSmsTask(smsTask);
	}

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

	public void processVeryLateDeliveryReports() {
		// TODO Auto-generated method stub

	}

	public boolean sendNotificationEmail(String toAddress, String subject,
			String body) {
		// TODO Call sakai service to send the email
		return true;
	}

	public void setSmsBilling(SmsBilling smsBilling) {
		this.smsBilling = smsBilling;
	}

	public void setSmsSmpp(SmsSmpp smsSmpp) {
		this.smsSmpp = smsSmpp;
	}

	public void tryProcessTaskRealTime(SmsTask smsTask) {

		// TODO also check number of process threads
		if (smsTask.getDateToSend().getTime() <= System.currentTimeMillis()) {
			this.processTask(smsTask);
		}
	}

	/**
	 * Send a email notification out.
	 * 
	 * @param smsTask
	 *            the sms task
	 * @param taskMessageType
	 *            the task message type
	 * 
	 * @return true, if successful
	 */
	private boolean sendTaskNotification(SmsTask smsTask,
			Integer taskMessageType) {

		String subject = null;
		String body = null;
		String toAddress = null;

		SmsConfig config = HibernateLogicFactory.getConfigLogic()
				.getOrCreateSmsConfigBySakaiSiteId(smsTask.getSakaiSiteId());
		// Get the balance available to calculate the available credit.
		SmsAccount account = HibernateLogicFactory.getAccountLogic()
				.getSmsAccount(smsTask.getSmsAccountId().longValue());
		Float amount = account.getBalance();

		if (!account.getAccountEnabled()) {
			amount = 0.0f;
		} else if (account.getOverdraftLimit() != null) {
			// Add the overdraft to the available balance
			amount += account.getOverdraftLimit();
		}

		String creditsAvailable = smsBilling.convertAmountToCredits(amount)
				+ ""; // GET THIS
		String creditsRequired = smsTask.getCreditEstimate() + "";

		if (taskMessageType
				.equals(SmsHibernateConstants.TASK_NOTIFICATION_STARTED)) {
			subject = MessageCatelog.getMessage(
					"messages.notificationSubjectStarted", smsTask.getId()
							.toString());
			body = MessageCatelog.getMessage(
					"messages.notificationBodyStarted", creditsRequired,
					creditsAvailable);
			toAddress = config.getNotificationEmail();

		} else if (taskMessageType
				.equals(SmsHibernateConstants.TASK_NOTIFICATION_SENT)) {
			subject = MessageCatelog.getMessage(
					"messages.notificationSubjectSent", smsTask.getId()
							.toString());
			body = MessageCatelog.getMessage("messages.notificationBodySent",
					creditsRequired, creditsAvailable);
			toAddress = config.getNotificationEmailSent();

		} else if (taskMessageType
				.equals(SmsHibernateConstants.TASK_NOTIFICATION_FAILED)) {
			subject = MessageCatelog.getMessage(
					"messages.notificationSubjectFailed", smsTask.getId()
							.toString());
			body = MessageCatelog.getMessage("messages.notificationBodyFailed",
					creditsRequired, creditsAvailable);
			toAddress = config.getNotificationEmail();
		}

		return sendNotificationEmail(toAddress, subject, body);

	}
}
