package org.sakaiproject.test;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import net.sourceforge.groboutils.junit.v1.MultiThreadedTestRunner;
import net.sourceforge.groboutils.junit.v1.TestRunnable;

import org.sakaiproject.sms.hibernate.model.SmsMessage;
import org.sakaiproject.sms.impl.SmsSmppImpl;

public class SmppThreadingTest extends TestCase {

	private class SmppSession extends TestRunnable {

		private int delivery_count, success_count, batch_count;
		private String sessionName;
		private SmsSmppImpl smsSmppImpl;

		private SmppSession(String sessionName, int batch_count) {
			this.sessionName = sessionName;
			smsSmppImpl = new SmsSmppImpl();
			smsSmppImpl.init();
			this.batch_count = batch_count;
		}

		@Override
		public void runTest() throws Throwable {
			System.out.println("Thread starting: " + sessionName);
			for (int i = 0; i < batch_count; i++) {
				SmsMessage smsMessage = new SmsMessage("+270731876135",
						"Jnit tesing forloop num:" + i);
				System.out.println(sessionName + " send message " + i);
				smsMessage.setId(i);

				smsMessage = smsSmppImpl.sendMessageToGateway(smsMessage);
				if (smsMessage.isSubmitResult()) {
					success_count++;
				}
				Thread.sleep(100);
			}
			boolean waitForDeliveries = true;

			// waiting for async delivery reports to arrive.
			while (waitForDeliveries) {
				delivery_count = smsSmppImpl.getDeliveryNotifications().size();
				Thread.sleep(5000);
				if (delivery_count == smsSmppImpl.getDeliveryNotifications()
						.size()) {
					waitForDeliveries = false;

				}

			}
			System.out.println("Thread ended: " + sessionName);
		}
	}

	/**
	 * Standard main() and suite() methods
	 */
	public static void main(String[] args) {
		String[] name = { SmppThreadingTest.class.getName() };
		junit.textui.TestRunner.main(name);
	}

	public static Test suite() {
		return new TestSuite(SmppThreadingTest.class);
	}

	private int session1_batch_count = 5;

	private int session2_batch_count = 10;

	/**
	 * You use the MultiThreadedTestRunner in your test cases. The MTTR takes an
	 * array of TestRunnable objects as parameters in its constructor.
	 * 
	 * After you have built the MTTR, you run it with a call to the
	 * runTestRunnables() method.
	 */
	public void testExampleThread() throws Throwable {

		// instantiate the TestRunnable classes
		SmppSession session1, session2;

		session1 = new SmppSession("SMSC Session 1", session1_batch_count);
		session2 = new SmppSession("SMSC Session 2", session2_batch_count);

		// pass that instance to the MTTR
		TestRunnable[] trs = { session1, session2 };
		MultiThreadedTestRunner mttr = new MultiThreadedTestRunner(trs);

		// kickstarts the MTTR & fires off threads
		mttr.runTestRunnables();

		System.out.println(session1.success_count);
		System.out.println(session2.success_count);
		System.out.println(session1.delivery_count + session2.delivery_count);

		assertEquals(true, session1.success_count == session1_batch_count);
		assertEquals(true, session2.success_count == session2_batch_count);
		assertEquals(
				true,
				(session1.delivery_count + session2.delivery_count) == (session1_batch_count + session2_batch_count));
		System.out.println("Done");

	}
}