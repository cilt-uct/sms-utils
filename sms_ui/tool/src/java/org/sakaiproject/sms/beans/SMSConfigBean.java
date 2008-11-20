/***********************************************************************************
 * SMSConfigBean.java
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

package org.sakaiproject.sms.beans;

public class SMSConfigBean {

	private  Long configID;
	private String notificatonEmail;
	private Boolean smsEnabled;

	public Long getConfigID() {
		return configID;
	}
		
	public SMSConfigBean() {
		super();
	}

	public SMSConfigBean(Long configID, Long siteID, String toolID,
			Boolean isSmsEnabled, String notificatonEmail,
			Integer gatewayReportTimeout) {
		super();
		this.configID = configID;
		this.smsEnabled = isSmsEnabled;
		this.notificatonEmail = notificatonEmail;
	}

	public void setConfigID(Long configID) {
		this.configID = configID;
	}

	public Boolean getSmsEnabled() {
		return smsEnabled;
	}
	
	public void setSmsEnabled(Boolean smsEnabled) {
		this.smsEnabled = smsEnabled;
	}
	
	public String getNotificatonEmail() {
		return notificatonEmail;
	}
	
	public void setNotificatonEmail(String notificatonEmail) {
		this.notificatonEmail = notificatonEmail;
	}
}
