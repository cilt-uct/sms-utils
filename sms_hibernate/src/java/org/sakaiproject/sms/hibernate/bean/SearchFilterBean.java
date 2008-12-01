package org.sakaiproject.sms.hibernate.bean;

public class SearchFilterBean {

	private String id;
	private String taskStatus;
	private String dateFrom;
	private String dateTo;
	private String toolName;
	private String sender;
	private Integer currentPage;
	private String orderBy;
	private String mobileNumber;
	
	
	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public SearchFilterBean() {
		super();
		currentPage = new Integer(1);
	}
	
	public SearchFilterBean(String id, String taskStatus, String dateFrom,
			String dateTo, String toolName, String sender, Integer currentPage) {
		super();
		this.id = id;
		this.taskStatus = taskStatus;
		this.dateFrom = dateFrom;
		this.dateTo = dateTo;
		this.toolName = toolName;
		this.sender = sender;
		this.currentPage = currentPage;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}

	public String getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(String dateFrom) {
		this.dateFrom = dateFrom;
	}

	public String getDateTo() {
		return dateTo;
	}

	public void setDateTo(String dateTo) {
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

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	/**
	 * @return the mobileNumber
	 */
	public String getMobileNumber() {
		return mobileNumber;
	}

	/**
	 * @param mobileNumber the mobileNumber to set
	 */
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}	
}
