/***********************************************************************************
 * HelperProducer.java
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

import static org.sakaiproject.sms.constants.SmsUiConstants.MAX_SMS_LENGTH;

import java.util.ArrayList;
import java.util.List;

import org.sakaiproject.sms.beans.ActionResults;
import org.sakaiproject.sms.otp.SmsTaskLocator;

import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIInitBlock;
import uk.org.ponder.rsf.components.UIInput;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.components.decorators.UIDisabledDecorator;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCase;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCaseReporter;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;

public class HelperProducer implements ViewComponentProducer,
		NavigationCaseReporter {

	public static final String VIEW_ID = "helper";

	public void fillComponents(UIContainer tofill, ViewParameters viewparams,
			ComponentChecker checker) {

		String smsTaskOTP = SmsTaskLocator.LOCATOR_NAME + "."
				+ SmsTaskLocator.NEW_1;

		UIMessage.make(tofill, "page-title", "sms.helper.title");
		UIMessage.make(tofill, "sms-helper-heading", "sms.helper.heading");
		UIForm form = UIForm.make(tofill, "helper-form");

		UIMessage.make(form, "message-body-label", "sms.helper.message-body");
		UIInput messageBody = UIInput.make(form, "message-body", smsTaskOTP
				+ ".messageBody");
		messageBody.mustapply = true; // Force to apply

		UIMessage.make(form, "chars-remaining-label",
				"sms.helper.chars-remaining");
		UIInput charsRemaining = UIInput.make(form, "chars-remaining", null,
				Integer.toString(MAX_SMS_LENGTH));
		// Disables the characters remaining input
		charsRemaining.decorate(new UIDisabledDecorator());

		// TODO: Continue / Send button
		UICommand.make(form, "action-button", UIMessage
				.make("sms.general.send"));
		UICommand.make(form, "cancel-button",
				UIMessage.make("sms.general.cancel")).setReturn(
				ActionResults.CANCEL);

		UIMessage.make(form, "estimated-group-size-label",
				"sms.helper.estimated-group-size");
		UIInput groupSize = UIInput.make(form, "estimated-group-size",
				smsTaskOTP + ".groupSizeEstimate");
		groupSize.decorate(new UIDisabledDecorator());
		groupSize.fossilize = false;

		UIMessage.make(form, "account-balance-label",
				"sms.helper.account-balance");
		UIInput accountBalance = UIInput.make(form, "account-balance", null,
				"todo");
		accountBalance.decorate(new UIDisabledDecorator());
		accountBalance.fossilize = false;

		UIMessage.make(form, "estimated-cost-label",
				"sms.helper.estimated-cost");
		UIInput estimatedCost = UIInput.make(form, "estimated-cost", smsTaskOTP
				+ ".creditEstimate");
		estimatedCost.decorate(new UIDisabledDecorator());
		estimatedCost.fossilize = false;

		UIInitBlock.make(tofill, "init-msg-body-change", "initMsgBodyChange",
				new Object[] { messageBody, charsRemaining,
						Integer.toString(MAX_SMS_LENGTH) });

	}

	public String getViewID() {
		return VIEW_ID;
	}

	/**
	 * @see NavigationCaseReporter#reportNavigationCases()
	 */
	public List reportNavigationCases() {
		List<NavigationCase> list = new ArrayList<NavigationCase>();
		list.add(new NavigationCase(ActionResults.CANCEL,
				new SimpleViewParameters(SmsTestProducer.VIEW_ID)));
		return list;
	}

}
