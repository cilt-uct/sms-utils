package org.sakaiproject.sms.test;

import junit.framework.TestCase;

import org.sakaiproject.sms.hibernate.model.SmsConfig;
import org.sakaiproject.sms.validators.SmsSystemConfigValidator;
import org.springframework.validation.BindException;

public class SmsSystemConfigValidatorTest extends TestCase{

	private SmsSystemConfigValidator validator;
	private BindException bindException;
	private SmsConfig smsConfig;

	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		smsConfig = new SmsConfig();
		validator = new SmsSystemConfigValidator();
		bindException = new BindException(smsConfig, "SmsConfig");
	}

	public void testInvalidConfigSettings() throws Exception {
		smsConfig.setDelReportTimeoutDuration(null);
		smsConfig.setSchedulerInterval(null);
		validator.validate(smsConfig, bindException);
		assertTrue(bindException.hasErrors());
	}
	
	public void testValidConfigSettings() throws Exception {
		smsConfig.setDelReportTimeoutDuration(new Integer(100));
		smsConfig.setSchedulerInterval(new Integer(100));
		smsConfig.setCreditCost(100.12f);
		validator.validate(smsConfig, bindException);
		assertFalse(bindException.hasErrors());
	}
	
	public void testInvalidCreditCost() throws Exception {
		smsConfig.setDelReportTimeoutDuration(new Integer(100));
		smsConfig.setSchedulerInterval(new Integer(100));
		smsConfig.setSchedulerInterval(null);
		validator.validate(smsConfig, bindException);
		assertTrue(bindException.hasErrors());
	}
}
