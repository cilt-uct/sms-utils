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

import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIJointContainer;
import uk.org.ponder.rsf.components.UIOutput;

public class MessageLogResultsRenderer implements SearchResultsRenderer {

	private SearchFilterBean searchFilterBean;
	private SortHeaderRenderer sortHeaderRenderer;
	

	public void setSearchFilterBean(SearchFilterBean searchFilterBean) {
		this.searchFilterBean = searchFilterBean;
	}
	
	public void init(){
		sortHeaderRenderer = new SortHeaderRenderer();
	}

	public void createTable(UIContainer tofill, String divID,
			SortPagerViewParams sortViewParams, String viewID) {
		
		init();
		
		searchFilterBean.setOrderBy(sortViewParams.sortBy);
		searchFilterBean.setSortDirection(sortViewParams.sortDir);
		searchFilterBean.setCurrentPage(sortViewParams.current_start);		
		
		//List<SmsMessage> messageList = smsMessageLogic.getSmsMessagesForCriteria(searchFilterBean);
		
		
		UIJointContainer searchResultsTable = new UIJointContainer(tofill, divID,  "message-log-search-results-component:");
		
		sortHeaderRenderer.makeSortingLink(searchResultsTable, "tableheader-group:", sortViewParams, "group", "sms.message-log-search-results.account.group");
        sortHeaderRenderer.makeSortingLink(searchResultsTable, "tableheader-tool-name:", sortViewParams, "resutlSize", "sms.message-log-search-results.account.tool.name");
        sortHeaderRenderer.makeSortingLink(searchResultsTable, "tableheader-sender:", sortViewParams, "sizeActaul", "sms.message-log-search-results.account.sender");
        sortHeaderRenderer.makeSortingLink(searchResultsTable, "tableheader-receiver:", sortViewParams, "toolName", "sms.message-log-search-results.account.reciver");
        sortHeaderRenderer.makeSortingLink(searchResultsTable, "tableheader-mobile-number:", sortViewParams, "sender", "sms.message-log-search-results.account.mobile.number");
        sortHeaderRenderer.makeSortingLink(searchResultsTable, "tableheader-date-processed:", sortViewParams, "message", "sms.message-log-search-results.account.date.processesd");
        sortHeaderRenderer.makeSortingLink(searchResultsTable, "tableheader-status:", sortViewParams, "processDate", "sms.message-log-search-results.account.Status");
        
		//for (SmsMessage smsMessage : messageList) {
			
			//smsMessage.
			UIBranchContainer row = UIBranchContainer.make(searchResultsTable, "dataset:");
			
			UIOutput.make(row, "row-data-group", "1");
			UIOutput.make(row, "row-data-size-estimate", "2");
			UIOutput.make(row, "row-data-size-actual", "3");
			UIOutput.make(row, "row-data-tool-name", "4");
			UIOutput.make(row, "row-data-sender", "5");
			UIOutput.make(row, "row-data-message", "6");
			UIOutput.make(row, "row-data-process-date", "7");
			
		//}
		
	}

}
