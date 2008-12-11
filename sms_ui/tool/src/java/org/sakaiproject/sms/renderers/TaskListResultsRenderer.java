/***********************************************************************************
 * TaskListResultsRenderer.java
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

import java.util.ArrayList;
import java.util.List;

import org.sakaiproject.sms.hibernate.bean.SearchFilterBean;
import org.sakaiproject.sms.hibernate.logic.SmsTaskLogic;
import org.sakaiproject.sms.hibernate.logic.impl.exception.SmsSearchException;
import org.sakaiproject.sms.hibernate.model.SmsTask;
import org.sakaiproject.sms.util.NullHandling;
import org.springframework.util.Assert;

import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIJointContainer;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.components.UIOutput;

public class TaskListResultsRenderer implements SearchResultsRenderer {

	private final static org.apache.log4j.Logger LOG = org.apache.log4j.Logger
			.getLogger(TaskListResultsRenderer.class);

	private List<SmsTask> smsTaskList = new ArrayList<SmsTask>();

	private SearchFilterBean searchFilterBean;
	private SortHeaderRenderer sortHeaderRenderer;
	private SmsTaskLogic smsTaskLogic;

	public void setSmsTaskLogic(SmsTaskLogic smsTaskLogic) {
		this.smsTaskLogic = smsTaskLogic;
	}

	public void setSearchFilterBean(SearchFilterBean searchFilterBean) {
		this.searchFilterBean = searchFilterBean;
	}

	public void init() {
		sortHeaderRenderer = new SortHeaderRenderer();
		Assert.notNull(smsTaskLogic);
	}

	public void createTable(UIContainer tofill, String divID,
			SortPagerViewParams sortViewParams, String viewID) {

		init();

		searchFilterBean.setOrderBy(sortViewParams.sortBy);
		searchFilterBean.setSortDirection(sortViewParams.sortDir);
		searchFilterBean.setCurrentPage(sortViewParams.current_start);

		boolean fail = false;
		try {
			if (NullHandling.safeDateCheck(searchFilterBean.getDateFrom(),
					searchFilterBean.getDateTo())) {
				smsTaskList = smsTaskLogic.getSmsTasksForCriteria(
						searchFilterBean).getPageResults();
			} else {
				smsTaskList = new ArrayList<SmsTask>();
			}
		} catch (SmsSearchException e) {
			LOG.error(e);
			fail = true;
		}

		UIJointContainer searchResultsTable = new UIJointContainer(tofill,
				divID, "task-search-results-component:");

		if (fail)
			UIMessage.make(searchResultsTable, "warning", "GeneralActionError");
		else {
			sortHeaderRenderer.makeSortingLink(searchResultsTable,
					"tableheader-group:", sortViewParams, "deliveryGroupName",
					"sms.task-list-search-results.group");
			sortHeaderRenderer.makeSortingLink(searchResultsTable,
					"tableheader-size-estimate:", sortViewParams,
					"groupSizeEstimate",
					"sms.task-list-search-results.size.estimate");
			sortHeaderRenderer.makeSortingLink(searchResultsTable,
					"tableheader-size-actual:", sortViewParams,
					"groupSizeActual",
					"sms.task-list-search-results.size.actual");
			sortHeaderRenderer.makeSortingLink(searchResultsTable,
					"tableheader-tool-name:", sortViewParams, "sakaiToolName",
					"sms.task-list-search-results.tool.name");
			sortHeaderRenderer.makeSortingLink(searchResultsTable,
					"tableheader-sender:", sortViewParams, "senderUserName",
					"sms.task-list-search-results.sender");
			sortHeaderRenderer.makeSortingLink(searchResultsTable,
					"tableheader-message:", sortViewParams, "messageBody",
					"sms.task-list-search-results.messagae");
			sortHeaderRenderer.makeSortingLink(searchResultsTable,
					"tableheader-process-date:", sortViewParams,
					"dateProcessed",
					"sms.task-list-search-results.process.date");
			sortHeaderRenderer.makeSortingLink(searchResultsTable,
					"tableheader-status:", sortViewParams, "statusCode",
					"sms.task-list-search-results.status");

			for (SmsTask smsTask : smsTaskList) {

				UIBranchContainer row = UIBranchContainer.make(
						searchResultsTable, "dataset:");

				UIOutput.make(row, "row-data-group", smsTask
						.getDeliveryGroupName());
				UIOutput.make(row, "row-data-size-estimate", NullHandling
						.safeToString(smsTask.getGroupSizeEstimate()));
				UIOutput.make(row, "row-data-size-actual", NullHandling
						.safeToString(smsTask.getGroupSizeActual()));
				UIOutput.make(row, "row-data-tool-name", smsTask
						.getSakaiToolName());
				UIOutput.make(row, "row-data-sender", smsTask
						.getSenderUserName());
				UIOutput
						.make(row, "row-data-message", smsTask.getMessageBody());
				UIOutput.make(row, "row-data-process-date", NullHandling
						.safeToString(smsTask.getDateProcessed()));
				UIOutput.make(row, "row-data-status", smsTask.getStatusCode());
			}
		}
	}

	public int getNumberOfRowsDisplayed() {
		return smsTaskList.size();
	}
}
