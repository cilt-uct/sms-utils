package org.sakaiproject.sms.producers;

public class TransactionLogProducer extends AbstractSearchListProducer{

	public static final String VIEW_ID = "TransactionLog";
	
	@Override
	public String getTitleMessage() {
		return "sms.view-results-transaction-log.name";
	}

	@Override
	public String getViewID() {
		return VIEW_ID;
	}

}
