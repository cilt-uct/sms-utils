package org.sakaiproject.sms.producers;

import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIInput;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.ViewParameters;

public class SMSTaksListProducer implements ViewComponentProducer{

	public static final String VIEW_ID = "SMSConfig";
	
	public String getViewID() {
		return null;
	}

	public void fillComponents(UIContainer tofill, ViewParameters viewparams,
			ComponentChecker checker) {

		UIForm searchForm =  UIForm.make(tofill, "task-search-criteria");
		UIInput.make(searchForm, "mobileNumber", "#{SearchFilterBean.mobileNumber}");
		UICommand.make(searchForm, "save", "#{SearchFilterBean.fireAction}");
	}
	
	
	
	
	
	


}
