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
