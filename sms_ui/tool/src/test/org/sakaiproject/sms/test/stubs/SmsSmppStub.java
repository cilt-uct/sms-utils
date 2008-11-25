package org.sakaiproject.sms.test.stubs;

import java.util.List;

import org.sakaiproject.sms.api.SmsSmpp;
import org.sakaiproject.sms.hibernate.model.SmsMessage;
import org.sakaiproject.sms.model.SmsDeliveryReport;

/**
 * The Class SmsSmppStub. Stub implementation of {@link SmsSmpp} with minimal
 * implementation, used for testing
 */
public class SmsSmppStub implements SmsSmpp {

	/**
	 * Public field to force RunTimeException from method
	 */
	public boolean forceException;

	/**
	 * Constant to set debug Info to if called
	 */
	public static final String CALLED = "called";

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sakaiproject.sms.api.SmsSmpp#connectToGateway()
	 */
	public boolean connectToGateway() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sakaiproject.sms.api.SmsSmpp#disconnectGateWay()
	 */
	public void disconnectGateWay() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sakaiproject.sms.api.SmsSmpp#getConnectionStatus()
	 */
	public int getConnectionStatus() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sakaiproject.sms.api.SmsSmpp#getDeliveryNotifications()
	 */
	public List<SmsDeliveryReport> getDeliveryNotifications() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sakaiproject.sms.api.SmsSmpp#getGatewayInfo()
	 */
	public String getGatewayInfo() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sakaiproject.sms.api.SmsSmpp#processMessageRemotely()
	 */
	public void processMessageRemotely() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sakaiproject.sms.api.SmsSmpp#sendMessagesToGateway(org.sakaiproject
	 * .sms.hibernate.model.SmsMessage[])
	 */
	public SmsMessage[] sendMessagesToGateway(SmsMessage[] arg0) {
		return new SmsMessage[] {};
	}

	/**
	 * Test method that sets debugInfo as "called"
	 * 
	 * @see org.sakaiproject.sms.api.SmsSmpp#sendMessageToGateway(org.sakaiproject
	 *      .sms.hibernate.model.SmsMessage)
	 */
	public SmsMessage sendMessageToGateway(SmsMessage msg) {
		msg.setDebugInfo(CALLED);

		if (forceException) {
			throw new RuntimeException();
		}

		return msg;
	}

}
