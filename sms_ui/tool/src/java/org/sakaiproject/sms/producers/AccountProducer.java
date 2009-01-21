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

import uk.org.ponder.beanutil.BeanGetter;
import uk.org.ponder.messageutil.TargettedMessage;
import uk.org.ponder.messageutil.TargettedMessageList;
import uk.org.ponder.rsf.components.ELReference;
import uk.org.ponder.rsf.components.UIBoundList;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIELBinding;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIInitBlock;
import uk.org.ponder.rsf.components.UIInput;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.components.UISelect;
import uk.org.ponder.rsf.evolvers.FormatAwareDateInputEvolver;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCase;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCaseReporter;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParamsReporter;
import uk.org.ponder.util.UniversalRuntimeException;

public class AccountProducer implements ViewComponentProducer,
		NavigationCaseReporter, ViewParamsReporter {

	public static final String VIEW_ID = "account";

	private FormatAwareDateInputEvolver dateEvolver;
	private TargettedMessageList messages;
	private BeanGetter ELEvaluator;

	private void createAccountEnabledBooleanSelection(String accountOTP,
			UIForm form) {

		UISelect comboBoolean = UISelect.make(form, "account-enabled");
		comboBoolean.selection = new UIInput();
		comboBoolean.selection.valuebinding = new ELReference(accountOTP
				+ ".accountEnabled");
		UIBoundList comboBoolValues = new UIBoundList();
		comboBoolValues.setValue(new String[] { "true", "false", });
		comboBoolean.optionlist = comboBoolValues;
		UIBoundList comboBoolNames = new UIBoundList();
		comboBoolNames.setValue(new String[] { "Yes", "No" });
		comboBoolean.optionnames = comboBoolNames;
	}

	public void fillComponents(UIContainer tofill, ViewParameters viewparams,
			ComponentChecker checker) {

		init();
		UIMessage.make(tofill, "page-title", "sms.sms-account.title");
		UIMessage.make(tofill, "sms-account-heading", "sms.sms-account.title");

		String accountOTP = SmsAccountLocator.LOCATOR_NAME + ".";

		boolean isNew = false;

		IdParams params = (IdParams) viewparams;
		if (params.id == null || "".equals(params.id)) {
			accountOTP += SmsUiConstants.NEW_1;
			isNew = true;
		} else {
			accountOTP += params.id;
		}

		UIForm form = UIForm.make(tofill, "account-form");

		UIMessage.make(form, "account-name-label",
				"sms.sms-account.account-name");
		UIInput.make(form, "account-name", accountOTP + ".accountName");

		UIMessage.make(form, "account-enabled-label",
				"sms.sms-account.account.enabled");

		createAccountEnabledBooleanSelection(accountOTP, form);

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

		UIMessage.make(form, "end-date-label", "sms.sms-account.end-date");

		UIInput endDate = UIInput.make(form, "endDate:", accountOTP
				+ ".enddate");

		dateEvolver.evolveDateInput(endDate);

		// to clear date input field
		if (isNew || ELEvaluator.getBean(accountOTP + ".enddate") == null) {
			UIInitBlock.make(tofill, "init-clear-date-input",
					"initClearDateInput",
					new Object[] { "endDate:1:date-field" });
		}

		UICommand cmd = UICommand.make(form, "save-btn",
				SmsAccountLocator.LOCATOR_NAME + ".saveAll");

		// This parameter will let us identify if the bean is submitted for
		// saving or if datepicker/cancel button was used
		cmd.parameters.add(new UIELBinding(accountOTP + ".messageTypeCode",
				SmsUiConstants.MESSAGE_TYPE_CODE));

		UICommand cancel = UICommand.make(form, "cancel-btn",
				UIMessage.make("sms.general.cancel")).setReturn(
				ActionResults.CANCEL);
		cancel.parameters.add(new UIELBinding(accountOTP + ".messageTypeCode",
				null));

	}

	// DataConverter still tries to bind invalid number to bean which causes
	// another message on list. This is to remove the extra messages.
	private void fixupMessages() {
		if (messages.size() > 1) {
			for (int i = 1; i < messages.size(); i++) {
				TargettedMessage message = messages.messageAt(i);
				// If the message is a UniversalRuntimeException for one of the
				// numeric fields
				if (message.exception instanceof UniversalRuntimeException
						&& (message.targetid.equals("overdraft-limit") || message.targetid
								.equals("balance"))) {
					// Remove it because an error is already registered on the
					// message list
					messages.removeMessageAt(i);
					i--;
				}
			}
		}
	}

	public String getViewID() {
		return VIEW_ID;
	}

	public ViewParameters getViewParameters() {
		return new IdParams();
	}

	public void init() {
		dateEvolver.setStyle(FormatAwareDateInputEvolver.DATE_INPUT);
		fixupMessages();
	}

	public List reportNavigationCases() {
		List<NavigationCase> list = new ArrayList<NavigationCase>();
		list.add(new NavigationCase(ActionResults.CANCEL,
				new SimpleViewParameters(BillingAdminProducer.VIEW_ID)));
		list.add(new NavigationCase(ActionResults.SUCCESS,
				new SimpleViewParameters(BillingAdminProducer.VIEW_ID)));
		return list;
	}

	public void setDateEvolver(FormatAwareDateInputEvolver dateEvolver) {
		this.dateEvolver = dateEvolver;
	}

	public void setELEvaluator(BeanGetter ELEvaluator) {
		this.ELEvaluator = ELEvaluator;
	}

	public void setMessages(TargettedMessageList messages) {
		this.messages = messages;
	}

}
