/***********************************************************************************
 * SmsService.java
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
import java.util.Set;

import org.sakaiproject.sms.hibernate.model.SmsTask;

/**
 * This API allows for easy implementation of SMS services in an existing or new
 * Sakai tool.
 * 
 * @author etienne@psybergate.co.za
 * 
 */
public interface SmsService {

	/**
	 * Add a new task to the sms task list, for eg. send message y to Sakai
	 * group X ant time Z. If the task is future dated, then it be picked up by
	 * the sms task (job) scheduler for processing.
	 * 
	 * @param sakaiGroupId
	 * @param dateToSend
	 * @param messageBody
	 * @param sakaiToolId
	 * @return
	 */
	public SmsTask getPreliminaryTask(String sakaiGroupId, Date dateToSend,
			String messageBody, String sakaiToolId);

	/**
	 * Add a new task to the sms task list. In this case you must supply a list
	 * of Sakai user ID's.
	 * 
	 * @param sakaiUserIds
	 * @param dateToSend
	 * @param messageBody
	 * @param sakaiToolId
	 * @return
	 */
	public SmsTask getPreliminaryTask(Set<String> sakaiUserIds,
			Date dateToSend, String messageBody, String sakaiToolId);

	/**
	 * Return true of the account has the required credits available to send the
	 * messages. The account number is calculated using either the Sakai site or
	 * the Sakai user. If this returns false, then the UI must not allow the
	 * user to proceed. If not handled by the UI, then the sms service will fail
	 * the the sending of the message anyway.
	 * 
	 * @param sakaiSiteID
	 *            , (e.g. "!admin")
	 * @param sakaiUserID
	 */
	public boolean checkSufficientCredits(String sakaiSiteID,
			String sakaiUserID, int creditsRequired);
}
