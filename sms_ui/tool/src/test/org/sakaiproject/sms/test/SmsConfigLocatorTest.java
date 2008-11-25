package org.sakaiproject.sms.test;

import org.sakaiproject.sms.hibernate.model.SmsMessage;
import org.sakaiproject.sms.otp.SmsConfigLocator;

public class SmsConfigLocatorTest extends AbstarctLocatorTest{

	SmsConfigLocator smsConfigLocator;

	@Override
	public void setUp() {
		smsConfigLocator = new SmsConfigLocator();
	}

	/**
	 * TODO: Complete retrieval of existing {@link SmsMessage} object.
	 * 
	 */
	public void testLocateExistingSmsMsg() {
		// Not possible at the moment
	}
	
	public void testLocateNewSmsMsg(){
		locateNewBean(smsConfigLocator);
	}
}
