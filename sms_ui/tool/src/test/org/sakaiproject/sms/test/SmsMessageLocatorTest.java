package org.sakaiproject.sms.test;

import junit.framework.TestCase;

import org.sakaiproject.sms.hibernate.model.SmsMessage;
import org.sakaiproject.sms.otp.SmsMessageLocator;

/**
 * The Class SmsMessageLocatorTest. Tests the Bean locator for
 * {@link SmsMessage}: {@link SmsMessageLocator}
 * 
 */
public class SmsMessageLocatorTest extends TestCase {

	private SmsMessageLocator smsMsgLocator;

	/**
	 * setUp to run at start of every test
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	public void setUp() {
		smsMsgLocator = new SmsMessageLocator();
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
