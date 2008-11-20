/***********************************************************************************
 * SMSSearchBean.java
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

import java.util.Date;

public class SMSSearchBean {

	private String mobileNumber;
	private String taskStatus; //<-- Could be a enum
	private String toolName;
	private String sender;
	private Date dateFrom;
	private Date dateTo;
	
	public SMSSearchBean(String mobileNumber, String taskStatus,
			String toolName, String sender, Date dateFrom, Date dateTo) {
		super();
		this.mobileNumber = mobileNumber;
		this.taskStatus = taskStatus;
		this.toolName = toolName;
		this.sender = sender;
		this.dateFrom = dateFrom;
		this.dateTo = dateTo;
	}
	
	public SMSSearchBean() {
		super();
	}

	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public String getTaskStatus() {
		return taskStatus;
	}
	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}
	public String getToolName() {
		return toolName;
	}
	public void setToolName(String toolName) {
		this.toolName = toolName;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public Date getDateFrom() {
		return dateFrom;
	}
	public void setDateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
	}
	public Date getDateTo() {
		return dateTo;
	}
	public void setDateTo(Date dateTo) {
		this.dateTo = dateTo;
	}
}
