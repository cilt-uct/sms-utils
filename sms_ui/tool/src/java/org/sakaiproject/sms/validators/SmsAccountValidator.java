package org.sakaiproject.sms.validators;

import org.sakaiproject.sms.hibernate.model.SmsAccount;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class SmsAccountValidator implements Validator {

	public boolean supports(Class clazz) {
		if (SmsAccount.class.equals(clazz.getClass())) {
			return true;
		}
		return false;
	}

	public void validate(Object obj, Errors err) {
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "accountName",
				"sms.errors.accountName.empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "sakaiSiteId",
				"sms.errors.sakaiSiteId.empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "sakaiUserId",
				"sms.errors.sakaiUserId.empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "overdraftLimit",
				"sms.errors.overdraftLimit.empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "balance",
				"sms.errors.balance.empty");
	}

}
