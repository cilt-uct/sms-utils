/***********************************************************************************
 * SearchCriteriaRenderer.java
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

import org.springframework.util.Assert;

import uk.org.ponder.rsf.components.ELReference;
import uk.org.ponder.rsf.components.UIBoundList;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIInput;
import uk.org.ponder.rsf.components.UIJointContainer;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.components.UISelect;

public class SearchCriteriaRenderer{

	private String labelID;
	private String labelDropDown;
	private String searchBeanName;
	
	public void setSearchBeanName(String searchBeanName) {
		this.searchBeanName = searchBeanName;
	}

	public void setLabelID(String labelID) {
		this.labelID = labelID;
	}

	public void setLabelDropDown(String labelDropDown) {
		this.labelDropDown = labelDropDown;
	}
	
	public void init(){
		Assert.notNull(labelID);
		Assert.notNull(labelDropDown);
		Assert.notNull(searchBeanName);
	}

	public void createSearchCriteria(UIContainer tofill, String divID, String viewID) {

		init();	
		
		UIJointContainer searchCriteria = new UIJointContainer(tofill,divID, "search-component:");
		
		UIForm searchForm =  UIForm.make(searchCriteria, "search-criteria");
		
		UIOutput.make(searchForm, "label-id", labelID);
		UIOutput.make(searchForm, "label-dropdown", labelDropDown);
	
		
		UIInput.make(searchForm, "id", createSearchELString("id"));
		UIInput.make(searchForm, "date-from", createSearchELString("dateFrom"));
		UIInput.make(searchForm, "tool-name", createSearchELString("toolName"));
		
		UISelect combo = UISelect.make(searchForm, "task-status");
		combo.selection = new UIInput();
		combo.selection.valuebinding = new ELReference(createSearchELString("taskStatus"));
		UIBoundList comboValues = new UIBoundList();
		comboValues.setValue(new String[] {"All", "Pending", "Successful", "Failed"});
		combo.optionlist = comboValues;
		UIBoundList comboNames = new UIBoundList();
		comboNames.setValue(new String[] {"All", "Pending", "Successful", "Failed"});
		combo.optionnames = comboNames;
		
		UIInput.make(searchForm, "date-to", createSearchELString("dateTo"));

		UIInput.make(searchForm, "sender", createSearchELString("sender"));
		UICommand.make(searchForm, "search", createSearchELString("fireAction"));
	}
	
	private String createSearchELString(String field){
		return "#{" + searchBeanName + "."  + field + "}";
	}
	
		
}