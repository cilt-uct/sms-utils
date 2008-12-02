package org.sakaiproject.test;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import net.sourceforge.groboutils.junit.v1.MultiThreadedTestRunner;
import net.sourceforge.groboutils.junit.v1.TestRunnable;

/**
 * This unit test will create 2 separate but concurrent connections (sessions)
 * to the gateway simulator. Each thread will pass on messages. It will then
 * hang on and wait for sms delivery reports to come in. The test will fail if
 * delivery reports are missing. It will also fail if a message could not be
 * sent to the gateway. The number of messages to be sent can be changed for
 * each thread. The delay between message transmissions can also be set. The
 * default is 100 ms. NOTE: Each thread can receive messages send by the other
 * thread. The gateway simply sends the reports to the ip address.
 * 
 * Due to limitation in the simulator, do not set session1_message_count and
 * session1_message_count higher that 500 each. Also give the simulator 30
 * seconds or so to process messages before re-running this unit test.
 * 
 */
public class SmppThreadingTest extends TestCase {
	/**
	 * Standard main() and suite() methods.
	 * 
	 * @param args
	 *            the args
	 */
	public static void main(String[] args) {
		String[] name = { SmppThreadingTest.class.getName() };
		junit.textui.TestRunner.main(name);
	}

	/**
	 * Suite.
	 * 
	 * @return the test
	 */
	public static Test suite() {
		return new TestSuite(SmppThreadingTest.class);
	}

	private int delay_between_messages = 1; // ms

	private int session1_message_count = 0;

	private int session2_message_count = 0;

	/**
	 * You use the MultiThreadedTestRunner in your test cases. The MTTR takes an
	 * array of TestRunnable objects as parameters in its constructor.
	 * 
	 * After you have built the MTTR, you run it with a call to the
	 * runTestRunnables() method.
	 * 
	 * @throws Throwable
	 *             the throwable
	 */
	public void testConcurrency() throws Throwable {

		// instantiate the TestRunnable classes
		SmppThread smsThread1, smsThread2;

		smsThread1 = new SmppThread("session 1", session1_message_count,
				delay_between_messages);
		smsThread2 = new SmppThread("session 2", session2_message_count,
				delay_between_messages);

		// pass that instance to the MTTR
		TestRunnable[] trs = { smsThread1, smsThread2 };
		MultiThreadedTestRunner mttr = new MultiThreadedTestRunner(trs);

		// kickstarts the MTTR & fires off threads
		mttr.runTestRunnables();
		int delivery_count = smsThread1.delivery_count
				+ smsThread2.delivery_count;
		int message_count = session1_message_count + session2_message_count;
		System.out.println(delivery_count);

		// NBNB: smpp delivery reports will be sent to any of the report
		// listeners. But eventually the number of reports must add up.
		assertEquals(true, delivery_count == message_count);
		System.out.println("Done");

	}
}