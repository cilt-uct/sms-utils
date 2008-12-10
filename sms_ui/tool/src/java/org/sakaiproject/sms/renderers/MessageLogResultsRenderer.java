/***********************************************************************************
 * MessageLogResultsRenderer.java
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

package org.sakaiproject.sms.renderers;

import org.sakaiproject.sms.hibernate.bean.SearchFilterBean;
import org.sakaiproject.sms.hibernate.bean.SearchResultContainer;
import org.sakaiproject.sms.hibernate.logic.SmsMessageLogic;
import org.sakaiproject.sms.hibernate.logic.impl.exception.SmsSearchException;
import org.sakaiproject.sms.hibernate.model.SmsMessage;
import org.sakaiproject.sms.util.NullHandling;
import org.springframework.util.Assert;

import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIJointContainer;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.components.UIOutput;

public class MessageLogResultsRenderer implements SearchResultsRenderer {

	private final static org.apache.log4j.Logger LOG = org.apache.log4j.Logger
			.getLogger(MessageLogResultsRenderer.class);

	private SearchFilterBean searchFilterBean;
	private SortHeaderRenderer sortHeaderRenderer;
	private SmsMessageLogic smsMessageLogic;

	public void setSmsMessageLogic(SmsMessageLogic smsMessageLogic) {
		this.smsMessageLogic = smsMessageLogic;
	}

	public void setSearchFilterBean(SearchFilterBean searchFilterBean) {
		this.searchFilterBean = searchFilterBean;
	}

	public void init() {
		sortHeaderRenderer = new SortHeaderRenderer();
		Assert.notNull(smsMessageLogic);
	}

	public void createTable(UIContainer tofill, String divID,
			SortPagerViewParams sortViewParams, String viewID) {

		init();

		searchFilterBean.setOrderBy(sortViewParams.sortBy);
		searchFilterBean.setSortDirection(sortViewParams.sortDir);
		setCurrentPage(searchFilterBean, sortViewParams);

		SearchResultContainer<SmsMessage> smsMessageList = null;
		boolean fail = false;
		try {
			if(NullHandling.safeDateCheck(searchFilterBean.getDateFrom(), searchFilterBean.getDateTo())){
				smsMessageList = smsMessageLogic.getSmsMessagesForCriteria(searchFilterBean);
				sortViewParams.current_count = smsMessageList.getNumberOfPages();
			}
			else{
				smsMessageList = new SearchResultContainer<SmsMessage>();
				sortViewParams.current_count = 1;
			}
		} catch (SmsSearchException e) {
			LOG.error(e);
			fail = true;
		}

		UIJointContainer searchResultsTable = new UIJointContainer(tofill,
				divID, "message-log-search-results-component:");
		if (fail)
			UIMessage.make(searchResultsTable, "warning", "GeneralActionError");
		else {
			sortHeaderRenderer.makeSortingLink(searchResultsTable,
					"tableheader-group:", sortViewParams,
					"smsTask.deliveryGroupName",
					"sms.message-log-search-results.account.group");
			sortHeaderRenderer.makeSortingLink(searchResultsTable,
					"tableheader-tool-name:", sortViewParams,
					"smsTask.sakaiToolName",
					"sms.message-log-search-results.account.tool.name");
			sortHeaderRenderer.makeSortingLink(searchResultsTable,
					"tableheader-sender:", sortViewParams,
					"smsTask.senderUserName",
					"sms.message-log-search-results.account.sender");
			sortHeaderRenderer.makeSortingLink(searchResultsTable,
					"tableheader-receiver:", sortViewParams,
					"smsTask.deliveryGroupName",
					"sms.message-log-search-results.account.reciver");
			sortHeaderRenderer.makeSortingLink(searchResultsTable,
					"tableheader-mobile-number:", sortViewParams,
					"mobileNumber",
					"sms.message-log-search-results.account.mobile.number");
			sortHeaderRenderer.makeSortingLink(searchResultsTable,
					"tableheader-date-processed:", sortViewParams,
					"smsTask.dateProcessed",
					"sms.message-log-search-results.account.date.processesd");
			sortHeaderRenderer.makeSortingLink(searchResultsTable,
					"tableheader-status:", sortViewParams,
					"smsTask.statusCode",
					"sms.message-log-search-results.account.Status");

			for (SmsMessage smsMessage : smsMessageList.getPageResults()) {

				UIBranchContainer row = UIBranchContainer.make(
						searchResultsTable, "dataset:");

				UIOutput.make(row, "row-data-group", smsMessage.getSmsTask()
						.getDeliveryGroupName());
				UIOutput.make(row, "row-data-tool-name", smsMessage
						.getSmsTask().getSakaiToolName());
				UIOutput.make(row, "row-data-sender", smsMessage.getSmsTask()
						.getSenderUserName());
				UIOutput.make(row, "row-data-receiver", smsMessage.getSmsTask()
						.getDeliveryGroupName());
				UIOutput.make(row, "row-data-mobile-number", smsMessage
						.getMobileNumber());
				UIOutput.make(row, "row-data-date-processed", NullHandling
						.safeToString(smsMessage.getSmsTask()
								.getDateProcessed()));
				UIOutput.make(row, "row-data-status", smsMessage
						.getStatusCode());
			}
		}
	}

	private void setCurrentPage(SearchFilterBean searchBean, SortPagerViewParams sortViewParams) {
		
		//new search
		if(searchBean.isNewSearch()){
			sortViewParams.current_start = 1;
			searchBean.setNewSearch(false);
		}
		else//paging
			searchBean.setCurrentPage(sortViewParams.current_start);	
	}

}