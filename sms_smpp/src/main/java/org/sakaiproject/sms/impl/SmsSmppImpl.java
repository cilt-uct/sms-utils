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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.jsmpp.InvalidResponseException;
import org.jsmpp.PDUException;
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
import org.jsmpp.bean.SMSCDeliveryReceipt;
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
import org.sakaiproject.sms.hibernate.model.SmsMessage;
import org.sakaiproject.sms.model.SmsDeliveryReport;

public class SmsSmppImpl implements SmsSmpp {
	class BindThread implements Runnable {

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
					Thread.sleep(enquireLinkTimeOut);
				} catch (InterruptedException e) {

					e.printStackTrace();
				}
				System.out.println("Trying to rebind");
				connectToGateway();

			}

		}
	}

	public class MessageReceiverListenerImpl implements MessageReceiverListener {
		public void onAcceptAlertNotification(
				AlertNotification alertNotification) {
		}

		public void onAcceptDeliverSm(DeliverSm deliverSm)
				throws ProcessRequestException {

			if (MessageType.SMSC_DEL_RECEIPT.containedIn(deliverSm
					.getEsmClass())) {
				// this message is delivery receipt
				try {
					DeliveryReceipt delReceipt = deliverSm
							.getShortMessageAsDeliveryReceipt();

					// lets cover the id to hex string format
					long id = Long.parseLong(delReceipt.getId()) & 0xffffffff;
					String messageId = Long.toString(id, 16).toUpperCase();

					SmsDeliveryReport receivedDelReport = new SmsDeliveryReport();

					receivedDelReport.setSmscID(messageId);
					receivedDelReport.setDeliveryReport(delReceipt
							.getFinalStatus().value());
					delReports.add(receivedDelReport);

					if (showDebug) {
						System.out
								.println("Receiving delivery receipt for message '"
										+ messageId
										+ " ' from "
										+ deliverSm.getSourceAddr()
										+ " to "
										+ deliverSm.getDestAddress()
										+ " : "
										+ delReceipt);
					}
				} catch (InvalidDeliveryReceiptException e) {
					System.err.println("Failed getting delivery receipt");
					e.printStackTrace();
				}
			} else {
				// this message is regular short message

				/*
				 * you can save the incoming message to database.
				 */
				if (showDebug) {
					System.out.println("Receiving message : "
							+ new String(deliverSm.getShortMessage()));
				}
			}
		}
	}

	private static TimeFormatter timeFormatter = new AbsoluteTimeFormatter();

	private BindThread bindTest;
	private byte dataCoding;
	private ArrayList<SmsDeliveryReport> delReports = new ArrayList<SmsDeliveryReport>();
	private byte destAddressNPI;
	private byte destAddressTON;
	private long enquireLinkTimeOut;
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
	public boolean showDebug = true;
	private byte smDefaultMsgId;
	private String sourceAddress;
	private byte sourceAddressNPI;
	private byte sourceAddressTON;
	private String systemType;
	private String userName;

	public boolean bind() {

		if (!gatewayBound) {
			System.out.println("Binding to "
					+ properties.getProperty("SMSCadress") + " on port "
					+ properties.getProperty("SMSCport") + " with Username "
					+ properties.getProperty("SMSCUsername"));
			try {
				session = new SMPPSession();
				session.connectAndBind(gatewayAdress, port, new BindParameter(
						BindType.BIND_TRX, userName, password, systemType,
						TypeOfNumber.valueOf(destAddressTON),
						NumberingPlanIndicator.valueOf(destAddressNPI), null));
				if (bindTest != null) {
					bindTest.allDone = true;
				}
				gatewayBound = true;
				session.setEnquireLinkTimer(5000);
				session
						.setMessageReceiverListener(new MessageReceiverListenerImpl());
				session.addSessionStateListener(new SessionStateListener() {

					public void onStateChange(SessionState arg0,
							SessionState arg1, Object arg2) {
						if (arg0.equals(SessionState.CLOSED)
								|| arg0.equals(SessionState.UNBOUND))
							System.out
									.println("SMSC session lost Starting BindThread");
						session.unbindAndClose();
						gatewayBound = false;
						bindTest = new BindThread();

					}
				});

			} catch (Exception e) {

				System.out.println("Bind operation failed. " + e);
				gatewayBound = false;
				session.unbindAndClose();
				if (bindTest == null) {
					System.out.println("Starting Binding thread");
					bindTest = new BindThread();
				}

			}
		}
		return gatewayBound;
	}

	public boolean connectToGateway() {
		return bind();

	}

	public void disconnectGateWay() {
		session.unbindAndClose();

	}

	public boolean getConnectionStatus() {

		if (showDebug) {
			if (gatewayBound) {
				System.out.println("The server is currently binded to "
						+ gatewayAdress + "  " + String.valueOf(port));
			} else {
				System.out.println("The server is not currently binded");
			}
		}
		return gatewayBound;
	}

	public List<SmsDeliveryReport> getDeliveryNotifications() {
		return delReports;

	}

	public String getGatewayInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	public void init() {
		System.out.println("SmsSmpp implementation is starting up");
		loadPropertiesFile();
		loadProperties();
		connectToGateway();
		System.out.println("SmsSmpp implementation is started");
	}

	public void insertIntoLog() {
		// TODO Auto-generated method stub

	}

	public void loadProperties() {

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

		} catch (Exception e) {

		}

	}

	public void loadPropertiesFile() {

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

	public void processMessageRemotely() {
		// TODO Auto-generated method stub

	}

	public void receiveDeliveryReport() {
		// TODO Auto-generated method stub

	}

	public void receiveMessage() {

	}

	public SmsMessage[] sendMessagesToGateway(SmsMessage[] messages) {
		for (int i = 0; i < messages.length; i++) {
			sendMessageToGateway(messages[i]);
		}
		return messages;
	}

	public SmsMessage sendMessageToGateway(SmsMessage message) {

		try {
			String messageId = session
					.submitShortMessage(serviceType, TypeOfNumber
							.valueOf(sourceAddressTON), NumberingPlanIndicator
							.valueOf(sourceAddressNPI), sourceAddress,
							TypeOfNumber.valueOf(destAddressTON),
							NumberingPlanIndicator.valueOf(destAddressNPI),
							message.getMobileNumber(), new ESMClass(),
							protocolId, priorityFlag, timeFormatter
									.format(new Date()), null,
							new RegisteredDelivery(
									SMSCDeliveryReceipt.SUCCESS_FAILURE),
							replaceIfPresentFlag, new GeneralDataCoding(false,
									true, MessageClass.CLASS1,
									Alphabet.ALPHA_DEFAULT), smDefaultMsgId,
							message.getMessageBody().getBytes());

			message.setSmscId(messageId);
			message.setDebugInfo("Message submitted, message_id is "
					+ messageId);
			message.setSubmitResult(true);
			System.out.println("Message submitted, message_id is " + messageId);
		} catch (PDUException e) {
			// Invalid PDU parameter
			message.setDebugInfo("Invalid PDU parameter Message failed");
			System.err.println("Invalid PDU parameter");
			e.printStackTrace();
		} catch (ResponseTimeoutException e) {
			// Response timeout
			message.setDebugInfo("Response timeout Message failed");
			System.err.println("Response timeout");
			e.printStackTrace();
		} catch (InvalidResponseException e) {
			// Invalid response
			message.setDebugInfo("Receive invalid respose Message failed");
			System.err.println("Receive invalid respose");
			e.printStackTrace();
		} catch (NegativeResponseException e) {
			// Receiving negative response (non-zero command_status)
			message.setDebugInfo("Receive negative response Message failed");
			System.err.println("Receive negative response");
			e.printStackTrace();
		} catch (IOException e) {
			message.setDebugInfo("IO error occur Message failed");
			System.err.println("IO error occur");
			e.printStackTrace();
		}
		return message;
	}
}
