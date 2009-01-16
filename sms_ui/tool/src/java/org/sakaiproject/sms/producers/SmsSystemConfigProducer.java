package org.sakaiproject.sms.producers;

import org.sakaiproject.sms.otp.SmsConfigLocator;
import org.sakaiproject.sms.otp.SmsSystemConfigLocator;

import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIInput;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.components.decorators.DecoratorList;
import uk.org.ponder.rsf.components.decorators.UITooltipDecorator;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.ViewParameters;

public class SmsSystemConfigProducer implements ViewComponentProducer{

	public static final String VIEW_ID = "SmsSystemConfig";

	private static final String DUMMY_ID = "12345";
	
	public String getViewID() {
		return VIEW_ID;
	}
	
	public void fillComponents(UIContainer tofill, ViewParameters viewparams,
			ComponentChecker checker) {
		
		String smsSystemConfigOTP = SmsSystemConfigLocator.LOCATOR_NAME + "." +  DUMMY_ID;
		
		UIMessage.make(tofill, "page-title", "sms.system.config.title");
		UIMessage.make(tofill, "page-heading", "sms.system.config.title");

		UIForm smsSystemForm = UIForm.make(tofill, "sms-system-config-form");
		
		UIMessage.make(smsSystemForm, "scheduler-interval-label", "sms.system.config.scheduler");
		UIInput schedulerInterval = UIInput.make(smsSystemForm, "sms-config-scheduler-interval", smsSystemConfigOTP +  ".schedulerInterval");
		schedulerInterval.decorators = new DecoratorList(new UITooltipDecorator(UIMessage.make("sms.system.config.scheduler-tooltip")));
		
		UIMessage.make(smsSystemForm, "gateway-report-timeout", "sms.system.config.gateway.timeout");
		UIInput reportTimeoutInput = UIInput.make(smsSystemForm, "sms-config-report-timeout", smsSystemConfigOTP + ".delReportTimeoutDuration");
		reportTimeoutInput.decorators = new DecoratorList(new UITooltipDecorator(UIMessage.make("sms.system.config.gateway.timeout-tooltip")));
		
		UICommand.make(smsSystemForm, "save", SmsSystemConfigLocator.LOCATOR_NAME + ".save");
		UICommand.make(smsSystemForm, "cancel", "#");
	}
}