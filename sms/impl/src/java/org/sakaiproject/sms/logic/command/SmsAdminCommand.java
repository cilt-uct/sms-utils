/***********************************************************************************
 * SmsAdminCommand.java - created by Sakai App Builder -AZ
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

package org.sakaiproject.sms.logic.command;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.sms.logic.hibernate.HibernateLogicLocator;
import org.sakaiproject.sms.logic.incoming.SmsCommand;
import org.sakaiproject.sms.model.hibernate.SmsAccount;
import org.sakaiproject.sms.model.hibernate.constants.SmsConstants;

public class SmsAdminCommand implements SmsCommand {

	private static Log log = LogFactory.getLog(SmsAdminCommand.class);

	public HibernateLogicLocator getHibernateLogicLocator() {
		return hibernateLogicLocator;
	}

	public void setHibernateLogicLocator(
			HibernateLogicLocator hibernateLogicLocator) {
		this.hibernateLogicLocator = hibernateLogicLocator;
	}

	private HibernateLogicLocator hibernateLogicLocator = null;

	public String execute(String siteId, String userId, String mobileNr,
			String... body) {
		log.debug(getCommandKey() + " command called with parameters: ("
				+ siteId + ", " + userId + ", " + body + ")");
		if (body[0] == null || "".equals(body[0].trim())) {
			return SmsConstants.SMS_MO_EMPTY_REPLY_BODY;
		} else {
			if (body[1] == null || "".equals(body[1].trim())) {
				return SmsConstants.SMS_MO_EMPTY_REPLY_BODY;

			}

		}
		try {
			SmsAccount smsAccount = hibernateLogicLocator.getSmsAccountLogic()
					.getSmsAccount(Long.parseLong(body[1]));
			String returnValue = "AccountName=" + smsAccount.getAccountName()
					+ " Credits=" + smsAccount.getCredits() + " OverDraft= "
					+ smsAccount.getOverdraftLimit();

			return returnValue;
		} catch (NumberFormatException e) {

			return SmsConstants.SMS_MO_EMPTY_REPLY_BODY;
		}

	}

	public String[] getAliases() {
		return new String[] { "SMS" };
	}

	public String getCommandKey() {
		return "SMS";
	}

	public String getHelpMessage() {
		return "BALANCE";
	}

	public int getBodyParameterCount() {
		return 2;
	}

	public boolean isEnabled() {
		return true;
	}

}