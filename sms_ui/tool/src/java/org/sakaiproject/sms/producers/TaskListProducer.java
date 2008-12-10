/***********************************************************************************
 * TaskListProducer.java
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

import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UICommand;

public class TaskListProducer extends AbstractSearchListProducer {

	public static final String VIEW_ID = "TaskList";

	@Override
	public String getViewID() {
		return VIEW_ID;
	}

	@Override
	public String getTitleMessage() {
		return "sms.view-results-task-list.name";
	}

	@Override
	public String getDefaultSortColumn() {
		return "dateProcessed";
	}

	@Override
	protected void addAddtionalButtons(UIBranchContainer branch) {
		UICommand.make(branch, "add-task", (String)null);
	}
	
	
}
