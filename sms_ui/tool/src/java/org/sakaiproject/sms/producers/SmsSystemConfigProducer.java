/***********************************************************************************
 * SmsSystemConfigProducer.java
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

import org.sakaiproject.sms.otp.SmsSystemConfigLocator;
import org.sakaiproject.sms.util.MessageFixupHelper;

import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIInput;
import uk.org.ponder.rsf.components.UIInternalLink;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.components.decorators.DecoratorList;
import uk.org.ponder.rsf.components.decorators.UITooltipDecorator;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;

public class SmsSystemConfigProducer implements ViewComponentProducer {

	public static final String VIEW_ID = "SmsSystemConfig";

	private static final String DUMMY_ID = "12345";

	private MessageFixupHelper messageFixupHelper;

	public void init() {
		messageFixupHelper.fixupMessages("sms-config-sms-credit-cost",
				"sms-config-scheduler-interval", "sms-config-sms-credit-cost");
	}

	public String getViewID() {
		return VIEW_ID;
	}

	public void setMessageFixupHelper(MessageFixupHelper messageFixupHelper) {
		this.messageFixupHelper = messageFixupHelper;
	}

	public void fillComponents(UIContainer tofill, ViewParameters viewparams,
			ComponentChecker checker) {
		init();

		String smsSystemConfigOTP = SmsSystemConfigLocator.LOCATOR_NAME + "."
				+ DUMMY_ID;

		UIMessage.make(tofill, "page-title", "sms.system.config.title");
		UIMessage.make(tofill, "page-heading", "sms.system.config.title");

		UIInternalLink.make(tofill, "site-settings", new SimpleViewParameters(
				SmsSiteConfigProducer.VIEW_ID));

		UIForm smsSystemForm = UIForm.make(tofill, "sms-system-config-form");

		UIMessage.make(smsSystemForm, "scheduler-interval-label",
				"sms.system.config.scheduler");
		UIInput schedulerInterval = UIInput.make(smsSystemForm,
				"sms-config-scheduler-interval", smsSystemConfigOTP
						+ ".schedulerInterval");
		schedulerInterval.decorators = new DecoratorList(
				new UITooltipDecorator(UIMessage
						.make("sms.system.config.scheduler-tooltip")));

		UIMessage.make(smsSystemForm, "gateway-report-timeout",
				"sms.system.config.gateway.timeout");
		UIInput reportTimeoutInput = UIInput.make(smsSystemForm,
				"sms-config-report-timeout", smsSystemConfigOTP
						+ ".delReportTimeoutDuration");
		reportTimeoutInput.decorators = new DecoratorList(
				new UITooltipDecorator(UIMessage
						.make("sms.system.config.gateway.timeout-tooltip")));

		UIMessage.make(smsSystemForm, "sms-credit-cost",
				"sms.system.config.sms.credit.cost");
		UIInput smsCreditCost = UIInput.make(smsSystemForm,
				"sms-config-sms-credit-cost", smsSystemConfigOTP
						+ ".creditCost");
		smsCreditCost.decorators = new DecoratorList(new UITooltipDecorator(
				UIMessage.make("sms.system.config.sms.credit.cost-tooltip")));

		UICommand.make(smsSystemForm, "save",
				SmsSystemConfigLocator.LOCATOR_NAME + ".save");
		UICommand.make(smsSystemForm, "cancel", "#");
	}
}