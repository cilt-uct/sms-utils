/***********************************************************************************
 * SMSConfigProducer.js
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

import uk.org.ponder.rsf.components.ELReference;
import uk.org.ponder.rsf.components.UIBoundList;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIInput;
import uk.org.ponder.rsf.components.UISelect;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.ViewParameters;

public class SMSConfigProducer implements ViewComponentProducer{

	public static final String VIEW_ID = "SMSConfig";
	
	public String getViewID() {
		return VIEW_ID;
	}

	public void fillComponents(UIContainer tofill, ViewParameters viewparams,
			ComponentChecker checker) {
		
		 UIForm smsConfigform = UIForm.make(tofill, "sms-config-form");
		 UIInput.make(smsConfigform, "sms-config-notification-email", "#{sMSConfigBean.notificatonEmail}");
		 
	
		 UISelect combo = UISelect.make(smsConfigform, "sms-config-enabled");
		 combo.selection = new UIInput();
		 combo.selection.valuebinding = new ELReference("#{sMSConfigBean.smsEnabled}");
		 UIBoundList comboValues = new UIBoundList();
		 comboValues.setValue(new String[] {"true", "false"});
		 combo.optionlist = comboValues;
		 UIBoundList comboNames = new UIBoundList();
		 comboNames.setValue(new String[] {"Yes","No"});
		 combo.optionnames = comboNames;
		 
		 
		 UICommand.make(smsConfigform, "save", "#{sMSConfigActionBean.save}");
		 
	}


}
