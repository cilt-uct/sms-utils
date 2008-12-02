/***********************************************************************************
 * SmppAPITest.java
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

import java.util.HashSet;
import java.util.Set;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.log4j.Level;
import org.sakaiproject.sms.hibernate.model.SmsMessage;
import org.sakaiproject.sms.hibernate.model.constants.SmsConst_DeliveryStatus;
import org.sakaiproject.sms.impl.SmsSmppImpl;

/**
 * Test some api function on the smpp api. For example successful connect and
 * disconnect to the remote gateway.
 */
public class SmppAPITest extends TestCase {

	private static SmsSmppImpl smsSmppImpl = null;

	// The setup method instanciates the smsCoreImpl once. The teardown
	// method safely calls disconnectGateWay at the end of the test
	public static Test suite() {

		TestSetup setup = new TestSetup(new TestSuite(SmppAPITest.class)) {

			protected void setUp() throws Exception {
				System.out.println("setting up");
				smsSmppImpl = new SmsSmppImpl();
				smsSmppImpl.init();

			}

			protected void tearDown() throws Exception {
				System.out.println("tear Down");
				smsSmppImpl.disconnectGateWay();

			}

		};
		return setup;
	}

	// Testing the connect and disconnecting from the smsc. The test succeeds if
	// the correct connectionStatus is returned.
	public void testGetConnectionStatus() {

		smsSmppImpl.disconnectGateWay();
		assertEquals(true, (!smsSmppImpl.getConnectionStatus()));

		smsSmppImpl.connectToGateway();
		assertEquals(true, (smsSmppImpl.getConnectionStatus()));

	}

	// Test to send 10 smsMessages to the SMSC and receive 11 devieryReports.
	// The test succeeds if the returned status is
	// STATUS_SENT and 10 devieryReports were received.
	public void testSendMessagesToGateway() {
		smsSmppImpl.setLogLevel(Level.WARN);
		Set<SmsMessage> smsMessages = new HashSet<SmsMessage>();
		for (int i = 0; i < 10; i++) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			SmsMessage smsMessage = new SmsMessage("+270731876135",
					"Jnit tesing forloop num:" + i);
			smsMessage.setId(new Long(i));
			smsMessages.add(smsMessage);

		}
		smsSmppImpl.getDeliveryNotifications().clear();
		assertEquals(true, smsSmppImpl.sendMessagesToGateway(smsMessages)
				.equals(SmsConst_DeliveryStatus.STATUS_SENT));
		waitForDeliveries();

		assertEquals(true, smsSmppImpl.getDeliveryNotifications().size() == 10);
		smsSmppImpl.getDeliveryNotifications().clear();
	}

	public void waitForDeliveries() {
		boolean waitForDeliveries = true;
		while (waitForDeliveries) {
			int reportsReceived = smsSmppImpl.getDeliveryNotifications().size();
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			int delivery_count = smsSmppImpl.getDeliveryNotifications().size();
			if (delivery_count == reportsReceived) {
				delivery_count = smsSmppImpl.getDeliveryNotifications().size();
				waitForDeliveries = false;

			}
		}
	}

	// Test to send an single smsMessage to the SMSC.The test succeeds if an
	// SmscID is poputated on the message.
	public void testSendMessageToGateway() {
		SmsMessage smsMessage = new SmsMessage("+270731876135", "testing12345");
		assertEquals(true, smsSmppImpl.sendMessageToGateway(smsMessage)
				.getSmscMessageId() != null);
		waitForDeliveries();
		assertEquals(true, smsSmppImpl.getDeliveryNotifications().size() == 1);

	}

	// The gateway return some information to us.
	public void testGetGatewayInfo() {
		System.out.println(smsSmppImpl.getGatewayInfo());
	}
}
