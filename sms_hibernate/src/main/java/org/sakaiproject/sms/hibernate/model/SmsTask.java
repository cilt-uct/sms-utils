/***********************************************************************************
 * SmsTask.java
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

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.sakaiproject.sms.hibernate.model.constants.SmsConst_DeliveryStatus;
import org.sakaiproject.sms.hibernate.util.DateUtil;

/**
 * A sms task that needs to be processed. For example: send message X to sakai
 * group Y at time Z. When a sms task is processed, a record is inserted into
 * SMS_MESSAGE for each message that must be sent out.
 * 
 * @author Julian Wyngaard
 * @version 1.0
 * @created 19-Nov-2008
 */
public class SmsTask extends BaseModel {

	/**
	 * Approximate credit cost for this task as calculated when the task is
	 * created. The exact credits can only be calculated when the task is
	 * processed and this might happen in the future.
	 */
	private Integer creditEstimate;

	/** The date-time when the task was successfully create. */
	private Date dateCreated;

	/**
	 * The date-time when the task was last processed. It might be processed a
	 * few times until successful or until the attempt count reaches a
	 * predefined maximum..
	 */
	private Date dateProcessed;

	/**
	 * Post dated for future delivery. If this date is equal to the current time
	 * or null, the message will be processed immediately, if possible.
	 */
	private Date dateToSend;

	/** The Sakai group who will receive the message, empty if not applicable. */
	private String deliveryGroupId;

	/** The friendly name of the Sakai group. */
	private String deliveryGroupName;

	/** Will be used for incoming messages. For phase II. */
	private String deliveryUserId;

	/** The actual Sakai group size. Calculated when the task is processed. */
	private Integer groupSizeActual;

	/** The estimated Sakai group size. Calculated when the task is created. */
	private Integer groupSizeEstimate;

	/** The message body. Already validated for character set, length etc. */
	private String messageBody;

	/** Type of task, only SO (system originating) for now. */
	private Integer messageTypeId;

	/**
	 * Number of delivery attempts until the task is marked as failed. The
	 * processing of the task will fail when the gateway is down or the line is
	 * down.
	 */
	private Integer attemptCount;

	/** The sakai site from where the sms task originated. */
	private String sakaiSiteId;

	/** The sakai tool id from where the sms task originated. */
	private String sakaiToolId;

	/** The sakai tool name from where the sms task originated. */
	private String sakaiToolName;

	/** The Sakai user name of the sender. */
	private String senderUserName;

	/** The sms account (cost centre) that will pay for the messages. */
	private Integer smsAccountId;

	/**
	 * The sms messages for this task. This will be generated when the task is
	 * processed.
	 */
	private Set<SmsMessage> smsMessages = new HashSet<SmsMessage>();

	/** Current status of this task. See SmsConst_TaskDeliveryStatus */
	private String statusCode;

	/**
	 * The maximum amount of minutes to allow this task to be pending since it
	 * date-to-deliver. Some tasks like announcements are time critical and is
	 * not relevant when it is sent out too late.
	 */
	private Integer maxTimeToLive;

	/**
	 * The maximum amount of minutes to wait for a delivery report for each
	 * message. If a message exeeds this time, it will be marked as failed.
	 */
	private Integer delReportTimeoutDuration;

	/**
	 * Instantiates a new sms task.
	 */
	public SmsTask() {
		setStatusCode(SmsConst_DeliveryStatus.STATUS_PENDING);
		setMessageTypeId(0);// outgoing, 1=incoming
	}

	/**
	 * Instantiates a new sms task.
	 * 
	 * @param sakaiSiteID
	 *            the sakai site id
	 * @param deliveryUserID
	 *            the delivery user id
	 * @param deliveryGroupID
	 *            the delivery group id
	 * @param accountID
	 *            the account id
	 * @param messageBody
	 *            the message body
	 * @Depricated Use default constructor and setter methods instead
	 * 
	 */
	private SmsTask(String sakaiSiteID, String deliveryUserID,
			String deliveryGroupID, int accountID, String messageBody) {
		this();
		this.sakaiSiteId = sakaiSiteID;
		this.deliveryUserId = deliveryUserID;
		this.deliveryGroupId = deliveryGroupID;
		this.smsAccountId = accountID;
		this.messageBody = messageBody;
		this.attemptCount = 0;
		this.statusCode = "";
		this.creditEstimate = 0;
		this.statusCode = SmsConst_DeliveryStatus.STATUS_PENDING;

	}

	/**
	 * Gets the credit estimate.
	 * 
	 * @return the credit estimate
	 */
	public Integer getCreditEstimate() {
		return creditEstimate;
	}

	/**
	 * Gets the date created.
	 * 
	 * @return the date created
	 */
	public Date getDateCreated() {
		return dateCreated;
	}

	/**
	 * Gets the date processed.
	 * 
	 * @return the date processed
	 */
	public Date getDateProcessed() {
		return dateProcessed;
	}

	/**
	 * Reschedules the date to send the message.
	 * 
	 * @param dateToSend
	 *            the date to send
	 */
	public void rescheduleDateToSend(Date dateToSend) {
		this.setDateToSend(dateToSend);
	}

	/**
	 * Gets the date to send.
	 * 
	 * @return the date to send
	 */
	public Date getDateToSend() {
		return dateToSend;
	}

	/**
	 * Gets the delivery group id.
	 * 
	 * @return the delivery group id
	 */
	public String getDeliveryGroupId() {
		return deliveryGroupId;
	}

	/**
	 * Gets the delivery group name.
	 * 
	 * @return the delivery group name
	 */
	public String getDeliveryGroupName() {
		return deliveryGroupName;
	}

	/**
	 * Gets the delivery user id.
	 * 
	 * @return the delivery user id
	 */
	public String getDeliveryUserId() {
		return deliveryUserId;
	}

	/**
	 * Gets the group size actual.
	 * 
	 * @return the group size actual
	 */
	public Integer getGroupSizeActual() {
		return groupSizeActual;
	}

	/**
	 * Gets the group size estimate.
	 * 
	 * @return the group size estimate
	 */
	public Integer getGroupSizeEstimate() {
		return groupSizeEstimate;
	}

	/**
	 * Gets the message body.
	 * 
	 * @return the message body
	 */
	public String getMessageBody() {
		return messageBody;
	}

	/**
	 * Gets the message type id.
	 * 
	 * @return the message type id
	 */
	public Integer getMessageTypeId() {
		return messageTypeId;
	}

	/**
	 * Gets the attempt count.
	 * 
	 * @return the attempt count
	 */
	public Integer getAttemptCount() {
		return attemptCount;
	}

	/**
	 * Gets the sakai site id.
	 * 
	 * @return the sakai site id
	 */
	public String getSakaiSiteId() {
		return sakaiSiteId;
	}

	/**
	 * Gets the sakai tool id.
	 * 
	 * @return the sakai tool id
	 */
	public String getSakaiToolId() {
		return sakaiToolId;
	}

	/**
	 * Gets the sakai tool name.
	 * 
	 * @return the sakai tool name
	 */
	public String getSakaiToolName() {
		return sakaiToolName;
	}

	/**
	 * Gets the sender user name.
	 * 
	 * @return the sender user name
	 */
	public String getSenderUserName() {
		return senderUserName;
	}

	/**
	 * Gets the sms account id.
	 * 
	 * @return the sms account id
	 */
	public Integer getSmsAccountId() {
		return smsAccountId;
	}

	/**
	 * Gets the sms messages.
	 * 
	 * @return the sms messages
	 */
	public Set<SmsMessage> getSmsMessages() {
		return smsMessages;
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
	 * Sets the credit estimate.
	 * 
	 * @param creditEstimate
	 *            the new credit estimate
	 */
	public void setCreditEstimate(Integer creditEstimate) {
		this.creditEstimate = creditEstimate;
	}

	/**
	 * Sets the date created.
	 * 
	 * @param dateCreated
	 *            the new date created
	 */
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = DateUtil.getUsableDate(dateCreated);
	}

	/**
	 * Sets the date processed.
	 * 
	 * @param dateProcessed
	 *            the new date processed
	 */
	public void setDateProcessed(Date dateProcessed) {
		this.dateProcessed = DateUtil.getUsableDate(dateProcessed);
	}

	/**
	 * Sets the date to send.
	 * 
	 * @param dateToSend
	 *            the new date to send
	 */
	public void setDateToSend(Date dateToSend) {
		this.dateToSend = DateUtil.getUsableDate(dateToSend);
	}

	/**
	 * Sets the delivery group id.
	 * 
	 * @param deliveryGroupId
	 *            the new delivery group id
	 */
	public void setDeliveryGroupId(String deliveryGroupId) {
		this.deliveryGroupId = deliveryGroupId;
	}

	/**
	 * Sets the delivery group name.
	 * 
	 * @param deliveryGroupName
	 *            the new delivery group name
	 */
	public void setDeliveryGroupName(String deliveryGroupName) {
		this.deliveryGroupName = deliveryGroupName;
	}

	/**
	 * Sets the delivery user id.
	 * 
	 * @param deliveryUserId
	 *            the new delivery user id
	 */
	public void setDeliveryUserId(String deliveryUserId) {
		this.deliveryUserId = deliveryUserId;
	}

	/**
	 * Sets the group size actual.
	 * 
	 * @param groupSizeActual
	 *            the new group size actual
	 */
	public void setGroupSizeActual(Integer groupSizeActual) {
		this.groupSizeActual = groupSizeActual;
	}

	/**
	 * Sets the group size estimate.
	 * 
	 * @param groupSizeEstimate
	 *            the new group size estimate
	 */
	public void setGroupSizeEstimate(Integer groupSizeEstimate) {
		this.groupSizeEstimate = groupSizeEstimate;
	}

	/**
	 * Sets the message body.
	 * 
	 * @param messageBody
	 *            the new message body
	 */
	public void setMessageBody(String messageBody) {
		this.messageBody = messageBody;
	}

	/**
	 * Sets the message type id.
	 * 
	 * @param messageTypeId
	 *            the new message type id
	 */
	public void setMessageTypeId(Integer messageTypeId) {
		this.messageTypeId = messageTypeId;
	}

	/**
	 * Sets the attempt count.
	 * 
	 * @param attemptCount
	 *            the new attempt count
	 */
	public void setAttemptCount(Integer attemptCount) {
		this.attemptCount = attemptCount;
	}

	/**
	 * Sets the sakai site id.
	 * 
	 * @param sakaiSiteId
	 *            the new sakai site id
	 */
	public void setSakaiSiteId(String sakaiSiteId) {
		this.sakaiSiteId = sakaiSiteId;
	}

	/**
	 * Sets the sakai tool id.
	 * 
	 * @param sakaiToolId
	 *            the new sakai tool id
	 */
	public void setSakaiToolId(String sakaiToolId) {
		this.sakaiToolId = sakaiToolId;
	}

	/**
	 * Sets the sakai tool name.
	 * 
	 * @param sakaiToolName
	 *            the new sakai tool name
	 */
	public void setSakaiToolName(String sakaiToolName) {
		this.sakaiToolName = sakaiToolName;
	}

	/**
	 * Sets the sender user name.
	 * 
	 * @param senderUserName
	 *            the new sender user name
	 */
	public void setSenderUserName(String senderUserName) {
		this.senderUserName = senderUserName;
	}

	/**
	 * Sets the sms account id.
	 * 
	 * @param smsAccountId
	 *            the new sms account id
	 */
	public void setSmsAccountId(Integer smsAccountId) {
		this.smsAccountId = smsAccountId;
	}

	/**
	 * Sets the sms messages.
	 * 
	 * @param smsMessages
	 *            the new sms messages
	 */
	private void setSmsMessages(Set<SmsMessage> smsMessages) {
		this.smsMessages = smsMessages;
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
	 * Sets the sms messages on a this SmsTask object.
	 * 
	 * @param smsMessages
	 *            the sms messages
	 */
	public void setSmsMessagesOnTask(Set<SmsMessage> smsMessages) {
		if (smsMessages != null) {
			for (SmsMessage message : smsMessages) {
				this.smsMessages.add(message);
			}
		}
	}

	/**
	 * Sets the status for messages.
	 * 
	 * @param oldStatus
	 *            the old status
	 * @param newStatus
	 *            the new status
	 */
	public void setStatusForMessages(String oldStatus, String newStatus) {
		if (smsMessages != null) {
			for (SmsMessage message : smsMessages) {
				if (message.getStatusCode().equals(oldStatus)) {
					message.setStatusCode(newStatus);
				}
			}
		}

	}

	/**
	 * Gets the messages with smsc status.
	 * 
	 * @param smscStatus
	 *            the smsc status
	 * 
	 * @return the messages with smsc status
	 */
	public Set<SmsMessage> getMessagesWithSmscStatus(int smscStatus) {
		Set<SmsMessage> filtered = new HashSet<SmsMessage>();
		if (smsMessages != null) {
			for (SmsMessage message : smsMessages) {
				if (message.getSmscDeliveryStatusCode() != null
						&& message.getSmscDeliveryStatusCode().equals(
								smscStatus)) {
					filtered.add(message);
				}
			}
		}
		return filtered;

	}

	/**
	 * Gets the messages with status.
	 * 
	 * @param status
	 *            the status
	 * 
	 * @return the messages with status
	 */
	public Set<SmsMessage> getMessagesWithStatus(String status) {
		Set<SmsMessage> filtered = new HashSet<SmsMessage>();
		if (smsMessages != null) {
			for (SmsMessage message : smsMessages) {
				if (message.getStatusCode().equals(status)) {
					filtered.add(message);
				}
			}
		}
		return filtered;
	}

	/**
	 * Gets the max time to live.
	 * <p>
	 * NB: This is in minutes
	 * 
	 * @return the max time to live
	 */
	public Integer getMaxTimeToLive() {
		return maxTimeToLive;
	}

	/**
	 * Sets the max time to live.
	 * <p>
	 * NB: This is in minutes
	 * 
	 * @param maxTimeToLive
	 *            the new max time to live
	 */
	public void setMaxTimeToLive(Integer maxTimeToLive) {
		this.maxTimeToLive = maxTimeToLive;
	}

	/**
	 * Gets the delivery report timeout duration.
	 * <p>
	 * NB: This is in minutes
	 * 
	 * @return the del report timeout duration
	 */
	public Integer getDelReportTimeoutDuration() {
		return delReportTimeoutDuration;
	}

	/**
	 * Sets the delivery report timeout duration.
	 * <p>
	 * NB: This is in minutes
	 * 
	 * @param delReportTimeoutDuration
	 *            the new del report timeout duration
	 */
	public void setDelReportTimeoutDuration(Integer delReportTimeoutDuration) {
		this.delReportTimeoutDuration = delReportTimeoutDuration;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		// int result = super.hashCode();
		int result = 43;
		result = prime * result
				+ ((attemptCount == null) ? 0 : attemptCount.hashCode());
		result = prime * result
				+ ((creditEstimate == null) ? 0 : creditEstimate.hashCode());
		result = prime * result
				+ ((dateCreated == null) ? 0 : dateCreated.hashCode());
		result = prime * result
				+ ((dateProcessed == null) ? 0 : dateProcessed.hashCode());
		result = prime * result
				+ ((dateToSend == null) ? 0 : dateToSend.hashCode());
		result = prime * result
				+ ((deliveryGroupId == null) ? 0 : deliveryGroupId.hashCode());
		result = prime
				* result
				+ ((deliveryGroupName == null) ? 0 : deliveryGroupName
						.hashCode());
		result = prime * result
				+ ((deliveryUserId == null) ? 0 : deliveryUserId.hashCode());
		result = prime * result
				+ ((groupSizeActual == null) ? 0 : groupSizeActual.hashCode());
		result = prime
				* result
				+ ((groupSizeEstimate == null) ? 0 : groupSizeEstimate
						.hashCode());
		result = prime * result
				+ ((messageBody == null) ? 0 : messageBody.hashCode());
		result = prime * result
				+ ((messageTypeId == null) ? 0 : messageTypeId.hashCode());
		result = prime * result
				+ ((sakaiSiteId == null) ? 0 : sakaiSiteId.hashCode());
		result = prime * result
				+ ((sakaiToolId == null) ? 0 : sakaiToolId.hashCode());
		result = prime * result
				+ ((sakaiToolName == null) ? 0 : sakaiToolName.hashCode());
		result = prime * result
				+ ((senderUserName == null) ? 0 : senderUserName.hashCode());
		result = prime * result
				+ ((smsAccountId == null) ? 0 : smsAccountId.hashCode());
		result = prime * result
				+ ((smsMessages == null) ? 0 : smsMessages.hashCode());
		result = prime * result
				+ ((statusCode == null) ? 0 : statusCode.hashCode());
		result = prime * result
				+ ((maxTimeToLive == null) ? 0 : maxTimeToLive.hashCode());
		result = prime
				* result
				+ ((delReportTimeoutDuration == null) ? 0
						: delReportTimeoutDuration.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof SmsTask))
			return false;
		SmsTask other = (SmsTask) obj;
		if (attemptCount == null) {
			if (other.attemptCount != null)
				return false;
		} else if (!attemptCount.equals(other.attemptCount))
			return false;
		if (creditEstimate == null) {
			if (other.creditEstimate != null)
				return false;
		} else if (!creditEstimate.equals(other.creditEstimate))
			return false;
		if (dateCreated == null) {
			if (other.dateCreated != null)
				return false;
		} else if (!dateCreated.equals(other.dateCreated))
			return false;
		if (dateProcessed == null) {
			if (other.dateProcessed != null)
				return false;
		} else if (!dateProcessed.equals(other.dateProcessed))
			return false;
		if (dateToSend == null) {
			if (other.dateToSend != null)
				return false;
		} else if (!dateToSend.equals(other.dateToSend))
			return false;
		if (deliveryGroupId == null) {
			if (other.deliveryGroupId != null)
				return false;
		} else if (!deliveryGroupId.equals(other.deliveryGroupId))
			return false;
		if (deliveryGroupName == null) {
			if (other.deliveryGroupName != null)
				return false;
		} else if (!deliveryGroupName.equals(other.deliveryGroupName))
			return false;
		if (deliveryUserId == null) {
			if (other.deliveryUserId != null)
				return false;
		} else if (!deliveryUserId.equals(other.deliveryUserId))
			return false;
		if (groupSizeActual == null) {
			if (other.groupSizeActual != null)
				return false;
		} else if (!groupSizeActual.equals(other.groupSizeActual))
			return false;
		if (groupSizeEstimate == null) {
			if (other.groupSizeEstimate != null)
				return false;
		} else if (!groupSizeEstimate.equals(other.groupSizeEstimate))
			return false;
		if (messageBody == null) {
			if (other.messageBody != null)
				return false;
		} else if (!messageBody.equals(other.messageBody))
			return false;
		if (messageTypeId == null) {
			if (other.messageTypeId != null)
				return false;
		} else if (!messageTypeId.equals(other.messageTypeId))
			return false;
		if (sakaiSiteId == null) {
			if (other.sakaiSiteId != null)
				return false;
		} else if (!sakaiSiteId.equals(other.sakaiSiteId))
			return false;
		if (sakaiToolId == null) {
			if (other.sakaiToolId != null)
				return false;
		} else if (!sakaiToolId.equals(other.sakaiToolId))
			return false;
		if (sakaiToolName == null) {
			if (other.sakaiToolName != null)
				return false;
		} else if (!sakaiToolName.equals(other.sakaiToolName))
			return false;
		if (senderUserName == null) {
			if (other.senderUserName != null)
				return false;
		} else if (!senderUserName.equals(other.senderUserName))
			return false;
		if (smsAccountId == null) {
			if (other.smsAccountId != null)
				return false;
		} else if (!smsAccountId.equals(other.smsAccountId))
			return false;
		if (smsMessages == null) {
			if (other.smsMessages != null)
				return false;
		} else if (!smsMessages.equals(other.smsMessages))
			return false;
		if (statusCode == null) {
			if (other.statusCode != null)
				return false;
		} else if (!statusCode.equals(other.statusCode))
			return false;

		if (maxTimeToLive == null) {
			if (other.maxTimeToLive != null)
				return false;
		} else if (!maxTimeToLive.equals(other.maxTimeToLive))
			return false;
		if (delReportTimeoutDuration == null) {
			if (other.delReportTimeoutDuration != null)
				return false;
		} else if (!delReportTimeoutDuration
				.equals(other.delReportTimeoutDuration))
			return false;

		return true;
	}

}
