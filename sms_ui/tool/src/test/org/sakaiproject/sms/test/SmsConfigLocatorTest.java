package org.sakaiproject.sms.test;

import junit.framework.TestCase;

import org.sakaiproject.sms.hibernate.model.SmsMessage;
import org.sakaiproject.sms.otp.SmsConfigLocator;
import org.sakaiproject.sms.otp.SmsMessageLocator;

public class SmsConfigLocatorTest extends TestCase{

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
		Object obj = smsConfigLocator.locateBean(SmsMessageLocator.NEW_1);
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
