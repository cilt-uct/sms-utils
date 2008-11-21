package org.sakaiproject.sms.beans;

import java.util.Date;

public class SearchFilterBean {

	private String mobileNumber;
	private String taskStatus;
	private Date dateFrom;
	private Date dateTo;
	private String toolName;
	private String sender;
	
	public SearchFilterBean() {
		super();
	}
	
	public SearchFilterBean(String mobileNumber, String taskStatus,
			Date dateFrom, Date dateTo, String toolName, String sender) {
		super();
		this.mobileNumber = mobileNumber;
		this.taskStatus = taskStatus;
		this.dateFrom = dateFrom;
		this.dateTo = dateTo;
		this.toolName = toolName;
		this.sender = sender;
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
	
	public void fireAction(){
		System.err.println("Action fired");
	}	
}
