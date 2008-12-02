package org.sakaiproject.test;

import net.sourceforge.groboutils.junit.v1.TestRunnable;

import org.sakaiproject.sms.hibernate.model.SmsMessage;
import org.sakaiproject.sms.impl.SmsSmppImpl;

/**
 * The Class SmppSession.
 */
class SmppThread extends TestRunnable {

	private int delay_between_messages;
	/** some private stuff for each thread. */
	public int delivery_count, sent_count, message_count;

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
		// smsSmppImpl.setLogLevel(Level.ERROR);
		// smsSmppImpl.showDebug = false;
		this.message_count = messageCount;
		this.delay_between_messages = messageDelay;
	}

	/**
	 * Send x messages to gateway inside a separate thread
	 */
	@Override
	public void runTest() throws Throwable {
		System.out.println(sessionName + ": sending " + message_count
				+ " to gateway...");
		for (int i = 0; i < message_count; i++) {
			SmsMessage smsMessage = new SmsMessage("+270731876135",
					"Junit testing forloop num:" + i);
			smsMessage.setId(new Long(i));
			smsMessage = smsSmppImpl.sendMessageToGateway(smsMessage);
			if (smsMessage.isSubmitResult()) {
				sent_count++;
			}
			Thread.sleep(delay_between_messages);
		}
		System.out
				.println(sessionName + ": sent " + sent_count + " to gateway");

		boolean waitForDeliveries = true;

		// waiting for a-synchronise delivery reports to arrive. If no
		// reports was received in a 10 seconds window, then we assume all
		// reports was
		// received from the simulator.
		while (waitForDeliveries) {
			int reportsReceived = smsSmppImpl.getDeliveryNotifications().size();
			System.out.println(sessionName + ": waiting for delivery reports ("
					+ reportsReceived + " of " + message_count + ")");
			Thread.sleep(10000);
			delivery_count = smsSmppImpl.getDeliveryNotifications().size();
			if (delivery_count == reportsReceived) {
				delivery_count = smsSmppImpl.getDeliveryNotifications().size();
				waitForDeliveries = false;
				smsSmppImpl.disconnectGateWay();

			}
		}
		// smsSmppImpl.disconnectGateWay();
		System.out.println(sessionName + " ended, received " + delivery_count
				+ " reports");
	}
}