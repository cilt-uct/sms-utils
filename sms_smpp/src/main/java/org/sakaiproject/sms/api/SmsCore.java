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
	 * @param sakaiSiteID
	 * @param sakaiGroupID
	 * @param smsTask
	 */
	public Set getDeliveryGroup(String sakaiSiteID, String sakaiGroupID,
			SmsTask smsTask);

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
	 * Make entry into log file.
	 */
	public void insertIntoDebugLog();

	/**
	 * Add a new task to the sms task list, for eg. send message to all
	 * administrators at 10:00, or get latest announcements and send to mobile
	 * numbers of Sakai group x (phase II).
	 */
	public void insertNewTask(SmsTask smsTask);

	/**
	 * Try to process an incoming message in real-time by inserting it into the
	 * queue and calling processMessage immediately. If unable to process, then
	 * leave in the queue for the job scheduler to handle. Incoming messages are
	 * for later development.
	 * 
	 * @param messageID
	 */
	public void processIncomingMessage(SmsMessage smsMessage);

	/**
	 * Gets the next task to process. Based on specific criteria like status en
	 * date to sent.
	 */
	public void processNextTask();

	/**
	 * Get the list of users that must receive the message. Send messages to
	 * SMPP service and also add them to SMS_MESSAGE table as pending. (Statuses
	 * will be updated when the gateway delivery notifications comes in)
	 */
	public void processTask(SmsTask smsTask);

}