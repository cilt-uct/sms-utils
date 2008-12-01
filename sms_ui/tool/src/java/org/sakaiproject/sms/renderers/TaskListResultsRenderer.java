package org.sakaiproject.sms.renderers;

import java.util.List;

import org.sakaiproject.sms.hibernate.bean.SearchFilterBean;
import org.sakaiproject.sms.hibernate.logic.SmsMessageLogic;
import org.sakaiproject.sms.hibernate.model.SmsMessage;
import org.springframework.util.Assert;

import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIJointContainer;
import uk.org.ponder.rsf.components.UIOutput;

public class TaskListResultsRenderer implements SearchResultsRenderer{

	private SearchFilterBean searchFilterBean;
	private SmsMessageLogic smsMessageLogic;
	private SortHeaderRenderer sortHeaderRenderer;
	
	public void setSmsMessageLogic(SmsMessageLogic smsMessageLogic) {
		this.smsMessageLogic = smsMessageLogic;
	}

	public void setSearchFilterBean(SearchFilterBean searchFilterBean) {
		this.searchFilterBean = searchFilterBean;
	}
	
	public void init(){
		sortHeaderRenderer = new SortHeaderRenderer();
		Assert.notNull(smsMessageLogic);
	}

	public void createTable(UIContainer tofill, String divID,
			SortPagerViewParams sortViewParams, String viewID) {
		
		init();
		
		searchFilterBean.setOrderBy(sortViewParams.sortBy);
		searchFilterBean.setSortDirection(sortViewParams.sortDir);
		searchFilterBean.setCurrentPage(sortViewParams.current_start);		
		
		//List<SmsMessage> messageList = smsMessageLogic.getSmsMessagesForCriteria(searchFilterBean);
		
		
		UIJointContainer searchResultsTable = new UIJointContainer(tofill, divID,  "task-search-results-component:");
		
		sortHeaderRenderer.makeSortingLink(searchResultsTable, "tableheader-group:", sortViewParams, "group", "sms.task-list-search-results.group");
        sortHeaderRenderer.makeSortingLink(searchResultsTable, "tableheader-size-estimate:", sortViewParams, "resutlSize", "sms.task-list-search-results.size.estimate");
        sortHeaderRenderer.makeSortingLink(searchResultsTable, "tableheader-size-actual:", sortViewParams, "sizeActaul", "sms.task-list-search-results.size.actual");
        sortHeaderRenderer.makeSortingLink(searchResultsTable, "tableheader-tool-name:", sortViewParams, "toolName", "sms.task-list-search-results.tool.name");
        sortHeaderRenderer.makeSortingLink(searchResultsTable, "tableheader-sender:", sortViewParams, "sender", "sms.task-list-search-results.sender");
        sortHeaderRenderer.makeSortingLink(searchResultsTable, "tableheader-message:", sortViewParams, "message", "sms.task-list-search-results.messagae");
        sortHeaderRenderer.makeSortingLink(searchResultsTable, "tableheader-process-date:", sortViewParams, "processDate", "sms.task-list-search-results.process.date");
        sortHeaderRenderer.makeSortingLink(searchResultsTable, "tableheader-status:", sortViewParams, "status", "sms.task-list-search-results.status");
        
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
			UIOutput.make(row, "row-data-status", "8");		
			
		//}
		
	}
}
