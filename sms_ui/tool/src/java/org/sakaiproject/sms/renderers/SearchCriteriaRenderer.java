package org.sakaiproject.sms.renderers;

import uk.org.ponder.rsf.components.ELReference;
import uk.org.ponder.rsf.components.UIBoundList;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIInput;
import uk.org.ponder.rsf.components.UIJointContainer;
import uk.org.ponder.rsf.components.UISelect;

public class SearchCriteriaRenderer{

	public void createSearchCriteria(UIContainer tofill, String divID, String viewID) {

		
		UIJointContainer searchCriteria = new UIJointContainer(tofill,divID, "search-component:");
		
		UIForm searchForm =  UIForm.make(searchCriteria, "search-criteria");
		
		UIInput.make(searchForm, "id", "#{searchFilterBean.id}");
		UIInput.make(searchForm, "date-from", "#{searchFilterBean.dateFrom}");
		UIInput.make(searchForm, "tool-name", "#{searchFilterBean.toolName}");
		
		UISelect combo = UISelect.make(searchForm, "task-status");
		combo.selection = new UIInput();
		combo.selection.valuebinding = new ELReference("#{searchFilterBean.taskStatus}");
		UIBoundList comboValues = new UIBoundList();
		comboValues.setValue(new String[] {"All", "Pending", "Successful", "Failed"});
		combo.optionlist = comboValues;
		UIBoundList comboNames = new UIBoundList();
		comboNames.setValue(new String[] {"All", "Pending", "Successful", "Failed"});
		combo.optionnames = comboNames;
		
		UIInput.make(searchForm, "date-to", "#{searchFilterBean.dateTo}");

		UIInput.make(searchForm, "sender", "#{searchFilterBean.sender}");
		UICommand.make(searchForm, "search", "#{searchFilterBean.fireAction}");
	}
	
	
		
}