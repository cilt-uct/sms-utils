package org.sakaiproject.sms.test;

import org.sakaiproject.sms.hibernate.model.SmsConfig;
import org.sakaiproject.sms.validators.SmsConfigValidator;
import org.springframework.validation.BindException;

import junit.framework.TestCase;

public class SMSConfigValidatorTest extends TestCase{
	
	private static final String INVALID_EMAILS = "jo@gmail.com,mark@gmailcom";
	private static final String VALID_EMAILS = "jo@gmail.com,mark@gmail.com";
	private static final String VALID_EMAIL = "jo@gmail.com";
	private SmsConfigValidator validator;
	private BindException bindException;
	private SmsConfig smsConfig;
	
	@Override
	protected void setUp() throws Exception {
		validator = new SmsConfigValidator();
		smsConfig = new SmsConfig();
		bindException = new BindException(smsConfig, "SmsConfig");
		
		super.setUp();
	}

	public void testCorrectEmail() throws Exception {
		smsConfig.setNotificationEmail(VALID_EMAIL);
		smsConfig.setSmsEnabled(true);
		validator.validate(smsConfig, bindException);
		assertFalse(bindException.hasErrors());
	}
	
	public void testCorrectEmails() throws Exception {
		smsConfig.setNotificationEmail(VALID_EMAILS);
		smsConfig.setSmsEnabled(true);
		validator.validate(smsConfig, bindException);
		assertFalse(bindException.hasErrors());
	}
	
	public void testNotifiationDisabled() throws Exception {
		smsConfig.setSmsEnabled(false);
		validator.validate(smsConfig, bindException);
		assertFalse(bindException.hasErrors());
	}
	
	public void testInvalidEmails() throws Exception {
		smsConfig.setNotificationEmail(INVALID_EMAILS);
		smsConfig.setSmsEnabled(true);
		validator.validate(smsConfig, bindException);
		assertTrue(bindException.hasErrors());
	}
}