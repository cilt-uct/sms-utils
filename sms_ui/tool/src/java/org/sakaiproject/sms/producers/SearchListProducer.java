package org.sakaiproject.sms.producers;

import org.sakaiproject.sms.constants.SMSConstants;
import org.sakaiproject.sms.renderers.SearchCriteriaRenderer;
import org.sakaiproject.sms.renderers.SearchResultsRenderer;
import org.sakaiproject.sms.renderers.SortPagerViewParams;
import org.sakaiproject.sms.renderers.TablePagerRenderer;
import org.springframework.util.Assert;

import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIInput;
import uk.org.ponder.rsf.components.UIJointContainer;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParamsReporter;

public class SearchListProducer implements ViewComponentProducer, ViewParamsReporter{

	public static final String VIEW_ID = "SearchList";
	
	private SearchCriteriaRenderer searchCriteriaRenderer;
	private SearchResultsRenderer searchResultsRenderer;
	private TablePagerRenderer tablePagerRenderer;
	private String titleMessage;
	
	public void setTablePagerRenderer(TablePagerRenderer tablePagerRenderer) {
		this.tablePagerRenderer = tablePagerRenderer;
	}

	public void setSearchResultsRenderer(SearchResultsRenderer searchResultsRenderer) {
		this.searchResultsRenderer = searchResultsRenderer;
	}

	public void setSearchCriteriaRenderer(SearchCriteriaRenderer searchCriteriaRender) {
		this.searchCriteriaRenderer = searchCriteriaRender;
	}
	
	public void setTitleMessage(){
		
	}

	public String getViewID() {
		return VIEW_ID;
	}

	public void init(){		
		Assert.notNull(searchCriteriaRenderer);
		Assert.notNull(searchResultsRenderer);
		Assert.notNull(tablePagerRenderer);
		Assert.notNull(titleMessage);
	}
	
	public void fillComponents(UIContainer tofill, ViewParameters viewparams,
			ComponentChecker checker) {

		init();
		
		
		SortPagerViewParams sortParams = (SortPagerViewParams) viewparams;		
	
		if (sortParams.sortBy == null) {
			sortParams.sortBy = SMSConstants.SORT_BY_NAME; // default
		}
		if (sortParams.sortDir == null) {
			sortParams.sortDir = SMSConstants.SORT_DESC; // default
		}
		
		searchCriteriaRenderer.createSearchCriteria(tofill, "searchCriteria:", VIEW_ID);
		UIMessage.make(tofill, "table-caption", "sms.view-search-task-list.name");
		searchResultsRenderer.createTable(tofill, "searchResults:", sortParams, VIEW_ID);
		tablePagerRenderer.createPager(tofill, "searchPager:", sortParams, VIEW_ID);
		exportToCSV(tofill);
	}

	private void exportToCSV(UIContainer tofill) {
		UIBranchContainer exportToCSV = UIJointContainer.make(tofill, "export:",  "search-results:");
		UIInput exportToCSVButton = UIInput.make(exportToCSV, "export-to-csv", null);
	}

	

	public ViewParameters getViewParameters() {
		return new SortPagerViewParams();
	}
	
}
