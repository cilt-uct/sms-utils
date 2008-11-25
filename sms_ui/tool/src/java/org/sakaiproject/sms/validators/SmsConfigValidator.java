package org.sakaiproject.sms.validators;

import java.util.StringTokenizer;

import org.sakaiproject.sms.hibernate.model.SmsConfig;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class SmsConfigValidator implements Validator{
	
	@SuppressWarnings("unchecked")
	public boolean supports(Class clazz) {
		if (SmsConfig.class.equals(clazz.getClass())) {
			return true;
		}
		return false;
	}
	
	public void validate(Object obj, Errors err) {
		SmsConfig smsConfig = (SmsConfig) obj;


		if(smsConfig.getSmsEnabled()){
			
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "notificationEmail",
			"sms.errors.email.empty");
			
			StringTokenizer stringTokenizer = new StringTokenizer(smsConfig.getNotificationEmail(), ",");
			
			while (stringTokenizer.hasMoreElements()) {
				String address =  stringTokenizer.nextToken();
			
				if(address.indexOf('@') == -1 || address.indexOf('.') == -1){
					err.rejectValue("notificationEmail",
							"sms.errors.email.invalid");
				}
			}
		}
	}
	

}
