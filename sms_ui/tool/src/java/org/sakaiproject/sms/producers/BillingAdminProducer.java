package org.sakaiproject.sms.producers;

import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIInternalLink;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;

public class BillingAdminProducer implements ViewComponentProducer {
	public static final String VIEW_ID = "billing_admin";

	public void fillComponents(UIContainer tofill, ViewParameters viewparams,
			ComponentChecker checker) {
		UIMessage.make(tofill, "page-title", "sms.billing-admin.title");

		// TODO: Make it link to add new account screen. Perhaps a specific
		// permission must be checked?
		UIInternalLink.make(tofill, "new-account", new SimpleViewParameters(
				SmsTestProducer.VIEW_ID));

		UIMessage.make(tofill, "accounts-heading",
				"sms.billing-admin.accounts-heading");

	}

	public String getViewID() {
		return VIEW_ID;
	}
}
