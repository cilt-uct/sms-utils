/***********************************************************************************
 * SmsServiceImpl.java
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

import java.util.Date;
import java.util.Set;

import org.sakaiproject.sms.api.SmsCore;
import org.sakaiproject.sms.api.SmsService;
import org.sakaiproject.sms.hibernate.model.SmsTask;

/**
 * This service allows for the quick creation of smsTasks.
 * 
 * @author Etienne.Swanepoel
 * 
 */
public class SmsServiceImpl implements SmsService {

	public SmsCore smsCore = null;

	public SmsCore getSmsCore() {
		return smsCore;
	}

	public void setSmsCore(SmsCore smsCore) {
		this.smsCore = smsCore;
	}

	/**
	 * Add a new task to the sms task list, for eg. send message to all
	 * administrators at 10:00, or get latest announcements and send to mobile
	 * numbers of Sakai group x (phase II).
	 * 
	 * @param deliverGroupId
	 * @param dateToSend
	 * @param messageBody
	 * @param sakaiToolId
	 * @return
	 */
	public SmsTask insertNewTask(String deliverGroupId, Date dateToSend,
			String messageBody, String sakaiToolId) {
		return smsCore.insertNewTask(deliverGroupId, dateToSend, messageBody,
				sakaiToolId);

	}

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
	public SmsTask insertNewTask(Set<String> sakaiUserIds, Date dateToSend,
			String messageBody, String sakaiToolId) {
		return smsCore.insertNewTask(sakaiUserIds, dateToSend, messageBody,
				sakaiToolId);
	}

	/**
	 * Return true of the account has the required credits available. Take into
	 * account overdraft limits, if applicable.
	 * 
	 * @param accountID
	 * @param creditsRequired
	 */
	public boolean checkSufficientCredits(int accountID, int creditsRequired) {
		return true; // TODO: insert real code.

	}
}
