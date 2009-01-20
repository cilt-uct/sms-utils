/***********************************************************************************
 * SmsCore.java
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

package org.sakaiproject.sms.api;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.sakaiproject.sms.hibernate.model.SmsMessage;
import org.sakaiproject.sms.hibernate.model.SmsTask;

/**
 * The SMS service will handle all logic regarding the queueing, sending and
 * receiving of messages.
 * 
 * @author louis@psybergate.com
 * @version 1.0
 * @created 12-Nov-2008
 */
public interface SmsCore {	
	/**
	 * Get the group list from Sakai and remove users with invalid/empty mobile
	 * numbers or opted out profiles.
	 * 
	 * @param smsTask
	 * @return
	 */
	public Set generateSmsMessages(SmsTask smsTask);

	/**
	 * Find the next sms task to process from the task queue. Determine tasks
	 * with highest priority. Priority is based on message age and type.
	 */
	public SmsTask getNextSmsTask();

	/**
	 * Get Sakai user's mobile number from profile. Return the mobile number,
	 * null if not found.
	 * 
	 * @param sakaiUserID
	 */
	public String getSakaiMobileNumber(String sakaiUserID);

	/**
	 * Add a new task to the sms task list, for eg. send message to all
	 * administrators at 10:00, or get latest announcements and send to mobile
	 * numbers of Sakai group x (phase II). Validation will be done to make sure
	 * that the preliminary values are supplied.
	 * 
	 * @param smsTask
	 * @return
	 */
	public SmsTask insertTask(SmsTask smsTask);

	
	/**
	 * Add a new task to the sms task list, that will send sms messages to the specified list of mobile numbers
	 * 
	 * @param dateToSend
	 * @param messageBody
	 * @param sakaiSiteID
	 * @param sakaiToolId
	 * @param sakaiSenderID
	 * @param deliveryMobileNumbers
	 * @return
	 */
	public SmsTask getPreliminaryTask(Date dateToSend, String messageBody, String sakaiSiteID,
			String sakaiToolId, String sakaiSenderID, Set<String> deliveryMobileNumbers);
	
	/**
	 * Add a new task to the sms task list, for eg. send message to all
	 * administrators at 10:00, or get latest announcements and send to mobile
	 * numbers of Sakai group x (phase II).
	 * 
	 * @param sakaiUserIds
	 * @param dateToSend
	 * @param messageBody
	 * @param sakaiToolId
	 * @return
	 */
	public SmsTask getPreliminaryTask(Set<String> sakaiUserIds,
			Date dateToSend, String messageBody, String sakaiSiteID,
			String sakaiToolId, String sakaiSenderID);

	/**
	 * Some delivery report might arive after the predefined timeout period. Wee
	 * still need to handle them because these messages are now billable. We
	 * need to receive them, update SMS_MESSAGE and make a account entry.
	 */
	public void processVeryLateDeliveryReports();

	/**
	 * Try to process an incoming message in real-time by inserting it into the
	 * queue and calling processMessage immediately. If unable to process, then
	 * leave in the queue for the job scheduler to handle. Incoming messages are
	 * for later development in phase II.
	 * 
	 * @param messageID
	 */
	public void processIncomingMessage(SmsMessage smsMessage);

	/**
	 * If we did not receive gateway delivery reports for messages that was sent
	 * out, then we mark those messages as time out after a predefined period as
	 * determined by DEL_REPORT_TIMEOUT_DURATION on the task. These messages are
	 * not billable.
	 */
	public void processTimedOutDeliveryReports();

	/**
	 * Gets the next task to process. Based on specific criteria like status en
	 * date to sent.
	 */
	public void processNextTask();

	/**
	 * Process is specific task. A task can be retried if a previous send
	 * attempt was unsuccessful due to gateway connection problems. A retry will
	 * be re-scheduled some time in the future. When the max retry attempts are
	 * reached or if credits are insufficient, the task is marked as failed.
	 * 
	 * The task will also expire if it cannot be processed in a specified time.
	 * See http://jira.sakaiproject.org/jira/browse/SMS-9
	 */
	public void processTask(SmsTask smsTask);

	/**
	 * If a new task is scheduled for immediate processing, then we try to
	 * process it in real-time. If it is not possible (too many threads) then
	 * the task will be handled by the scheduler. If the scheduler is too busy
	 * and the task is picked up too late, then the task is marked as
	 * SmsConst_DeliveryStatus.STATUS_EXPIRE
	 * 
	 * @param smsTask
	 */
	public void tryProcessTaskRealTime(SmsTask smsTask);

	/**
	 * We calculate the group size and cost of the task without persisting any
	 * data to the database. The returned task can then be passed on to
	 * insertNewTask.
	 * 
	 */
	public SmsTask getPreliminaryTask(String deliverGroupId, Date dateToSend,
			String messageBody, String sakaiSiteID, String sakaiToolId,
			String sakaiSenderID);

	/**
	 * Calculate the number of messages to be sent when the new sms task is
	 * created. Also populate other estimated values on the task.
	 * 
	 * @param smsTask
	 * @return
	 */
	public SmsTask calculateEstimatedGroupSize(SmsTask smsTask);

	/**
	 * Send a email
	 * 
	 * @param toAddress
	 * @param subject
	 * @param body
	 * @return
	 */
	public boolean sendNotificationEmail(String toAddress, String subject,
			String body);
}