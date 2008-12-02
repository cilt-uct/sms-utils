package org.sakaiproject.sms.hibernate.test;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Run all tests.
 */
public class SmsTestSuite {

	private static void createTestSchema() {
		/**
		 * Configuration cfg = ....; new SchemaExport(cfg).create(false, true);
		 */
	}

	/**
	 * Suite.
	 * 
	 * @return the test
	 */
	public static Test suite() {

		createTestSchema();

		TestSuite suite = new TestSuite(
				"Test for org.sakaiproject.sms.hibernate.test");
		// $JUnit-BEGIN$
		suite.addTestSuite(SmsTransactionTest.class);
		suite.addTestSuite(SmsTaskTest.class);
		suite.addTestSuite(SmsConfigTest.class);
		suite.addTestSuite(SmsMessageTest.class);
		suite.addTestSuite(SmsAccountTest.class);
		suite.addTestSuite(SmsDatabaseStressTest.class);
		// $JUnit-END$
		return suite;
	}

}
