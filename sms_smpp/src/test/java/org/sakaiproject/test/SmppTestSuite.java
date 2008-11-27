package org.sakaiproject.test;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Run all tests.
 */
public class SmppTestSuite {

	/**
	 * Suite.
	 * 
	 * @return the test
	 */
	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for org.sakaiproject.sms.smpp.test");
		// $JUnit-BEGIN$
		suite.addTestSuite(SmppAPITest.class);
		suite.addTestSuite(SmppThreadingTest.class);
		suite.addTestSuite(SmsSmppImplTest.class);
		// $JUnit-END$
		return suite;
	}

}
