/***********************************************************************************
 * SmsMessageLogicImpl.java
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

import java.util.ArrayList;
import java.util.List;

import org.sakaiproject.sms.hibernate.model.BaseModel;
import org.sakaiproject.sms.hibernate.model.constants.SmsHibernateConstants;

/**
 * This class is used my the criteria search methods.
 * 
 * It is used to house the page result set that will be returned to the UI as
 * well as other attributes that will be used.
 * 
 * @author Julian Wyngaard
 * @version 1.0
 * @created 08-Dec-2008
 */
public class SearchResultContainer<T extends BaseModel> {

	/** The total result set size. */
	private Long totalResultSetSize;

	/** The page results. */
	private List<T> pageResults;

	/**
	 * Gets the page results.
	 * 
	 * @return the page results
	 */
	public List<T> getPageResults() {
		return pageResults;
	}

	/**
	 * Gets the total result set size.
	 * 
	 * @return the total result set size
	 */
	public Long getTotalResultSetSize() {
		return totalResultSetSize;
	}

	/**
	 * Get the number of pages the user can view
	 * 
	 * @return the number of pages
	 */
	public int getNumberOfPages() {
		int paratialPage = 0;
		if ((totalResultSetSize.longValue() % SmsHibernateConstants.DEFAULT_PAGE_SIZE) > 0) {
			paratialPage = 1;
		}
		int pageNumber = (int) (totalResultSetSize.longValue() / SmsHibernateConstants.DEFAULT_PAGE_SIZE)
				+ paratialPage;
		
		if(pageNumber == 0)
			return 1;
		
		return pageNumber;
	}

	/**
	 * Sets the total result set size.
	 * 
	 * @param totalResultSetSize
	 *            the new total result set size
	 */
	public void setTotalResultSetSize(Long totalResultSetSize) {
		this.totalResultSetSize = totalResultSetSize;
	}

	/**
	 * Calculate and set page results.
	 * 
	 * @param pageResults
	 *            the page results
	 * @param currentPage
	 *            the current page
	 */
	public void calculateAndSetPageResults(List<T> fullResultSet,
			Integer currentPage) {

		if (fullResultSet == null || fullResultSet.size() == 0) {
			// Make it an empty list
			pageResults = new ArrayList<T>();
			return;
		}

		int indexStart = (currentPage * SmsHibernateConstants.DEFAULT_PAGE_SIZE)
				- SmsHibernateConstants.DEFAULT_PAGE_SIZE;
		int indexEnd = indexStart + SmsHibernateConstants.DEFAULT_PAGE_SIZE;

		if (indexEnd > fullResultSet.size()) {
			indexEnd = fullResultSet.size();
		}

		pageResults = fullResultSet.subList(indexStart, indexEnd);

	}

	@Override
	public String toString() {
		StringBuffer retStr = new StringBuffer("");

		retStr.append("\n\n----------\n");
		retStr.append("Results\n");
		retStr.append("----------\n");
		retStr.append("total resultset size: ").append(totalResultSetSize)
				.append("\n");
		retStr.append("tpage result size: ").append(pageResults.size()).append(
				"\n");

		retStr.append("----------");

		return retStr.toString();
	}
}
