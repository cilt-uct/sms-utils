package org.sakaiproject.sms.producers;

import java.util.ArrayList;

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

public class SearchListProducer implements ViewComponentProducer{

	public static final String VIEW_ID = "SearchList";
	
	public String getViewID() {
		return VIEW_ID;
	}

	public void fillComponents(UIContainer tofill, ViewParameters viewparams,
			ComponentChecker checker) {

		bindSearchCriteria(tofill);
		createTable(tofill);

		UIBranchContainer exportToCSV = UIJointContainer.make(tofill, "export:",  "search-results:");
		UIInput exportToCSVButton = UIInput.make(exportToCSV, "export-to-csv", null);
	}


	private void bindSearchCriteria(UIContainer tofill) {
		UIForm searchForm =  UIForm.make(tofill, "search-criteria");
		
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
	
	private void createTable(UIContainer tofill) {
		ArrayList<TestDataResultSet.TestDataRow> resultSet = TestDataResultSet.testDataSet();
		
		UIBranchContainer jointContainer = UIJointContainer.make(tofill, "search-results:",  "search-results:");
		
		for (TestDataResultSet.TestDataRow testDataRow : resultSet) {
			
			UIBranchContainer row = UIBranchContainer.make(jointContainer, "dataset:");
			
			UIOutput.make(row, "row-data-name", testDataRow.getName());
			UIOutput.make(row, "row-data-house", testDataRow.getHouse());
			UIOutput.make(row, "row-data-street", testDataRow.getStreet());
		}
	}
	
}
