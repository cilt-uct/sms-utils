/***********************************************************************************
 * HelperActionBean.java
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
package org.sakaiproject.sms.beans;

import org.sakaiproject.sms.api.SmsCore;
import org.sakaiproject.sms.api.SmsService;
import org.sakaiproject.sms.hibernate.model.SmsTask;
import org.sakaiproject.sms.otp.SmsTaskLocator;

public class HelperActionBean {

	private SmsTaskLocator smsTaskLocator;
	private SmsService smsService;
	private SmsCore smsCore;

	/**
	 * Calculates estimated group size and continues to next page
	 * 
	 * @return
	 */
	public String doContinue() {
		if (smsTaskLocator.containsNew()) {

			SmsTask smsTask = (SmsTask) smsTaskLocator
					.locateBean(SmsTaskLocator.NEW_1);
			smsService.calculateEstimatedGroupSize(smsTask);

			return ActionResults.CONTINUE;
		} else {
			return ActionResults.CANCEL; // TODO: Change to error message
		}

	}

	/**
	 * Checks sufficient credits and then sends
	 * 
	 * @return
	 */
	public String send() {
		if (smsTaskLocator.containsNew()) {
			SmsTask smsTask = (SmsTask) smsTaskLocator
					.locateBean(SmsTaskLocator.NEW_1);

			// Check if credits available
			if (smsService.checkSufficientCredits(smsTask.getSakaiSiteId(),
					smsTask.getSenderUserName(), smsTask.getCreditEstimate())) {
				// do sending
				smsCore.insertTask(smsTask);
				// TODO: Give success message
			} else {
				// TODO: Give error message
			}

			smsTaskLocator.clearBeans();
			return ActionResults.SUCCESS;
		} else {
			return ActionResults.CANCEL; // TODO: Change to error message
		}

	}

	public void setSmsCore(SmsCore smsCore) {
		this.smsCore = smsCore;
	}

	public void setSmsService(SmsService smsService) {
		this.smsService = smsService;
	}

	public void setSmsTaskLocator(SmsTaskLocator smsTaskLocator) {
		this.smsTaskLocator = smsTaskLocator;
	}
}
