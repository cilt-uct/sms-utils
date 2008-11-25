package org.sakaiproject.sms.test.stubs;

import java.util.List;

import org.sakaiproject.sms.api.SmsSmpp;
import org.sakaiproject.sms.hibernate.model.SmsMessage;
import org.sakaiproject.sms.model.SmsDeliveryReport;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

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
		throw new NotImplementedException();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sakaiproject.sms.api.SmsSmpp#disconnectGateWay()
	 */
	public void disconnectGateWay() {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sakaiproject.sms.api.SmsSmpp#getConnectionStatus()
	 */
	public int getConnectionStatus() {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sakaiproject.sms.api.SmsSmpp#getDeliveryNotifications()
	 */
	public List<SmsDeliveryReport> getDeliveryNotifications() {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sakaiproject.sms.api.SmsSmpp#getGatewayInfo()
	 */
	public String getGatewayInfo() {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sakaiproject.sms.api.SmsSmpp#processMessageRemotely()
	 */
	public void processMessageRemotely() {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sakaiproject.sms.api.SmsSmpp#sendMessagesToGateway(org.sakaiproject
	 * .sms.hibernate.model.SmsMessage[])
	 */
	public SmsMessage[] sendMessagesToGateway(SmsMessage[] arg0) {
		throw new NotImplementedException();
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
