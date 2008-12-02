/***********************************************************************************
 * SmppThread.java
 * Copyright (c) 2008 Sakai Project/Sakai Foundation
 * 
 * Licensed under the Educational Community License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * 
 *      http://www.osedu.org/licenses/ECL-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.s
 *
 **********************************************************************************/
package org.sakaiproject.test;

import net.sourceforge.groboutils.junit.v1.TestRunnable;

import org.apache.log4j.Level;
import org.sakaiproject.sms.hibernate.model.SmsMessage;
import org.sakaiproject.sms.impl.SmsSmppImpl;

/**
 * The Class SmppSession.
 */
class SmppThread extends TestRunnable {
	private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger
			.getLogger(SmppThread.class);
	private int delay_between_messages;
	/** some private stuff for each thread. */
	public int reportsReceivedAfterSleep, sent_count, message_count;

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
		this.smsSmppImpl = new SmsSmppImpl();
		this.smsSmppImpl.init();
		this.smsSmppImpl.setLogLevel(Level.WARN);
		this.LOG.setLevel(Level.ALL);
		this.message_count = messageCount;
		this.delay_between_messages = messageDelay;
	}

	/**
	 * Send x messages to gateway inside a separate thread
	 */
	public void runTest() throws Throwable {
		LOG.info(sessionName + ": sending " + message_count + " to gateway...");
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
		LOG.info(sessionName + ": sent " + sent_count + " to gateway");

		boolean waitForDeliveries = true;

		// waiting for a-synchronise delivery reports to arrive. Every 10
		// secondes we check to see if new messages came in.If the
		// reportsReceivedAfterSleep == reportsReceivedBeforeSleep, then we
		// assume all
		// reports was
		// received from the simulator.
		while (waitForDeliveries) {
			int reportsReceivedBeforeSleep = smsSmppImpl
					.getDeliveryNotifications().size();
			LOG
					.info(sessionName + ": waiting for delivery reports ("
							+ reportsReceivedBeforeSleep + " of "
							+ message_count + ")");
			Thread.sleep(10000);
			reportsReceivedAfterSleep = smsSmppImpl.getDeliveryNotifications()
					.size();
			if (reportsReceivedAfterSleep == reportsReceivedBeforeSleep) {
				reportsReceivedAfterSleep = smsSmppImpl
						.getDeliveryNotifications().size();
				waitForDeliveries = false;

			}
		}
		smsSmppImpl.disconnectGateWay();
		LOG.info(sessionName + " ended, received " + reportsReceivedAfterSleep
				+ " reports");
	}
}