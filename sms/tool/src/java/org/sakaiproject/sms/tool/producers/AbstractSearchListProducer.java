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
package org.sakaiproject.sms.tool.producers;

import org.sakaiproject.sms.hibernate.bean.SearchFilterBean;
import org.sakaiproject.sms.hibernate.model.constants.SmsHibernateConstants;
import org.sakaiproject.sms.tool.params.DownloadReportViewParams;
import org.sakaiproject.sms.tool.params.SortPagerViewParams;
import org.sakaiproject.sms.tool.renderers.SearchCriteriaRenderer;
import org.sakaiproject.sms.tool.renderers.SearchResultsRenderer;
import org.sakaiproject.sms.tool.renderers.TablePagerRenderer;
import org.sakaiproject.sms.tool.util.SakaiDateFormat;
import org.springframework.util.Assert;

import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIInternalLink;
import uk.org.ponder.rsf.components.UIJointContainer;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCaseReporter;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParamsReporter;

public abstract class AbstractSearchListProducer implements
		ViewComponentProducer, ViewParamsReporter, NavigationCaseReporter {

	private SearchCriteriaRenderer searchCriteriaRenderer;

	private SearchResultsRenderer searchResultsRenderer;

	private TablePagerRenderer tablePagerRenderer;

	private SakaiDateFormat sakaiDateFormat;

	private SearchFilterBean searchFilterBean;

	public abstract String getViewID();

	public abstract String getTitleMessage();

	public abstract String getDefaultSortColumn();

	public void setSearchFilterBean(SearchFilterBean searchFilterBean) {
		this.searchFilterBean = searchFilterBean;
	}

	public void setSakaiDateFormat(SakaiDateFormat sakaiDateFormat) {
		this.sakaiDateFormat = sakaiDateFormat;
	}

	public void setSearchResultsRenderer(
			SearchResultsRenderer searchResultsRenderer) {
		this.searchResultsRenderer = searchResultsRenderer;
	}

	public void setSearchCriteriaRenderer(
			SearchCriteriaRenderer searchCriteriaRender) {
		this.searchCriteriaRenderer = searchCriteriaRender;
	}

	public void init() {
		Assert.notNull(searchCriteriaRenderer);
		Assert.notNull(searchResultsRenderer);
		searchResultsRenderer.setSearchFilterBean(searchFilterBean);
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
			sortParams.sortDir = SmsHibernateConstants.SORT_ASC; // default
		}

		searchCriteriaRenderer.createSearchCriteria(tofill, "searchCriteria:",
				getViewID());
		UIMessage.make(tofill, "table-caption", getTitleMessage());
		searchResultsRenderer.createTable(tofill, "searchResults:", sortParams,
				getViewID());

		tablePagerRenderer.createPager(tofill, "searchPager:", sortParams,
				getViewID(), searchResultsRenderer
						.getTotalNumberOfRowsReturned());
		UIBranchContainer branchContainer = exportToCSV(tofill);
		addAddtionalButtons(branchContainer);
	}

	/**
	 * A Extension point for implementations
	 * 
	 * @param tofill
	 */
	protected void addAddtionalButtons(UIBranchContainer branch) {

	}

	private UIBranchContainer exportToCSV(UIContainer tofill) {
		UIBranchContainer branchContainer = UIJointContainer.make(tofill,
				"export:", "search-results:");

		// Search criteria set as parameter since beans are not available when
		// the handler hook intercepts
		DownloadReportViewParams downloadReportViewParams = new DownloadReportViewParams(
				"downloadCsv", getViewID(), sakaiDateFormat
						.formatDate(searchFilterBean.getDateFrom()),
				sakaiDateFormat.formatDate(searchFilterBean.getDateTo()),
				searchFilterBean.getNumber(), searchFilterBean.getOrderBy(),
				searchFilterBean.getSender(), searchFilterBean
						.getSortDirection(), searchFilterBean.getStatus(),
				searchFilterBean.getToolName(), searchFilterBean
						.getTransactionType());
		UIInternalLink.make(tofill, "export-to-csv", downloadReportViewParams);

		return branchContainer;
	}

	public ViewParameters getViewParameters() {
		return new SortPagerViewParams();
	}

}