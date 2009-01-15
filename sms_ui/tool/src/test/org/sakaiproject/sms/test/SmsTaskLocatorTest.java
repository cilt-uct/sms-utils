package org.sakaiproject.sms.test;

import junit.framework.TestCase;

import org.sakaiproject.sms.api.SmsCore;
import org.sakaiproject.sms.hibernate.model.SmsTask;
import org.sakaiproject.sms.otp.SmsTaskLocator;
import org.sakaiproject.sms.test.stubs.SmsCoreStub;

public class SmsTaskLocatorTest extends TestCase {

	private SmsTaskLocator smsTaskLocator;
	private SmsCore smsCore;

	/**
	 * setUp to run at start of every test
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	public void setUp() {
		smsTaskLocator = new SmsTaskLocator();
		smsCore = new SmsCoreStub();

		smsTaskLocator.setSmsCore(smsCore);
	}

	/**
	 * Test clearing of beans
	 */
	public void testClearBeans() {
		SmsTask smsTask = (SmsTask) smsTaskLocator
				.locateBean(SmsTaskLocator.NEW_1);
		assertTrue(smsTaskLocator.containsNew());
		smsTaskLocator.clearBeans();
		assertFalse(smsTaskLocator.containsNew());
	}

	/**
	 * Test containsNew method
	 */
	public void testContainsNew() {
		assertFalse(smsTaskLocator.containsNew());
		SmsTask smsTask = (SmsTask) smsTaskLocator
				.locateBean(SmsTaskLocator.NEW_1);
		assertTrue(smsTaskLocator.containsNew());
	}

	/**
	 * Test locateBean method for new bean
	 */
	public void testLocateNew() {
		assertFalse(smsTaskLocator.containsNew());
		SmsTask smsTask = (SmsTask) smsTaskLocator
				.locateBean(SmsTaskLocator.NEW_1);
		assertNotNull(smsTask);
	}
}
