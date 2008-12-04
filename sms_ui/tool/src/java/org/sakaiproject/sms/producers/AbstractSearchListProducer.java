/***********************************************************************************
 * AbstractSearchListProducer.java
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
package org.sakaiproject.sms.producers;

import org.sakaiproject.sms.constants.SMSConstants;
import org.sakaiproject.sms.renderers.SearchCriteriaRenderer;
import org.sakaiproject.sms.renderers.SearchResultsRenderer;
import org.sakaiproject.sms.renderers.SortPagerViewParams;
import org.sakaiproject.sms.renderers.TablePagerRenderer;
import org.springframework.util.Assert;

import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIJointContainer;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParamsReporter;

public abstract class AbstractSearchListProducer implements ViewComponentProducer, ViewParamsReporter{
	
	private SearchCriteriaRenderer searchCriteriaRenderer;
	private SearchResultsRenderer searchResultsRenderer;
	private TablePagerRenderer tablePagerRenderer;
	
	public abstract String getViewID();
	public abstract String getTitleMessage();
	public abstract String getDefaultSortColumn();
	
	public void setSearchResultsRenderer(SearchResultsRenderer searchResultsRenderer) {
		this.searchResultsRenderer = searchResultsRenderer;
	}

	public void setSearchCriteriaRenderer(SearchCriteriaRenderer searchCriteriaRender) {
		this.searchCriteriaRenderer = searchCriteriaRender;
	}
	
	public void init(){		
		Assert.notNull(searchCriteriaRenderer);
		Assert.notNull(searchResultsRenderer);
	}
	
	public void fillComponents(UIContainer tofill, ViewParameters viewparams,
			ComponentChecker checker) {

		init();
		
		tablePagerRenderer = new TablePagerRenderer();
		
		SortPagerViewParams sortParams = (SortPagerViewParams) viewparams;		
	
		if (sortParams.sortBy == null) {
			sortParams.sortBy = getDefaultSortColumn(); // default
		}
		if (sortParams.sortDir == null) {
			sortParams.sortDir = SMSConstants.SORT_ASC; // default
		}
		
		searchCriteriaRenderer.createSearchCriteria(tofill, "searchCriteria:", getViewID());
		UIMessage.make(tofill, "table-caption", getTitleMessage());
		searchResultsRenderer.createTable(tofill, "searchResults:", sortParams, getViewID());
	
		tablePagerRenderer.createPager(tofill, "searchPager:", sortParams, getViewID());
		exportToCSV(tofill);
	}

	private void exportToCSV(UIContainer tofill) {
		UIBranchContainer exportToCSV = UIJointContainer.make(tofill, "export:",  "search-results:");
		UICommand.make(exportToCSV, "export-to-csv", "#{csvActionBean.fireAction}");
	}


	public ViewParameters getViewParameters() {
		return new SortPagerViewParams();
	}
	
}
