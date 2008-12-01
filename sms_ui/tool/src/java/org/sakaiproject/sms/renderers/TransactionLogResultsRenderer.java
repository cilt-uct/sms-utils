/***********************************************************************************
 * TransactionLogResultsRenderer.java
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

public class TransactionLogResultsRenderer implements SearchResultsRenderer{

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
		
		
		UIJointContainer searchResultsTable = new UIJointContainer(tofill, divID,  "transaction-log-search-results-component:");
		
		sortHeaderRenderer.makeSortingLink(searchResultsTable, "tableheader-account-number:", sortViewParams, "accountNumber", "sms.transaction-log-search-results.account.no");
        sortHeaderRenderer.makeSortingLink(searchResultsTable, "tableheader-transaction-type", sortViewParams, "transactionType", "sms.transaction-log-search-results.trans.type");
        sortHeaderRenderer.makeSortingLink(searchResultsTable, "tableheader-transaction-date:", sortViewParams, "transactionDate", "sms.transaction-log-search-results.trans.date");
        sortHeaderRenderer.makeSortingLink(searchResultsTable, "tableheader-transaction-credits:", sortViewParams, "transactionCredits", "sms.transaction-log-search-results.trans.credits");
        sortHeaderRenderer.makeSortingLink(searchResultsTable, "tableheader-transaction-amount:", sortViewParams, "transactionCredits", "sms.transaction-log-search-results.trans.amount");
        sortHeaderRenderer.makeSortingLink(searchResultsTable, "tableheader-account-balance:", sortViewParams, "balance", "sms.transaction-log-search-results.account.balanace");
                
		//for (SmsMessage smsMessage : messageList) {
			
			//smsMessage.
			UIBranchContainer row = UIBranchContainer.make(searchResultsTable, "dataset:");
			
			UIOutput.make(row, "row-data-account-number", "1");
			UIOutput.make(row, "row-data-transaction-type", "2");
			UIOutput.make(row, "row-data-transaction-date", "3");
			UIOutput.make(row, "row-data-transaction-credits", "4");
			UIOutput.make(row, "row-data-transaction-amount", "5");
			UIOutput.make(row, "row-data-account-balance", "6");
			
		//}
		
	}


}