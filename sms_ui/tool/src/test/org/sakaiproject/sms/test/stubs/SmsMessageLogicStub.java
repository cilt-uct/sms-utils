package org.sakaiproject.sms.test.stubs;

import java.sql.Timestamp;
import java.util.List;

import org.sakaiproject.sms.hibernate.bean.SearchFilterBean;
import org.sakaiproject.sms.hibernate.bean.SearchResultContainer;
import org.sakaiproject.sms.hibernate.logic.SmsMessageLogic;
import org.sakaiproject.sms.hibernate.logic.impl.exception.SmsSearchException;
import org.sakaiproject.sms.hibernate.model.SmsMessage;
import org.sakaiproject.sms.hibernate.model.SmsTask;
import org.sakaiproject.sms.hibernate.model.constants.SmsConst_DeliveryStatus;

public class SmsMessageLogicStub implements SmsMessageLogic {

	public void deleteSmsMessage(SmsMessage arg0) {
		// TODO Auto-generated method stub

	}

	public List<SmsMessage> getAllSmsMessages() {
		// TODO Auto-generated method stub
		return null;
	}

	public Integer getBillableMessagesCount(String arg0, String arg1,
			String arg2, Integer arg3) {
		// TODO Auto-generated method stub
		return null;
	}

	public SmsMessage getNewTestSmsMessageInstance(String mobileNumber,
			String messageBody) {
		SmsTask smsTask = new SmsTask();
		smsTask.setSakaiSiteId("sakaiSiteId");
		smsTask.setSmsAccountId(1);
		smsTask.setDateCreated(new Timestamp(System.currentTimeMillis()));
		smsTask.setDateToSend(new Timestamp(System.currentTimeMillis()));
		smsTask.setStatusCode(SmsConst_DeliveryStatus.STATUS_PENDING);
		smsTask.setAttemptCount(2);
		smsTask.setMessageBody("messageBody");
		smsTask.setSenderUserName("senderUserName");

		SmsMessage smsMessage = new SmsMessage();
		smsMessage.setSmsTask(smsTask);
		smsMessage.setMobileNumber(mobileNumber);
		smsMessage.setMessageBody(messageBody);
		smsMessage.setSmscMessageId("smscMessageId");
		smsMessage.setSakaiUserId("sakaiUserId");
		smsMessage.setStatusCode(SmsConst_DeliveryStatus.STATUS_PENDING);

		return smsMessage;
	}

	public SmsMessage getSmsMessage(Long arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public SmsMessage getSmsMessageBySmscMessageId(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public SearchResultContainer<SmsMessage> getSmsMessagesForCriteria(
			SearchFilterBean arg0) throws SmsSearchException {
		// TODO Auto-generated method stub
		return null;
	}

	public List<SmsMessage> getSmsMessagesWithStatus(Long arg0, String... arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	public void persistSmsMessage(SmsMessage arg0) {
		// TODO Auto-generated method stub

	}

}
