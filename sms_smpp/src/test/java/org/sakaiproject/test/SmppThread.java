package org.sakaiproject.test;

import net.sourceforge.groboutils.junit.v1.TestRunnable;

import org.sakaiproject.sms.hibernate.model.SmsMessage;
import org.sakaiproject.sms.impl.SmsSmppImpl;

/**
 * The Class SmppSession.
 */
class SmppThread extends TestRunnable {

	/** some private stuff for each thread. */
	public int delivery_count, sent_count, message_count;
	private int delay_between_messages;

	/** The session name. */
	private String sessionName;

	/** The sms smpp impl. */
	private SmsSmppImpl smsSmppImpl;

	/**
	 * Instantiates a new smpp session.
	 * 
	 * @param sessionName
	 *            the session name
	 * @param messageCount
	 *            the batch_count
	 */
	public SmppThread(String sessionName, int messageCount, int messageDelay) {
		this.sessionName = sessionName;
		smsSmppImpl = new SmsSmppImpl();
		smsSmppImpl.init();
		// smsSmppImpl.showDebug = false;
		this.message_count = messageCount;
		this.delay_between_messages = messageDelay;
	}

	/**
	 * Send x messages to gateway inside a separate thread
	 */
	@Override
	public void runTest() throws Throwable {
		System.out.println("Thread starting: " + sessionName);
		for (int i = 0; i < message_count; i++) {
			SmsMessage smsMessage = new SmsMessage("+270731876135",
					"Junit tesing forloop num:" + i);
			System.out.println(sessionName + " send message " + i);
			smsMessage.setId(i);
			smsMessage = smsSmppImpl.sendMessageToGateway(smsMessage);
			if (smsMessage.isSubmitResult()) {
				sent_count++;
			}
			Thread.sleep(delay_between_messages);
		}
		boolean waitForDeliveries = true;

		// waiting for a-synchronise delivery reports to arrive. If no
		// reports was
		// received in a 10 seconds window, then we assume all reports was
		// received from the simulator.
		while (waitForDeliveries) {
			int reportsReceived = smsSmppImpl.getDeliveryNotifications().size();
			System.out.println("Waiting for delivery reports in " + sessionName
					+ ":" + reportsReceived);
			delivery_count = smsSmppImpl.getDeliveryNotifications().size();
			Thread.sleep(20000);
			if (delivery_count == reportsReceived) {
				waitForDeliveries = false;

			}
		}
		// smsSmppImpl.disconnectGateWay();
		System.out.println("Thread ended: " + sessionName + ", reports="
				+ delivery_count);
	}
}