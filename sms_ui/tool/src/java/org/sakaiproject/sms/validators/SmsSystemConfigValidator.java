package org.sakaiproject.sms.validators;

import org.sakaiproject.sms.hibernate.model.SmsConfig;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class SmsSystemConfigValidator implements Validator{

	@SuppressWarnings("unchecked")
	public boolean supports(Class clazz) {
		if (SmsConfig.class.equals(clazz.getClass())) {
			return true;
		}
		return false;
	}

	public void validate(Object target, Errors errors) {
				
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "delReportTimeoutDuration", "sms.errors.gateway.empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors,"schedulerInterval", "sms.errors.scheduler.interval");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors,"creditCost", "sms.errors.credit.cost");
	}

}
