package org.sakaiproject.sms.producers;

import java.util.ArrayList;

import org.sakaiproject.sms.renderers.SearchCriteriaRender;
import org.sakaiproject.sms.renderers.SortHeaderRenderer;
import org.sakaiproject.sms.renderers.SortPagerViewParams;
import org.springframework.util.Assert;

import uk.org.ponder.rsf.components.ELReference;
import uk.org.ponder.rsf.components.UIBoundList;
import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIInput;
import uk.org.ponder.rsf.components.UIJointContainer;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.components.UISelect;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParamsReporter;

public class SearchListProducer implements ViewComponentProducer, ViewParamsReporter{

	public static final String VIEW_ID = "SearchList";
	
	private SortHeaderRenderer sortHeaderRenderer;
	private SearchCriteriaRender searchCriteriaRender;
	
	public void setSearchCriteriaRender(SearchCriteriaRender searchCriteriaRender) {
		this.searchCriteriaRender = searchCriteriaRender;
	}

	public void setSortHeaderRenderer(SortHeaderRenderer sortHeaderRenderer) {
		this.sortHeaderRenderer = sortHeaderRenderer;
	}

	public String getViewID() {
		return VIEW_ID;
	}

	public void init(){		
		Assert.notNull(sortHeaderRenderer);
		Assert.notNull(searchCriteriaRender);
	}
	
	public void fillComponents(UIContainer tofill, ViewParameters viewparams,
			ComponentChecker checker) {

		init();
		
		SortPagerViewParams sortParams = (SortPagerViewParams) viewparams;		
	
		searchCriteriaRender.fillComponents(tofill, "searchCriteria:", VIEW_ID);
		createTable(tofill, sortParams);
		exportToCSV(tofill);
	}

	private void exportToCSV(UIContainer tofill) {
		UIBranchContainer exportToCSV = UIJointContainer.make(tofill, "export:",  "search-results:");
		UIInput exportToCSVButton = UIInput.make(exportToCSV, "export-to-csv", null);
	}

	
	private void createTable(UIContainer tofill, SortPagerViewParams sortViewParams) {
		ArrayList<TestDataResultSet.TestDataRow> resultSet = TestDataResultSet.testDataSet();
		
		UIBranchContainer searchResultsTable = UIJointContainer.make(tofill, "search-results:",  "search-results:");
//		
//		sortHeaderRenderer.makeSortingLink(searchResultsTable, "tableheader-name:", sortViewParams,
//        		"name"/*SortByConstants.QUESTIONS*/, "sms.view-search-list.name");
//        sortHeaderRenderer.makeSortingLink(searchResultsTable, "tableheader-house:", sortViewParams,
//        		"house"/*SortByConstants.VIEWS*/, "sms.view-search-list.house");
//        sortHeaderRenderer.makeSortingLink(searchResultsTable, "tableheader-street:", sortViewParams,
//        		"street"/*SortByConstants.ANSWERS*/, "sms.view-search-list.street");
		
		
		for (TestDataResultSet.TestDataRow testDataRow : resultSet) {
			
			UIBranchContainer row = UIBranchContainer.make(searchResultsTable, "dataset:");
			
			UIOutput.make(row, "row-data-name", testDataRow.getName());
			UIOutput.make(row, "row-data-house", testDataRow.getHouse());
			UIOutput.make(row, "row-data-street", testDataRow.getStreet());
		}
	}

	public ViewParameters getViewParameters() {
		return new SortPagerViewParams();
	}
	
}
