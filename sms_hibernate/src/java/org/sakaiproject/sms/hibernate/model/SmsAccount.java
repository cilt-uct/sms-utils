/***********************************************************************************
 * SmsAccount.java
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

/**
 * Each Sakai site will have its own set of account for billing outgoing
 * messages. A specific user may also have a account.
 * 
 * @author Julian Wyngaard
 * @version 1.0
 * @created 19-Nov-2008
 */
public class SmsAccount extends AbstractBaseModel {

	/** The current account balance in currency. */
	private Float balance;

	/**
	 * The message type, will be incoming (MO) or outgoing (SO), currently only
	 * SO.
	 */
	private String messageTypeCode;

	/** Some accounts will be allowed to have a overdraft limit. */
	private Float overdraftLimit;

	/** The account must be linked to either an Sakai site or a Sakai user. */
	private String sakaiSiteId;

	/** The account must be linked to either an Sakai site or a Sakai user. */
	private String sakaiUserId;

	/**
	 * Instantiates a new sms account.
	 */
	public SmsAccount() {
	}

	/**
	 * Gets the balance.
	 * 
	 * @return the balance
	 */
	public Float getBalance() {
		return balance;
	}

	/**
	 * Gets the message type code.
	 * 
	 * @return the message type code
	 */
	public String getMessageTypeCode() {
		return messageTypeCode;
	}

	/**
	 * Gets the overdraft limit.
	 * 
	 * @return the overdraft limit
	 */
	public Float getOverdraftLimit() {
		return overdraftLimit;
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
	 * Gets the sakai user id.
	 * 
	 * @return the sakai user id
	 */
	public String getSakaiUserId() {
		return sakaiUserId;
	}

	/**
	 * Sets the balance.
	 * 
	 * @param balance
	 *            the new balance
	 */
	public void setBalance(Float balance) {
		this.balance = balance;
	}

	/**
	 * Sets the message type code.
	 * 
	 * @param messageTypeCode
	 *            the new message type code
	 */
	public void setMessageTypeCode(String messageTypeCode) {
		this.messageTypeCode = messageTypeCode;
	}

	/**
	 * Sets the overdraft limit.
	 * 
	 * @param overdraftLimit
	 *            the new overdraft limit
	 */
	public void setOverdraftLimit(Float overdraftLimit) {
		this.overdraftLimit = overdraftLimit;
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
	 * Sets the sakai user id.
	 * 
	 * @param sakaiUserId
	 *            the new sakai user id
	 */
	public void setSakaiUserId(String sakaiUserId) {
		this.sakaiUserId = sakaiUserId;
	}

}
