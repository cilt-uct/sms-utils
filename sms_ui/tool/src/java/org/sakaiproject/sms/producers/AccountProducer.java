/***********************************************************************************
 * AccountProducer.java
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

import java.util.ArrayList;
import java.util.List;

import org.sakaiproject.sms.beans.ActionResults;
import org.sakaiproject.sms.constants.SmsUiConstants;
import org.sakaiproject.sms.otp.SmsAccountLocator;
import org.sakaiproject.sms.params.IdParams;

import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIELBinding;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIInput;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.evolvers.FormatAwareDateInputEvolver;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCase;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCaseReporter;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParamsReporter;

public class AccountProducer implements ViewComponentProducer,
		NavigationCaseReporter, ViewParamsReporter {

	public static final String VIEW_ID = "account";
	
private FormatAwareDateInputEvolver dateEvolver;
	
	public void setDateEvolver(FormatAwareDateInputEvolver dateEvolver) {
		this.dateEvolver = dateEvolver;
	}
	
	public void init(){
		dateEvolver.setStyle(FormatAwareDateInputEvolver.DATE_INPUT);
	}
	
	public void fillComponents(UIContainer tofill, ViewParameters viewparams,
			ComponentChecker checker) {

		init();
		UIMessage.make(tofill, "page-title", "sms.sms-account.title");
		UIMessage.make(tofill, "sms-account-heading", "sms.sms-account.title");

		String accountOTP = SmsAccountLocator.LOCATOR_NAME + ".";

		IdParams params = (IdParams) viewparams;
		if (params.id == null || "".equals(params.id)) {
			accountOTP += SmsUiConstants.NEW_1;
		} else {
			accountOTP += params.id;
		}

		UIForm form = UIForm.make(tofill, "account-form");

		UIMessage.make(form, "account-name-label",
				"sms.sms-account.account-name");
		UIInput.make(form, "account-name", accountOTP
				+ ".accountName");

		UIMessage.make(form, "sakai-site-id-label",
				"sms.sms-account.sakai-site-id");
		UIInput.make(form, "sakai-site-id", accountOTP + ".sakaiSiteId");
		UIMessage.make(form, "sakai-user-id-label",
				"sms.sms-account.sakai-user-id");
		UIInput.make(form, "sakai-user-id", accountOTP + ".sakaiUserId");
		UIMessage.make(form, "overdraft-limit-label",
				"sms.sms-account.overdraft-limit");
		UIInput.make(form, "overdraft-limit", accountOTP + ".overdraftLimit");
		UIMessage.make(form, "balance-label", "sms.sms-account.balance");
		UIInput.make(form, "balance", accountOTP + ".balance");
		
		UIMessage.make(form, "end-date-label", "sms.sms-account.end.date");
		
		UIInput dateTo = UIInput.make(form, "date-to:", accountOTP + ".enddate");
		dateEvolver.evolveDateInput(dateTo);

		form.addParameter(new UIELBinding(accountOTP + ".messageTypeCode",
				SmsUiConstants.MESSAGE_TYPE_CODE));

		UICommand.make(form, "save-btn", SmsAccountLocator.LOCATOR_NAME
				+ ".saveAll");
		UICommand
				.make(form, "cancel-btn", UIMessage.make("sms.general.cancel"))
				.setReturn(ActionResults.CANCEL);

	}

	public String getViewID() {
		return VIEW_ID;
	}

	public ViewParameters getViewParameters() {
		return new IdParams();
	}

	public List reportNavigationCases() {
		List<NavigationCase> list = new ArrayList<NavigationCase>();
		list.add(new NavigationCase(ActionResults.CANCEL,
				new SimpleViewParameters(BillingAdminProducer.VIEW_ID)));
		list.add(new NavigationCase(ActionResults.SUCCESS,
				new SimpleViewParameters(BillingAdminProducer.VIEW_ID)));
		return list;
	}

}
