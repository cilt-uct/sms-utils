/***********************************************************************************
 * SmsConfig.java
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
 * Some site specific configuration info for the sms tool. COnfiguration info
 * could also be set up for a specific tool in a specific site or for all sites.
 * 
 * @author Julian Wyngaard
 * @version 1.0
 * @created 19-Nov-2008
 */
public class SmsConfig extends BaseModel {

	/**
	 * The sms gateway report timeout in seconds. If a delivery report takes
	 * langer that this, the message will be marked as failed.
	 */
	private Integer gateWayReportTimeout;

	/**
	 * Person(s) who will receive notifications regarding transactions and sms
	 * delivery reports. A comma list is allowed.
	 */
	private String notificationEmail;

	/** The sakai site id. */
	private String sakaiSiteId;

	/** The sakai tool id, if empty, the config is for the site. */
	private String sakaiToolId;

	/** Enable or disable sms functionality for the site or tool. */
	private Boolean smsEnabled;

	/**
	 * Instantiates a new sms configuration.
	 */
	public SmsConfig() {

	}

	/**
	 * Gets the gateway report timeout.
	 * 
	 * @return the gate way report timeout
	 */
	public Integer getGateWayReportTimeout() {
		return gateWayReportTimeout;
	}

	/**
	 * Gets the notification email.
	 * 
	 * @return the notification email
	 */
	public String getNotificationEmail() {
		return notificationEmail;
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
	 * Gets the sms enabled.
	 * 
	 * @return the sms enabled
	 */
	public Boolean getSmsEnabled() {
		return smsEnabled;
	}

	/**
	 * Sets the gateway report timeout.
	 * 
	 * @param gateWayReportTimeout
	 *            the new gateway report timeout
	 */
	public void setGateWayReportTimeout(Integer gateWayReportTimeout) {
		this.gateWayReportTimeout = gateWayReportTimeout;
	}

	/**
	 * Sets the notification email.
	 * 
	 * @param notificationEmail
	 *            the new notification email
	 */
	public void setNotificationEmail(String notificationEmail) {
		this.notificationEmail = notificationEmail;
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
	 * Sets sms enabled/disabled.
	 * 
	 * @param smsEnabled
	 *            the new sms enabled/disabled
	 */
	public void setSmsEnabled(Boolean smsEnabled) {
		this.smsEnabled = smsEnabled;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime
				* result
				+ ((gateWayReportTimeout == null) ? 0 : gateWayReportTimeout
						.hashCode());
		result = prime
				* result
				+ ((notificationEmail == null) ? 0 : notificationEmail
						.hashCode());
		result = prime * result
				+ ((sakaiSiteId == null) ? 0 : sakaiSiteId.hashCode());
		result = prime * result
				+ ((sakaiToolId == null) ? 0 : sakaiToolId.hashCode());
		result = prime * result
				+ ((smsEnabled == null) ? 0 : smsEnabled.hashCode());
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
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof SmsConfig))
			return false;
		SmsConfig other = (SmsConfig) obj;
		if (gateWayReportTimeout == null) {
			if (other.gateWayReportTimeout != null)
				return false;
		} else if (!gateWayReportTimeout.equals(other.gateWayReportTimeout))
			return false;
		if (notificationEmail == null) {
			if (other.notificationEmail != null)
				return false;
		} else if (!notificationEmail.equals(other.notificationEmail))
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
		if (smsEnabled == null) {
			if (other.smsEnabled != null)
				return false;
		} else if (!smsEnabled.equals(other.smsEnabled))
			return false;
		return true;
	}
}
