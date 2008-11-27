package org.sakaiproject.test;

import junit.framework.TestCase;

import org.jsmpp.session.Session;
import org.sakaiproject.sms.hibernate.model.SmsMessage;
import org.sakaiproject.sms.impl.SmsSmppImpl;

public class SmsSmppImplTest extends TestCase {
	Session session = null;
	SmsSmppImpl smsSmppImpl = new SmsSmppImpl();

	SmsSmppImpl smsSmppImpl2 = new SmsSmppImpl();

	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		smsSmppImpl.init();
	}

	@Override
	protected void tearDown() throws Exception {
		smsSmppImpl.disconnectGateWay();
		smsSmppImpl2.disconnectGateWay();
	}

	public void testBind() {

		assertEquals(true, smsSmppImpl.bind());
	}

	public void testSendFakeSMS() {

		SmsMessage smsMessage = new SmsMessage("+270731876135",
				"voidse toetsstuffesls");
		smsMessage = smsSmppImpl.sendMessageToGateway(smsMessage);
		String smscID = smsMessage.getSmscMessageId();
		assertEquals(true, smscID != null);
	}

	public void testSendMultiSMS() {
		SmsMessage[] smsMessages = new SmsMessage[5];
		for (int i = 0; i < smsMessages.length; i++) {
			smsMessages[i] = new SmsMessage("+270731876135",
					"Jnit tesing forloop num:" + i);
			smsMessages[i].setId(new Long(i));

		}
		// smsMessages = smsSmppImpl.sendMessagesToGateway(smsMessages);
	}

	public void testSendMultiSMStoOtherSession() {

		smsSmppImpl2.init();
		SmsMessage[] smsMessages = new SmsMessage[5];
		for (int i = 0; i < smsMessages.length; i++) {
			smsMessages[i] = new SmsMessage("+270731876135",
					"Jnit tesing forloop num:" + i);
			smsMessages[i].setId(new Long(i));

		}
		// smsMessages = smsSmppImpl2.sendMessagesToGateway(smsMessages);
	}
}
