package org.sakaiproject.test;

import junit.framework.TestCase;

import org.jsmpp.session.Session;
import org.sakaiproject.sms.impl.SmsSmppImpl;

public class SmppAPITest extends TestCase {

	Session session = null;
	SmsSmppImpl smsSmppImpl;

	@Override
	protected void setUp() throws Exception {
		System.out
				.println("___________________START_______________________________\n");
		smsSmppImpl = new SmsSmppImpl();
		smsSmppImpl.init();
	}

	@Override
	protected void tearDown() throws Exception {
		smsSmppImpl.disconnectGateWay();
		System.out
				.println("___________________END_______________________________\n");

	}

	public void testGetConnectionStatusFalse() {

		smsSmppImpl.disconnectGateWay();
		assertEquals(false, (smsSmppImpl.getConnectionStatus()));
	}

	public void testGetConnectionStatusTrue() {
		assertEquals(true, (smsSmppImpl.getConnectionStatus()));
	}

	public void testGetGatewayInfo() {
		System.out.println(smsSmppImpl.getGatewayInfo());
	}
}
