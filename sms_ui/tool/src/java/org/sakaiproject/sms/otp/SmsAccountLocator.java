/***********************************************************************************
 * SmsAccountLocator.java
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
package org.sakaiproject.sms.otp;

import java.util.HashMap;
import java.util.Map;

import org.sakaiproject.sms.beans.ActionResults;
import org.sakaiproject.sms.hibernate.logic.SmsAccountLogic;
import org.sakaiproject.sms.hibernate.logic.impl.exception.DuplicateUniqueFieldException;
import org.sakaiproject.sms.hibernate.model.SmsAccount;
import org.sakaiproject.sms.hibernate.model.constants.SmsHibernateConstants;

import uk.org.ponder.beanutil.BeanLocator;
import uk.org.ponder.messageutil.TargettedMessage;
import uk.org.ponder.messageutil.TargettedMessageList;

public class SmsAccountLocator implements BeanLocator {

	/** The Constant LOCATOR_NAME. */
	public static final String LOCATOR_NAME = "SmsAccountLocator";

	public static final String NEW_PREFIX = "new ";

	/** The Constant NEW_1. */
	public static final String NEW_1 = NEW_PREFIX + "1";

	/** The delivered map (used to store beans). */
	private final Map<String, SmsAccount> delivered = new HashMap<String, SmsAccount>();

	private SmsAccountLogic smsAccountLogic;

	private TargettedMessageList messages;

	public Object locateBean(String name) {
		SmsAccount togo = delivered.get(name);
		if (togo == null) {
			if (name.startsWith(NEW_PREFIX)) {
				togo = new SmsAccount();
				togo.setBalance(SmsHibernateConstants.INITIAL_BALANCE);
				togo
						.setSakaiSiteId(SmsHibernateConstants.SMS_DEV_DEFAULT_SAKAI_SITE_ID);
			} else {
				togo = smsAccountLogic.getSmsAccount(Long.parseLong(name));
			}
			delivered.put(name, togo);
		}
		return togo;
	}

	public String saveAll() {
		for (SmsAccount account : delivered.values()) {
			try {
				smsAccountLogic.persistSmsAccount(account);
			} catch (DuplicateUniqueFieldException e) {
				messages.addMessage(new TargettedMessage("sms.errors."
						+ e.getField() + ".duplicate", null,
						TargettedMessage.SEVERITY_ERROR));
				return ActionResults.ERROR;
			}

			messages.addMessage(new TargettedMessage("sms.sms-account.saved",
					null, TargettedMessage.SEVERITY_INFO));
		}
		return ActionResults.SUCCESS;
	}

	public void setMessages(TargettedMessageList messages) {
		this.messages = messages;
	}

	public void setSmsAccountLogic(SmsAccountLogic smsAccountLogic) {
		this.smsAccountLogic = smsAccountLogic;
	}

}
