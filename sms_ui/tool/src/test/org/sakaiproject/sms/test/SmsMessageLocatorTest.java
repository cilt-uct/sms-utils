package org.sakaiproject.sms.test;

import org.sakaiproject.sms.otp.SmsMessageLocator;


public class SmsMessageLocatorTest extends AbstarctLocatorTest {

	private SmsMessageLocator smsMsgLocator;

	@Override
	public void setUp() {
		smsMsgLocator = new SmsMessageLocator();
	}
	
	
	public void testLocateNewSmsConfig(){
		locateNewBean(smsMsgLocator);
	}
}
