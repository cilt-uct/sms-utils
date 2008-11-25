/***********************************************************************************
 * SmsMessageValidator.java
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

import org.sakaiproject.sms.constants.SMSConstants;
import org.sakaiproject.sms.hibernate.model.SmsMessage;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * The Class SmsMessageValidator.
 */
public class SmsMessageValidator implements Validator {

	/**
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	public boolean supports(Class clazz) {
		if (SmsMessage.class.equals(clazz.getClass())) {
			return true;
		}
		return false;
	}

	/**
	 * Only basic validation at this stage
	 * 
	 * @see org.springframework.validation.Validator#validate(java.lang.Object,
	 *      org.springframework.validation.Errors)
	 */
	public void validate(Object obj, Errors err) {
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "mobileNumber",
				"sms.errors.mobileNumber.empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "messageBody",
				"sms.errors.messageBody.empty");

		SmsMessage msg = (SmsMessage) obj;

		if (msg.getMessageBody() != null && !"".equals(msg.getMessageBody())) {
			// Check length of messageBody
			if (msg.getMessageBody().length() > SMSConstants.MAX_SMS_LENGTH) {
				err
						.rejectValue("messageBody",
								"sms.errors.messageBody.tooLong");
			}
		}

		if (msg.getMobileNumber() != null && !"".equals(msg.getMobileNumber())) {
			String trimmedNumber = msg.getMobileNumber().trim();
			// Check length of mobile number
			if (trimmedNumber.length() > SMSConstants.MAX_MOBILE_NR_LENGTH) {
				err.rejectValue("mobileNumber",
						"sms.errors.mobileNumber.tooLong");
			}

			// Can start with + or not and can only contain digits
			if (!trimmedNumber.matches("[+]?[0-9 ]+")) {
				err.rejectValue("mobileNumber",
						"sms.errors.mobileNumber.invalid");
			}
		}

	}
}
