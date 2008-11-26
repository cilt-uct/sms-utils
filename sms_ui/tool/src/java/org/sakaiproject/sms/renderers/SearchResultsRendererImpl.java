package org.sakaiproject.sms.renderers;

import java.util.ArrayList;

import org.sakaiproject.sms.constants.SMSConstants;
import org.sakaiproject.sms.constants.SortDirection;
import org.sakaiproject.sms.producers.TestDataResultSet;
import org.sakaiproject.sms.util.ReflectionBasedSorter;
import org.springframework.util.Assert;

import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIJointContainer;
import uk.org.ponder.rsf.components.UIOutput;

public class SearchResultsRendererImpl implements SearchResultsRenderer {

	private SortHeaderRenderer sortHeaderRenderer;
	
	public void setSortHeaderRenderer(SortHeaderRenderer sortHeaderRenderer) {
		this.sortHeaderRenderer = sortHeaderRenderer;
	}

	public void init(){
		Assert.notNull(sortHeaderRenderer);
	}
	
	public void createTable(UIContainer tofill, String divID, SortPagerViewParams sortViewParams, String viewID) {		
		init();
		
		ArrayList<TestDataResultSet.TestDataRow> resultSet = TestDataResultSet.testDataSet();
		sortViewParams.current_count = resultSet.size()/SMSConstants.DEFAULT_PAGE_SIZE;
		resultSet = TestDataResultSet.getPage(SMSConstants.DEFAULT_PAGE_SIZE, sortViewParams.current_start, resultSet);
		SortDirection sortDirection = SortDirection.findByCode(sortViewParams.sortDir);		
		ReflectionBasedSorter.sortByName(resultSet, sortViewParams.sortBy, sortDirection);
		
		
		UIJointContainer searchResultsTable = new UIJointContainer(tofill, divID,  "search-results-component:");
		
		sortHeaderRenderer.makeSortingLink(searchResultsTable, "tableheader-name:", sortViewParams,
        		SMSConstants.SORT_BY_NAME, "sms.view-search-results.name");
        sortHeaderRenderer.makeSortingLink(searchResultsTable, "tableheader-house:", sortViewParams,
        		SMSConstants.SORT_BY_HOUSE, "sms.view-search-results.house");
        sortHeaderRenderer.makeSortingLink(searchResultsTable, "tableheader-street:", sortViewParams,
        		SMSConstants.SORT_BY_STREET, "sms.view-search-results.street");
		
        
        
       
		for (TestDataResultSet.TestDataRow testDataRow : resultSet) {
			
			UIBranchContainer row = UIBranchContainer.make(searchResultsTable, "dataset:");
			
			UIOutput.make(row, "row-data-name", testDataRow.getName());
			UIOutput.make(row, "row-data-house", testDataRow.getHouse().toString());
			UIOutput.make(row, "row-data-street", testDataRow.getStreet());
		}
	}
}
