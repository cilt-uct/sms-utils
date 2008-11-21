package org.sakaiproject.test;

import junit.framework.TestCase;

import org.jsmpp.session.Session;
import org.sakaiproject.sms.impl.SmsSmppImpl;

/**
 * Test some api function on the smpp api. For example successful connect and
 * disconnect to the remote gateway.
 */
public class SmppAPITest extends TestCase {

	Session session = null;
	SmsSmppImpl smsSmppImpl;

	@Override
	protected void setUp() throws Exception {
		smsSmppImpl = new SmsSmppImpl();
		smsSmppImpl.init();
	}

	@Override
	protected void tearDown() throws Exception {
		smsSmppImpl.disconnectGateWay();
	}

	public void testGetConnectionStatusFalse() {

		smsSmppImpl.disconnectGateWay();
		assertEquals(false, (smsSmppImpl.getConnectionStatus()));
	}

	// Our smpp service must auto-connect to the gateway if the connection is
	// down. It is down because we disconnected in the prev. test.
	public void testGetConnectionStatusTrue() {
		assertEquals(true, (smsSmppImpl.getConnectionStatus()));
	}

	// The gateway return some information to us.
	public void testGetGatewayInfo() {
		System.out.println(smsSmppImpl.getGatewayInfo());
	}
}
