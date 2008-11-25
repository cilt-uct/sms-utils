package org.sakaiproject.sms.test;

import org.sakaiproject.sms.hibernate.model.SmsConfig;
import org.sakaiproject.sms.hibernate.model.SmsMessage;
import org.sakaiproject.sms.otp.SmsConfigLocator;
import org.sakaiproject.sms.otp.SmsMessageLocator;

import junit.framework.TestCase;

public class SmsConfigLocatorTest extends TestCase{

	private SmsConfigLocator smsConfigLocator;

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

	/**
	 * Test locate new {@link SmsMessage} object.
	 */
	public void testLocateNewSmsMsg() {
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
