package org.sakaiproject.sms.hibernate.test.dataload;

import java.util.List;

import org.sakaiproject.sms.hibernate.logic.SmsMessageLogic;
import org.sakaiproject.sms.hibernate.logic.SmsTaskLogic;
import org.sakaiproject.sms.hibernate.logic.SmsTransactionLogic;
import org.sakaiproject.sms.hibernate.logic.impl.SmsMessageLogicImpl;
import org.sakaiproject.sms.hibernate.logic.impl.SmsTaskLogicImpl;
import org.sakaiproject.sms.hibernate.logic.impl.SmsTransactionLogicImpl;
import org.sakaiproject.sms.hibernate.model.SmsMessage;
import org.sakaiproject.sms.hibernate.model.SmsTask;
import org.sakaiproject.sms.hibernate.model.SmsTransaction;

public class SmsSampleDataLoad {

	public static void main(String[] args) {
		loadSmsMessages();
		loadSmsTransactions();
	}

	private static void loadSmsTransactions() {
		SmsTransactionLogic smsTransactionLogic = new SmsTransactionLogicImpl();
		
		deleteSmsTransactions(smsTransactionLogic);
		
		SampleSmsTransactionFactory testSMSTransactionFactory = new SampleSmsTransactionFactory();
		
		List<SmsTransaction> smsTransactions = testSMSTransactionFactory.getAllSmsTransaction();
		for (SmsTransaction smsTransaction : smsTransactions) {
			smsTransactionLogic.persistSmsTransaction(smsTransaction);
		}
	}


	private static void loadSmsMessages() {
		SmsTaskLogic smsTaskLogic = new SmsTaskLogicImpl();
		SmsMessageLogic smsMessageLogic = new SmsMessageLogicImpl();
		
		deleteSmsMessages(smsMessageLogic);
		deleteSmsTasks(smsTaskLogic);
	
		SampleSmsTaskFactory taskFactory = new SampleSmsTaskFactory();
		SampleSmsMessageFactory messageFactory =  new SampleSmsMessageFactory();
		List<SmsMessage> smsMessages = messageFactory.getAllTestSmsMessages();
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

	private static void deleteSmsTransactions(
			SmsTransactionLogic smsTransactionLogic) {
		List<SmsTransaction> smsTransactionsToDelete = smsTransactionLogic.getAllSmsTransactions();
		
		for (SmsTransaction smsTransaction : smsTransactionsToDelete) {
			smsTransactionLogic.deleteSmsTransaction(smsTransaction);
		}
	}

	private static void deleteSmsTasks(SmsTaskLogic smsTaskLogic) {
		List<SmsTask> smsTasksToDelete =  smsTaskLogic.getAllSmsTask();
		
		for (SmsTask smsTask : smsTasksToDelete) {
			smsTaskLogic.deleteSmsTask(smsTask);
		}
	}

	private static void deleteSmsMessages(SmsMessageLogic smsMessageLogic) {
		List<SmsMessage> smsMessagesToDelete =  smsMessageLogic.getAllSmsMessages();
		
		for (SmsMessage smsMessage : smsMessagesToDelete) {
			smsMessageLogic.deleteSmsMessage(smsMessage);
		}
	}
}
