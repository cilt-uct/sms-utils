/***********************************************************************************
 * SearchFilterBean.java
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
package org.sakaiproject.sms.hibernate.bean;

import java.util.Date;

import org.sakaiproject.sms.hibernate.model.constants.SmsHibernateConstants;


// TODO: Auto-generated Javadoc
/**
 * Generic search filter bean object. Will be used for the search screens as a
 * criteria object to house the search parameters
 * 
 * @author Julian Wyngaard
 * @version 1.0
 * @created 19-Nov-2008
 */
public class SearchFilterBean {

	/** The id. */
	private String id;

	/** The status. */
	private String status;

	/** The date from. */
	private Date dateFrom;

	/** The date to. */
	private Date dateTo;

	/** The tool name. */
	private String toolName;

	/** The sender. */
	private String sender;

	/** The current page. */
	private Integer currentPage;

	/** The order by. */
	private String orderBy;

	/** The mobile number. */
	private String mobileNumber;

	/** The account number. */
	private Long accountNumber;

	/** The transaction type. */
	private String transactionType;

	/** The sort direction. */
	private String sortDirection;

	/**
	 * Instantiates a new search filter bean.
	 */
	public SearchFilterBean() {
		super();
		dateFrom = new Date();
		dateTo = new Date();
		currentPage = new Integer(1);
	}

	/**
	 * Instantiates a new search filter bean.
	 * 
	 * @param id
	 *            the id
	 * @param status
	 *            the status
	 * @param dateFrom
	 *            the date from
	 * @param dateTo
	 *            the date to
	 * @param toolName
	 *            the tool name
	 * @param sender
	 *            the sender
	 * @param currentPage
	 *            the current page
	 * @param orderBy
	 *            the order by
	 * @param sortDirection
	 *            the sort direction
	 */
	public SearchFilterBean(String id, String status, Date dateFrom,
			Date dateTo, String toolName, String sender, Integer currentPage,
			String orderBy, String sortDirection) {
		super();
		this.id = id;
		this.status = status;
		this.dateFrom = dateFrom;
		this.dateTo = dateTo;
		this.toolName = toolName;
		this.sender = sender;
		this.currentPage = currentPage;
		this.orderBy = orderBy;
		this.sortDirection = sortDirection;
	}

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the new id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Gets the status.
	 * 
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Sets the status.
	 * 
	 * @param status
	 *            the new status
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * Gets the date from.
	 * 
	 * @return the date from
	 */
	public Date getDateFrom() {
		return dateFrom;
	}

	/**
	 * Sets the date from.
	 * 
	 * @param dateFrom
	 *            the new date from
	 */
	public void setDateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
	}

	/**
	 * Gets the date to.
	 * 
	 * @return the date to
	 */
	public Date getDateTo() {
		return dateTo;
	}

	/**
	 * Sets the date to.
	 * 
	 * @param dateTo
	 *            the new date to
	 */
	public void setDateTo(Date dateTo) {
		this.dateTo = dateTo;
	}

	/**
	 * Gets the tool name.
	 * 
	 * @return the tool name
	 */
	public String getToolName() {
		return toolName;
	}

	/**
	 * Sets the tool name.
	 * 
	 * @param toolName
	 *            the new tool name
	 */
	public void setToolName(String toolName) {
		this.toolName = toolName;
	}

	/**
	 * Gets the sender.
	 * 
	 * @return the sender
	 */
	public String getSender() {
		return sender;
	}

	/**
	 * Sets the sender.
	 * 
	 * @param sender
	 *            the new sender
	 */
	public void setSender(String sender) {
		this.sender = sender;
	}

	/**
	 * Gets the current page.
	 * 
	 * @return the current page
	 */
	public Integer getCurrentPage() {
		return currentPage;
	}

	/**
	 * Sets the current page.
	 * 
	 * @param currentPage
	 *            the new current page
	 */
	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	/**
	 * Gets the mobile number.
	 * 
	 * @return the mobileNumber
	 */
	public String getMobileNumber() {
		return mobileNumber;
	}

	/**
	 * Sets the mobile number.
	 * 
	 * @param mobileNumber
	 *            the mobileNumber to set
	 */
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	/**
	 * Sets the account number.
	 * 
	 * @param accountNumber
	 *            the new account number
	 */
	public void setAccountNumber(Long accountNumber) {
		this.accountNumber = accountNumber;
	}

	/**
	 * Gets the account number.
	 * 
	 * @return the account number
	 */
	public Long getAccountNumber() {
		return accountNumber;
	}

	/**
	 * Gets the order by.
	 * 
	 * @return the order by
	 */
	public String getOrderBy() {
		return orderBy;
	}

	/**
	 * Sets the order by.
	 * 
	 * @param orderBy
	 *            the new order by
	 */
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	/**
	 * Gets the transaction type.
	 * 
	 * @return the transaction type
	 */
	public String getTransactionType() {
		return transactionType;
	}

	/**
	 * Sets the transaction type.
	 * 
	 * @param transactionType
	 *            the new transaction type
	 */
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	/**
	 * Gets the sort direction.
	 * 
	 * @return the sort direction
	 */
	public String getSortDirection() {
		return sortDirection;
	}

	/**
	 * Sets the sort direction.
	 * 
	 * @param sortDirection
	 *            the new sort direction
	 */
	public void setSortDirection(String sortDirection) {
		this.sortDirection = sortDirection;
	}

	/**
	 * Fire action. Denotes a new search
	 * 
	 * @return true, if successful
	 */
	public boolean fireAction() {
		setCurrentPage(1);
		return true;
	}

	/**
	 * Sort asc.
	 * 
	 * @return true, if successful
	 */
	public boolean sortAsc() {
		if (sortDirection != null && !sortDirection.trim().equals("")) {
			return sortDirection.equals(SmsHibernateConstants.SORT_ASC);
		}
		// default to asc
		return true;
	}
}
