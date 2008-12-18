/***********************************************************************************
 * SmsSmppImpl.java
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
 * limitations under the License.
 *
 **********************************************************************************/
package org.sakaiproject.sms.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Level;
import org.jsmpp.InvalidResponseException;
import org.jsmpp.PDUException;
import org.jsmpp.bean.Address;
import org.jsmpp.bean.AlertNotification;
import org.jsmpp.bean.Alphabet;
import org.jsmpp.bean.BindType;
import org.jsmpp.bean.DeliverSm;
import org.jsmpp.bean.DeliveryReceipt;
import org.jsmpp.bean.ESMClass;
import org.jsmpp.bean.GeneralDataCoding;
import org.jsmpp.bean.MessageClass;
import org.jsmpp.bean.MessageType;
import org.jsmpp.bean.NumberingPlanIndicator;
import org.jsmpp.bean.RegisteredDelivery;
import org.jsmpp.bean.ReplaceIfPresentFlag;
import org.jsmpp.bean.SMSCDeliveryReceipt;
import org.jsmpp.bean.SubmitMultiResult;
import org.jsmpp.bean.TypeOfNumber;
import org.jsmpp.extra.NegativeResponseException;
import org.jsmpp.extra.ProcessRequestException;
import org.jsmpp.extra.ResponseTimeoutException;
import org.jsmpp.extra.SessionState;
import org.jsmpp.session.BindParameter;
import org.jsmpp.session.MessageReceiverListener;
import org.jsmpp.session.SMPPSession;
import org.jsmpp.session.SessionStateListener;
import org.jsmpp.util.AbsoluteTimeFormatter;
import org.jsmpp.util.InvalidDeliveryReceiptException;
import org.jsmpp.util.TimeFormatter;
import org.sakaiproject.sms.api.SmsSmpp;
import org.sakaiproject.sms.hibernate.logic.SmsMessageLogic;
import org.sakaiproject.sms.hibernate.logic.SmsTaskLogic;
import org.sakaiproject.sms.hibernate.model.SmsMessage;
import org.sakaiproject.sms.hibernate.model.constants.SmsConst_DeliveryStatus;
import org.sakaiproject.sms.hibernate.model.constants.SmsConst_SmscDeliveryStatus;
import org.sakaiproject.sms.model.SmsStatusBridge;

public class SmsSmppImpl implements SmsSmpp {

	private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger
			.getLogger(SmsSmppImpl.class);
	private static TimeFormatter timeFormatter = new AbsoluteTimeFormatter();
	private BindThread bindTest;
	private int bindThreadTimer;
	private byte dataCoding;
	private byte destAddressNPI;
	private byte destAddressTON;
	private boolean disconnectGateWayCalled;
	private int enquireLinkTimeOut;
	private String gatewayAdress;
	private boolean gatewayBound = false;
	private String password;
	private int port;
	private byte priorityFlag;
	private Properties properties = new Properties();
	private byte protocolId;
	private byte replaceIfPresentFlag;
	private String serviceType;
	private SMPPSession session = new SMPPSession();
	private byte smDefaultMsgId;
	private String sourceAddress;
	private byte sourceAddressNPI;
	private byte sourceAddressTON;
	private String systemType;
	private String userName;
	private String addressRange;
	public SmsMessageLogic smsMessageLogic = null;
	public SmsTaskLogic smsTaskLogic = null;
	private int transactionTimer;
	private int sendingDelay;
	private String smscID;

	public SmsTaskLogic getSmsTaskLogic() {
		return smsTaskLogic;
	}

	public void setSmsTaskLogic(SmsTaskLogic smsTaskLogic) {
		this.smsTaskLogic = smsTaskLogic;
	}
	
	public SmsMessageLogic getSmsMessageLogic() {
		return smsMessageLogic;
	}

	public void setSmsMessageLogic(SmsMessageLogic smsMessageLogic) {
		this.smsMessageLogic = smsMessageLogic;
	}

	private class BindThread implements Runnable {

		boolean allDone = false;

		BindThread() {
			Thread t = new Thread(this);
			t.start();
		}

		public void run() {
			Work();
		}

		public void Work() {
			while (true) {
				if (allDone) {
					return;
				}
				try {
					Thread.sleep(bindThreadTimer);
				} catch (InterruptedException e) {

					e.printStackTrace();
				}
				LOG.info("Trying to rebind");
				connectToGateway();

			}

		}
	}

	/**
	 * This listener will receive delivery reports as well as incoming messages
	 * from the smpp gateway. When we are binded to the gateway, this listener
	 * will receive tcp packets form the gateway. Note that any of the listeners
	 * running on a ip address, will receive reports and not just the session
	 * that sent them!
	 * 
	 * @author etienne@psybergate.co.za
	 * 
	 */
	private class MessageReceiverListenerImpl implements
			MessageReceiverListener {
		public void onAcceptAlertNotification(
				AlertNotification alertNotification) {
		}

		public void onAcceptDeliverSm(DeliverSm deliverSm)
				throws ProcessRequestException {

			if (MessageType.SMSC_DEL_RECEIPT.containedIn(deliverSm
					.getEsmClass())) {
				// this message is delivery receipt
				try {

					DeliveryReceipt deliveryReceipt = deliverSm
							.getShortMessageAsDeliveryReceipt();
					LOG.info("Receiving delivery receipt for message '"
							+ deliveryReceipt.getId() + " ' from "
							+ deliverSm.getSourceAddr() + " to "
							+ deliverSm.getDestAddress() + " : "
							+ deliveryReceipt);
					SmsMessage smsMessage = smsMessageLogic
							.getSmsMessageBySmscMessageId(deliveryReceipt
									.getId());
					if (smsMessage != null) {
						smsMessage.setSmscDeliveryStatusCode(SmsStatusBridge
								.getSmsDeliveryStatus((deliveryReceipt
										.getFinalStatus())));
						smsMessage.setDateDelivered(new Date(System
								.currentTimeMillis()));
						if (SmsStatusBridge
								.getSmsDeliveryStatus((deliveryReceipt
										.getFinalStatus())) != SmsConst_SmscDeliveryStatus.DELIVERED) {
							smsMessage
									.setStatusCode(SmsConst_DeliveryStatus.STATUS_FAIL);
						}

						smsMessageLogic.persistSmsMessage(smsMessage);

					} else {
						LOG
								.error("Delivery report received for message not in database. MessageSMSCID="
										+ deliveryReceipt.getId());
					}

				} catch (InvalidDeliveryReceiptException e) {
					LOG.error("Failed getting delivery receipt" + e);

				}
			} else {
				// this message is regular short message
				LOG.info("Receiving message : "
						+ new String(deliverSm.getShortMessage()));

			}
		}
	}

	/**
	 * Bind to the remote gateway using a username and password. If the
	 * connection is dropped, this service will try and reconnect and specified
	 * intervals.
	 * 
	 * @return
	 */
	private boolean bind() {

		if (!gatewayBound) {

			LOG.info("Binding to " + properties.getProperty("SMSCadress")
					+ " on port " + properties.getProperty("SMSCport")
					+ " with Username "
					+ properties.getProperty("SMSCUsername"));
			try {
				session = new SMPPSession();
				session.connectAndBind(gatewayAdress, port, new BindParameter(
						BindType.BIND_TRX, userName, password, systemType,
						TypeOfNumber.valueOf(destAddressTON),
						NumberingPlanIndicator.valueOf(destAddressNPI),
						addressRange));
				if (bindTest != null) {
					bindTest.allDone = true;
				}
				gatewayBound = true;
				session.setEnquireLinkTimer(enquireLinkTimeOut);
				session.setTransactionTimer(transactionTimer);
				session
						.setMessageReceiverListener(new MessageReceiverListenerImpl());
				session.addSessionStateListener(new SessionStateListener() {

					public void onStateChange(SessionState arg0,
							SessionState arg1, Object arg2) {
						if ((arg0.equals(SessionState.CLOSED) || arg0
								.equals(SessionState.UNBOUND))
								&& (!disconnectGateWayCalled)) {
							LOG.warn("SMSC session lost Starting BindThread");
							session.unbindAndClose();
							gatewayBound = false;
							bindTest = new BindThread();
						}
					}
				});
				LOG.info("Bind successfull");
			} catch (Exception e) {
				LOG.error("Bind operation failed. " + e);
				gatewayBound = false;
				session.unbindAndClose();
				if (bindTest == null) {
					LOG.info("Starting Binding thread");
					bindTest = new BindThread();
				}

			}
		}
		return gatewayBound;
	}

	/**
	 * Establish a connection the the gateway (bind). The connection will be
	 * kept open for the lifetime of the session. Concurrent connections will be
	 * possible from other smpp services. The status of the connection will be
	 * checked before sending a message, and an auto-bind will be made if
	 * possible.
	 */
	public boolean connectToGateway() {
		disconnectGateWayCalled = false;
		return bind();

	}

	/**
	 * Unbind from the gateway. If disconnected, no message sending will be
	 * possible. For unit testing purposes.
	 */
	public void disconnectGateWay() {
		disconnectGateWayCalled = true;
		session.unbindAndClose();
		gatewayBound = false;

	}

	/**
	 * Enables or disables the debug Information
	 * 
	 * @param debug
	 */
	public void enableDebugInformation(boolean debug) {
		if (debug) {
			LOG.setLevel(Level.ALL);
		} else {
			LOG.setLevel(Level.OFF);
		}
	}

	/**
	 * Return the status of this connection to the gateway.
	 */
	public boolean getConnectionStatus() {

		if (gatewayBound) {
			LOG.info("The server is currently binded to " + gatewayAdress
					+ "  " + String.valueOf(port));
		} else {
			LOG.info("The server is not currently binded");
		}

		return gatewayBound;
	}

	/**
	 * Get some info from the remote gateway.
	 */
	public String getGatewayInfo() {
		String gatewayInfo = "Session bound as = " + session.getSessionState()
				+ "\n";
		gatewayInfo += "EnquireLinkTimer = " + session.getEnquireLinkTimer()
				/ 1000 + " seconds \n";
		gatewayInfo += "SessionID is 	 = " + session.getSessionId() + "\n";
		return gatewayInfo;
	}

	public void init() {
		LOG.setLevel(Level.ALL);
		LOG.info("SmsSmpp implementation is starting up");
		loadPropertiesFile();
		loadProperties();
		connectToGateway();
		LOG.info("SmsSmpp implementation is started");
	}

	/**
	 * Read some smpp properties from a file. These properties can be changed as
	 * required.
	 */
	private void loadProperties() {

		try {

			gatewayAdress = properties.getProperty("SMSCadress");
			port = Integer.parseInt(properties.getProperty("SMSCport"));
			userName = properties.getProperty("SMSCUsername");
			password = properties.getProperty("SMSCPassword");
			systemType = properties.getProperty("systemType");
			serviceType = properties.getProperty("serviceType");
			sourceAddress = (properties.getProperty("sourceAddress"));
			sourceAddressNPI = (Byte.parseByte(properties
					.getProperty("sourceAddressNPI")));
			sourceAddressTON = (Byte.parseByte(properties
					.getProperty("sourceAddressTON")));

			destAddressNPI = (Byte.parseByte(properties
					.getProperty("destAddressNPI")));
			destAddressTON = (Byte.parseByte(properties
					.getProperty("destAddressTON")));

			protocolId = Byte.parseByte(properties.getProperty("protocolId"));
			priorityFlag = Byte.parseByte(properties
					.getProperty("priorityFlag"));
			replaceIfPresentFlag = Byte.parseByte(properties
					.getProperty("replaceIfPresentFlag"));
			dataCoding = Byte.parseByte(properties.getProperty("dataCoding"));
			smDefaultMsgId = Byte.parseByte(properties
					.getProperty("smDefaultMsgId"));
			enquireLinkTimeOut = Integer.parseInt(properties
					.getProperty("enquireLinkTimeOutSecondes")) * 1000;
			bindThreadTimer = Integer.parseInt(properties
					.getProperty("bindThreadTimerSecondes")) * 1000;
			addressRange = properties.getProperty("addressRange");

			transactionTimer = Integer.parseInt(properties
					.getProperty("transactionTimer")) * 1000;

			sendingDelay = Integer.parseInt(properties
					.getProperty("sendingDelay"));
			smscID = properties.getProperty("SMSCid");
		} catch (Exception e) {
			LOG.error("Properies faild to load" + e);
		}

	}

	private void loadPropertiesFile() {

		try {
			InputStream is = this.getClass().getResourceAsStream(
					"/smpp.properties");
			if (is != null) {
				properties.load(is);
			} else {
				properties.load((new FileInputStream("smpp.properties")));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Call an external service to handle the processing of an outgoing message.
	 * This could be a simple php script or another Java service.
	 */
	public void processMessageRemotely() {
		// TODO Auto-generated method stub

	}

	/**
	 * Send a list of Bulk messages to the gateway. This method is not
	 * implemented because it does not return a list of message id's. The
	 * optional parameters are causing an exception.
	 */
	public SmsMessage[] sendBulkMessagesToGateway(SmsMessage[] messages) {

		String messageBody = "";
		Address[] addresses = new Address[messages.length];
		String[] mobileNumbers = new String[messages.length];
		for (int i = 0; i < messages.length; i++) {
			messageBody = messages[i].getMessageBody();
			mobileNumbers[i] = messages[i].getMobileNumber();
			addresses[i] = new Address(TypeOfNumber.valueOf(destAddressTON),
					NumberingPlanIndicator.valueOf(destAddressNPI), messages[i]
							.getMobileNumber());
		}
		try {
			SubmitMultiResult submitMultiResult = session
					.submitMultiple(
							serviceType,
							TypeOfNumber.valueOf(sourceAddressTON),
							NumberingPlanIndicator.valueOf(sourceAddressNPI),
							sourceAddress,
							addresses,
							new ESMClass(),
							protocolId,
							priorityFlag,
							timeFormatter.format(new Date()),
							null,
							new RegisteredDelivery(
									SMSCDeliveryReceipt.SUCCESS_FAILURE),
							new ReplaceIfPresentFlag(replaceIfPresentFlag),
							new GeneralDataCoding(false, true,
									MessageClass.CLASS1, Alphabet.ALPHA_DEFAULT),
							smDefaultMsgId, messageBody.getBytes(), null);

		} catch (PDUException e) {
			LOG.error(e);

		} catch (ResponseTimeoutException e) {
			LOG.error(e);

		} catch (InvalidResponseException e) {
			LOG.error(e);

		} catch (NegativeResponseException e) {
			LOG.error(e);

		} catch (IOException e) {
			LOG.error(e);

		} catch (NullPointerException e) {
			e.printStackTrace();
			LOG.error(e);
		}
		return messages;
	}

	/**
	 * Send a list of messages one-by-one to the gateway. Abort if the gateway
	 * connection is down or when gateway returns an error and mark relevant
	 * messages as failed. Return message statuses (not reports) back to caller.
	 * 
	 * @return
	 */
	public String sendMessagesToGateway(Set<SmsMessage> messages) {
		String status = null;
		if (!gatewayBound) {
			return SmsConst_DeliveryStatus.STATUS_RETRY;

		}

		for (SmsMessage message : messages) {
			if (!gatewayBound) {
				return (SmsConst_DeliveryStatus.STATUS_INCOMPLETE);

			}
			try {
				Thread.sleep(sendingDelay);
			} catch (InterruptedException e) {
				LOG.error(e);
			}
			message = sendMessageToGateway(message);
			if (!message.getStatusCode().equals(
					SmsConst_DeliveryStatus.STATUS_SENT)) {
				status = (SmsConst_DeliveryStatus.STATUS_INCOMPLETE);
			}

		}
		if (status == null) {
			return (SmsConst_DeliveryStatus.STATUS_SENT);
		} else {
			return status;
		}
	}

	/**
	 * Send one message to the SMS gateway. Return result code to caller.
	 */
	public SmsMessage sendMessageToGateway(SmsMessage message) {

		if (gatewayBound) {
			try {

				String messageId = session.submitShortMessage(serviceType,
						TypeOfNumber.valueOf(sourceAddressTON),
						NumberingPlanIndicator.valueOf(sourceAddressNPI),
						sourceAddress, TypeOfNumber.valueOf(destAddressTON),
						NumberingPlanIndicator.valueOf(destAddressNPI), message
								.getMobileNumber(), new ESMClass(), protocolId,
						priorityFlag, timeFormatter.format(new Date()), null,
						new RegisteredDelivery(
								SMSCDeliveryReceipt.SUCCESS_FAILURE),
						replaceIfPresentFlag, new GeneralDataCoding(false,
								true, MessageClass.CLASS1,
								Alphabet.ALPHA_DEFAULT), smDefaultMsgId,
						message.getMessageBody().getBytes());
				message.setSmscMessageId(messageId);
				message
						.setDebugInfo("Message sent to gateway, smsc message id is "
								+ messageId);
				message.setSubmitResult(true);
				message.setSmscId(smscID);
				message.setStatusCode(SmsConst_DeliveryStatus.STATUS_SENT);
				message
						.setSmscDeliveryStatusCode(SmsConst_SmscDeliveryStatus.ENROUTE);
				smsTaskLogic.persistSmsTask(message.getSmsTask());
				smsMessageLogic.persistSmsMessage(message);
				LOG.info("Message submitted, message_id is " + messageId);
			} catch (PDUException e) {
				// Invalid PDU parameter
				message.setDebugInfo("Invalid PDU parameter Message failed");
				message.setStatusCode(SmsConst_DeliveryStatus.STATUS_ERROR);
				LOG.error(e);

			} catch (ResponseTimeoutException e) {
				// Response timeout
				message.setDebugInfo("Response timeout Message failed");
				message.setStatusCode(SmsConst_DeliveryStatus.STATUS_ERROR);
				LOG.error(e);

			} catch (InvalidResponseException e) {
				// Invalid response
				message.setDebugInfo("Receive invalid respose Message failed");
				message.setStatusCode(SmsConst_DeliveryStatus.STATUS_ERROR);
				LOG.error(e);

			} catch (NegativeResponseException e) {
				// Receiving negative response (non-zero command_status)
				message
						.setDebugInfo("Receive negative response Message failed");
				message.setStatusCode(SmsConst_DeliveryStatus.STATUS_ERROR);
				LOG.error(e);

			} catch (IOException e) {
				message.setDebugInfo("IO error occur Message failed");
				message.setStatusCode(SmsConst_DeliveryStatus.STATUS_ERROR);
				LOG.error(e);

			}
		} else {
			LOG.error("Sms Gateway is not bound sending failed");
			message.setDebugInfo("Sms Gateway is not bound");
			message.setStatusCode(SmsConst_DeliveryStatus.STATUS_ERROR);
		}
		return message;
	}

	public void setLogLevel(Level level) {
		LOG.setLevel(level);

	}

	
}
