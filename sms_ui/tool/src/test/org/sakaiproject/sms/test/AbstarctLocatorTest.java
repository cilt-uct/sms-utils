package org.sakaiproject.sms.test;

import org.sakaiproject.sms.hibernate.model.SmsMessage;
import org.sakaiproject.sms.otp.AbstractLocator;
import org.sakaiproject.sms.otp.SmsMessageLocator;

import junit.framework.TestCase;

public abstract class AbstarctLocatorTest extends TestCase{

	/**
	 * Test locate new {@link SmsMessage} object.
	 */
	public void locateNewBean(AbstractLocator locator) {
		Object obj = locator.locateBean(SmsMessageLocator.NEW_1);
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
