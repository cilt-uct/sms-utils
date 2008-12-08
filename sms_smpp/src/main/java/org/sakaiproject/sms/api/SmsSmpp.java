package org.sakaiproject.sms.api;

import java.util.List;
import java.util.Set;

import org.sakaiproject.sms.hibernate.model.SmsMessage;
import org.sakaiproject.sms.model.SmsDeliveryReport;

/**
 * This service will contain the necessary logic to communicate with a standard
 * SMPP gateway.
 * 
 * @author louis@psybergate.com
 * @created 12-Nov-2008
 */
public interface SmsSmpp {

	/**
	 * Establish a connection to the gateway (bind). The connection will be kept
	 * open for the lifetime of the session. Concurrent connections will be
	 * possible from other smpp services. The status of the connection will be
	 * checked before sending a message, and a auto-bind will be made if
	 * possible.
	 */

	public boolean connectToGateway();

	/**
	 * Unbind from the gateway. If disconnected, no message sending will be
	 * possible. For unit testing purposes.
	 */
	public void disconnectGateWay();

	/**
	 * Enables or disables the debug Information
	 * 
	 * @param debug
	 */
	public void enableDebugInformation(boolean debug);

	/**
	 * Return the status of this connection to the gateway.
	 */
	public boolean getConnectionStatus();

	/**
	 * Get some info about the remote gateway.
	 */
	public String getGatewayInfo();

	/**
	 * Call an external service to handle the processing of an outgoing message.
	 * This could be a simple php script or another Java service.
	 */
	public void processMessageRemotely();

	/**
	 * Send a list of messages to the gateway. Abort if the gateway connection
	 * is down or gateway returns an error and mark relevant messages as failed.
	 * Return message statuses back to caller.
	 */
	public String sendMessagesToGateway(Set<SmsMessage> messages);

	/**
	 * Send one message to the SMS gateway. Return result code to caller.
	 */
	public SmsMessage sendMessageToGateway(SmsMessage message);

	// for testing
	public List<SmsDeliveryReport> getDeliveryNotifications();
}
