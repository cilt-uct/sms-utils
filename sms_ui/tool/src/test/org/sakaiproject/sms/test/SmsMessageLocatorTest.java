package org.sakaiproject.sms.test;

import junit.framework.TestCase;

import org.sakaiproject.sms.hibernate.model.SmsMessage;
import org.sakaiproject.sms.otp.SmsMessageLocator;


public class SmsMessageLocatorTest extends TestCase {

	private SmsMessageLocator smsMsgLocator;

	@Override
	public void setUp() {
		smsMsgLocator = new SmsMessageLocator();
	}
	
	
	public void testLocateNewSmsConfig(){
		Object obj = smsMsgLocator.locateBean(SmsMessageLocator.NEW_1);
		assertNotNull(obj); // Should retrieve something
		try {
			SmsMessage msg = (SmsMessage) obj;
			assertNull(msg.getId()); // Should not have id yet as it is not
			// persisted
		} catch (Exception e) {
			fail("No exception should be caught"); // In case of possible
			// ClassCastException
		}
	}
}
