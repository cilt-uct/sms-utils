/***********************************************************************************
 * SmsMessage.java
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

package org.sakaiproject.sms.hibernate.model;

import java.sql.Timestamp;

/**
 * The object to represent a single sms message to be linked up with SmsTask.
 * 
 * @author Julian Wyngaard
 * @version 1.0
 * @created 19-Nov-2008
 */
public class SmsMessage extends AbstractBaseModel {

	/** The date delivered. */
	private Timestamp dateDelivered;

	/** The Debug info. */
	private String DebugInfo;

	/** Unique mobile number to send the message to. */
	private String mobileNumber;

	/**
	 * The sakai user that will receive the message. May be empty in the case of
	 * ad hoc messages.
	 */
	private String sakaiUserId;

	/**
	 * The smsc (sms centre) message id. A unique message id generated by the
	 * message centre.
	 */
	private String smscMessageId;

	/** The sms task linked to this message. */
	private SmsTask smsTask;

	/** Current delivery status of this message. */
	private String statusCode;

	/** The submit result. */
	private boolean submitResult;

	/**
	 * Instantiates a new sms message.
	 */
	public SmsMessage() {

	}

	/**
	 * Instantiates a new sms message. For testing of SMPP service.
	 * 
	 * @param mobileNumber
	 *            the mobile number
	 * @param messageBody
	 *            the message body
	 */
	public SmsMessage(String mobileNumber, String messageBody) {
		smsTask = new SmsTask("", "", "", 0, messageBody);
		this.mobileNumber = mobileNumber;
	}

	/**
	 * Gets the date delivered.
	 * 
	 * @return the date delivered
	 */
	public Timestamp getDateDelivered() {
		return dateDelivered;
	}

	/**
	 * Gets the debug info.
	 * 
	 * @return the debug info
	 */
	public String getDebugInfo() {
		return DebugInfo;
	}

	/**
	 * Gets the message body.
	 * 
	 * @return the message body
	 */
	public String getMessageBody() {
		return smsTask.getMessageBody();
	}

	/**
	 * Gets the mobile number.
	 * 
	 * @return the mobile number
	 */
	public String getMobileNumber() {
		return mobileNumber;
	}

	/**
	 * Gets the sakai user id.
	 * 
	 * @return the sakai user id
	 */
	public String getSakaiUserId() {
		return sakaiUserId;
	}

	/**
	 * Gets the smsc id.
	 * 
	 * @return the smsc id
	 */
	public String getSmscId() {
		return smscMessageId;
	}

	/**
	 * Gets the sms task.
	 * 
	 * @return the sms task
	 */
	public SmsTask getSmsTask() {
		return smsTask;
	}

	/**
	 * Gets the status code.
	 * 
	 * @return the status code
	 */
	public String getStatusCode() {
		return statusCode;
	}

	/**
	 * Checks if submit to gateway was successful.
	 * 
	 * @return true, if is submit result
	 */
	public boolean isSubmitResult() {
		return submitResult;
	}

	/**
	 * Sets the date delivered.
	 * 
	 * @param dateDelivered
	 *            the new date delivered
	 */
	public void setDateDelivered(Timestamp dateDelivered) {
		this.dateDelivered = dateDelivered;
	}

	/**
	 * Sets the debug info.
	 * 
	 * @param debugInfo
	 *            the new debug info
	 */
	public void setDebugInfo(String debugInfo) {
		DebugInfo = debugInfo;
	}

	/**
	 * Sets the message body.
	 * 
	 * @return the message body
	 */
	public void setMessageBody(String messageBody) {
		smsTask.setMessageBody(messageBody);
	}

	/**
	 * Sets the mobile number.
	 * 
	 * @param mobileNumber
	 *            the new mobile number
	 */
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	/**
	 * Sets the sakai user id.
	 * 
	 * @param sakaiUserId
	 *            the new sakai user id
	 */
	public void setSakaiUserId(String sakaiUserId) {
		this.sakaiUserId = sakaiUserId;
	}

	/**
	 * Sets the smsc id.
	 * 
	 * @param smscId
	 *            the new smsc id
	 */
	public void setSmscId(String smscId) {
		this.smscMessageId = smscId;
	}

	/**
	 * Sets the sms task.
	 * 
	 * @param smsTask
	 *            the new sms task
	 */
	public void setSmsTask(SmsTask smsTask) {
		this.smsTask = smsTask;
	}

	/**
	 * Sets the status code.
	 * 
	 * @param statusCode
	 *            the new status code
	 */
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	/**
	 * Sets the submit result.
	 * 
	 * @param submitResult
	 *            the new submit result
	 */
	public void setSubmitResult(boolean submitResult) {
		this.submitResult = submitResult;
	}

}
