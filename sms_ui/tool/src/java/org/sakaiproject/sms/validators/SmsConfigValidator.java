/***********************************************************************************
 * SmsConfigValidator.java
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

package org.sakaiproject.sms.validators;

import java.util.StringTokenizer;

import org.sakaiproject.sms.hibernate.model.SmsConfig;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class SmsConfigValidator implements Validator {

	@SuppressWarnings("unchecked")
	public boolean supports(Class clazz) {
		if (SmsConfig.class.equals(clazz.getClass())) {
			return true;
		}
		return false;
	}

	public void validate(Object obj, Errors err) {
		SmsConfig smsConfig = (SmsConfig) obj;

		if (smsConfig.getSakaiSiteId() == null
				|| smsConfig.getSakaiSiteId().equals("")) {

			
			if (smsConfig.getSmsRetryMaxCount() == null)
				err
						.rejectValue("smsRetryMaxCount",
								"sms.errors.maxretry.empty");
			if (smsConfig.getSmsTaskMaxLifeTime() == null)
				err.rejectValue("smsTaskMaxLifeTime",
						"sms.errors.tasklifetime.empty");
			if (smsConfig.getSmsRetryScheduleInterval() == null)
				err.rejectValue("smsRetryScheduleInterval",
						"sms.errors.retryschedule.empty");

		}

		if (smsConfig.getPagingSize() == null)
			err.rejectValue("pagingSize", "sms.errors.paging.empty");

		if (smsConfig.isSendSmsEnabled()) {

			ValidationUtils.rejectIfEmptyOrWhitespace(err, "notificationEmail",
					"sms.errors.email.empty");

			if (smsConfig.getNotificationEmail() != null) {
				StringTokenizer stringTokenizer = new StringTokenizer(smsConfig
						.getNotificationEmail(), ",");

				while (stringTokenizer.hasMoreElements()) {
					String address = stringTokenizer.nextToken();

					if (address.indexOf('@') == -1
							|| address.indexOf('.') == -1) {
						err.rejectValue("notificationEmail",
								"sms.errors.email.invalid");
					}
				}
			}
		}
	}
}
