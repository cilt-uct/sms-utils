package org.sakaiproject.sms.validators;

import org.sakaiproject.sms.hibernate.model.SmsMessage;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

// TODO: Auto-generated Javadoc
/**
 * The Class SmsMessageValidator.
 */
public class SmsMessageValidator implements Validator {
	public boolean supports(Class clazz) {
		if (clazz.getClass().equals(SmsMessage.class)) {
			return true;
		}
		return false;
	}

	public void validate(Object arg0, Errors arg1) {
		// SmsMessage msg = (SmsMessage) arg0;
		// arg1.reject("sms.error.test");

		// ValidationUtils.rejectIfEmpty(e, "mobileNumber",
		// "sms.errors.mobileNumber.empty");
		// ValidationUtils.rejectIfEmpty(e, "messageBody",
		// "sms.errors.messageBody.empty");
		// SmsMessage msg = (SmsMessage) obj;
		// if (msg.getMessageBody().length() > SMSConstants.MAX_SMS_LENGTH) {
		// e.rejectValue("messageBody", "sms.errors.messageBody.tooLong");
		// }
		// if (msg.getMobileNumber().length() > 0) {
		// e.rejectValue("mobileNumber", "sms.errors.mobileNumber.tooLong");
		// }

	}
}
