package org.sakaiproject.sms.producers;

public class MessageLogProducer extends AbstractSearchListProducer{

	public static final String VIEW_ID = "MessageLog";
	
	@Override
	public String getTitleMessage() {
		return "sms.view-results-message-log.name";
	}

	@Override
	public String getViewID() {
		return VIEW_ID;
	}

}
