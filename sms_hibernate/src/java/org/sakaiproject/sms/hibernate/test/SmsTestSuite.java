package org.sakaiproject.sms.hibernate.test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class SmsTestSuite {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for org.sakaiproject.sms.hibernate.test");
		// $JUnit-BEGIN$
		suite.addTestSuite(SmsTransactionTest.class);
		suite.addTestSuite(SmsTaskTest.class);
		suite.addTestSuite(SmsConfigTest.class);
		suite.addTestSuite(SmsMessageTest.class);
		suite.addTestSuite(SmsAccountTest.class);
		// $JUnit-END$
		return suite;
	}

}
