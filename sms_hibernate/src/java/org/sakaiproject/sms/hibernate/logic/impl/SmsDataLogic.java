package org.sakaiproject.sms.hibernate.logic.impl;

import java.util.List;

import org.sakaiproject.sms.hibernate.logic.HibernateDaoResource;
import org.sakaiproject.sms.hibernate.model.SmsMessage;
import org.sakaiproject.sms.hibernate.model.SmsTask;

public class SmsDataLogic extends HibernateDaoResource implements
		org.sakaiproject.sms.hibernate.logic.SmsDataLogic {

	public SmsDataLogic() {
	}

	public void getSmsAccount(int smsAccountID) {

	}

	public void getSmsConfig() {

	}

	public SmsMessage getSmsMessage(long smsMessageID) {
		return null;
	}

	public SmsMessage[] getSmsMessages(long smsTaskID) {
		return null;
	}

	public SmsTask getSmsTask(long smsTaskID) {
		return null;
	}

	public SmsTask[] getSmsTasks(String sakaiSiteID, String statusCode) {
		return null;
	}

	public void getSmsTransaction(int smsTransactionID) {

	}

	public List getSmsTransactions(int smsAccountID) {
		return null;
	}

	public boolean updateMessageStatus(long smsMessageID) {
		return true;
	}

	public boolean updateMessageStatus(String smscMessageID) {
		return true;
	}

	public boolean updateTaskStatus(long smsTaskID) {
		return true;
	}
}
