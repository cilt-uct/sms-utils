package org.sakaiproject.sms.hibernate.test.dataload;

import java.util.List;

import org.sakaiproject.sms.hibernate.logic.SmsMessageLogic;
import org.sakaiproject.sms.hibernate.logic.SmsTaskLogic;
import org.sakaiproject.sms.hibernate.logic.impl.SmsMessageLogicImpl;
import org.sakaiproject.sms.hibernate.logic.impl.SmsTaskLogicImpl;
import org.sakaiproject.sms.hibernate.model.SmsMessage;
import org.sakaiproject.sms.hibernate.model.SmsTask;

public class SMSDataLoad {

	public static void main(String[] args) {
		loadSMSMessages();
	}

	private static void loadSMSMessages() {
		SmsTaskLogic smsTaskLogic = new SmsTaskLogicImpl();
		SmsMessageLogic smsMessageLogic = new SmsMessageLogicImpl();
		
		deleteSmsMessages(smsMessageLogic);
		deleteSmsTasks(smsTaskLogic);
	
		TestSMSTaskFactory taskFactory = new TestSMSTaskFactory();
		TestSMSMessageFactory messageFactory =  new TestSMSMessageFactory();
		List<SmsMessage> smsMessages = messageFactory.getAllTestSmsTasks();
		List<SmsTask> smsTasks = taskFactory.getAllTestSmsTasks();
		for (SmsTask smsTask : smsTasks) {
			smsTaskLogic.persistSmsTask(smsTask);
		}
		
		int i = 0;
		for (SmsMessage smsMessage : smsMessages) {
			smsMessage.setSmsTask(smsTasks.get(i));
			smsMessageLogic.persistSmsMessage(smsMessage);
			i++;
		}
	}

	private static void deleteSmsTasks(SmsTaskLogic smsTaskLogic) {
		List<SmsTask> smsTasks =  smsTaskLogic.getAllSmsTask();
		
		for (SmsTask smsTask : smsTasks) {
			smsTaskLogic.deleteSmsTask(smsTask);
		}
	}

	private static void deleteSmsMessages(SmsMessageLogic smsMessageLogic) {
		List<SmsMessage> smsMessages =  smsMessageLogic.getAllSmsMessages();
		
		for (SmsMessage smsMessage : smsMessages) {
			smsMessageLogic.deleteSmsMessage(smsMessage);
		}
	}
}
