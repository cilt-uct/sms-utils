package org.sakaiproject.sms.hibernate.test.dataload;

import java.util.List;

import org.sakaiproject.sms.hibernate.logic.SmsAccountLogic;
import org.sakaiproject.sms.hibernate.logic.SmsMessageLogic;
import org.sakaiproject.sms.hibernate.logic.SmsTaskLogic;
import org.sakaiproject.sms.hibernate.logic.SmsTransactionLogic;
import org.sakaiproject.sms.hibernate.logic.impl.SmsAccountLogicImpl;
import org.sakaiproject.sms.hibernate.logic.impl.SmsMessageLogicImpl;
import org.sakaiproject.sms.hibernate.logic.impl.SmsTaskLogicImpl;
import org.sakaiproject.sms.hibernate.logic.impl.SmsTransactionLogicImpl;
import org.sakaiproject.sms.hibernate.model.SmsAccount;
import org.sakaiproject.sms.hibernate.model.SmsMessage;
import org.sakaiproject.sms.hibernate.model.SmsTask;
import org.sakaiproject.sms.hibernate.model.SmsTransaction;

public class SmsSampleDataLoad{
	
	
	public static final int NUMBER_OF_REPETITIONS = 10;
	
	private SmsTransactionLogic smsTransactionLogic;
	private SmsAccountLogic smsAccountLogic;
	private SmsTaskLogic smsTaskLogic;
	private SmsMessageLogic smsMessageLogic;

	private SampleSmsTaskFactory taskFactory;
	private SampleSmsMessageFactory messageFactory;
	private SampleSmsTransactionFactory testSMSTransactionFactory;
	private SampleSmsAccountFactorty sampleSmsAccountFactorty;
	
	public static void main(String[] args) {
		SmsSampleDataLoad sampleDataLoad = new SmsSampleDataLoad();
		
		sampleDataLoad.persistSmsMessages();
		sampleDataLoad.persistSmsTransactions();
		System.out.println("Done");
	}
	
	public SmsSampleDataLoad() {
		super();
		smsTransactionLogic = new SmsTransactionLogicImpl();
		smsAccountLogic = new SmsAccountLogicImpl(); 
		smsTaskLogic = new SmsTaskLogicImpl();
		smsMessageLogic = new SmsMessageLogicImpl();
	}


	private void persistSmsTransactions() {
		testSMSTransactionFactory = new SampleSmsTransactionFactory();

		deleteSmsAccounts(smsAccountLogic);
		deleteSmsTransactions(smsTransactionLogic);

		System.out.println("Inserting SmsAccounts:");
		
		persistsSmsAccounts(smsAccountLogic);

		List<SmsAccount> persistedSmsAccounts = smsAccountLogic.getAllSmsAccounts();
		
		System.out.println("Inserting SmsTransactions:");

		
		int index = 0;
		for (int i = 0; i < NUMBER_OF_REPETITIONS; i++) {
			List<SmsTransaction> smsTransactions = testSMSTransactionFactory
			.getAllSmsTransaction();
		
			for (SmsTransaction smsTransaction : smsTransactions) {

				smsTransaction.setSmsAccount(persistedSmsAccounts.get(index));
				smsTransactionLogic.persistSmsTransaction(smsTransaction);
				index++;
			}
			testSMSTransactionFactory.refreshList();
		}
	}
	
	private void persistSmsMessages() {
		taskFactory = new SampleSmsTaskFactory();
		messageFactory = new SampleSmsMessageFactory();		
		
		deleteSmsMessages(smsMessageLogic);
		deleteSmsTasks(smsTaskLogic);
		
		System.out.println("Inserting SmsMessages and Tasks:");
		
		
		for (int i = 0; i < NUMBER_OF_REPETITIONS; i++) {

			List<SmsMessage> smsMessages = messageFactory.getAllTestSmsMessages();
			List<SmsTask> smsTasks = taskFactory.getAllTestSmsTasks();

			for (SmsTask smsTask : smsTasks) {
				smsTaskLogic.persistSmsTask(smsTask);
			}

			int index = 0;
			for (SmsMessage smsMessage : smsMessages) {
				smsMessage.setSmsTask(smsTasks.get(index));
				smsMessageLogic.persistSmsMessage(smsMessage);
				index++;
			}
		
			messageFactory.refreshList();
			taskFactory.refreshList();
		}
	}
		
	private void persistsSmsAccounts(SmsAccountLogic smsAccountLogic) {
		sampleSmsAccountFactorty = new SampleSmsAccountFactorty();

		for (int i = 0; i < NUMBER_OF_REPETITIONS; i++) {
			
			List<SmsAccount> smsAccountsToAdd = sampleSmsAccountFactorty.getAllTestSmsMessages();
			for (SmsAccount smsAccount : smsAccountsToAdd) {
				smsAccountLogic.persistSmsAccount(smsAccount);
			}
			
			sampleSmsAccountFactorty.refreshList();
		}
	}

	private  void deleteSmsAccounts(SmsAccountLogic smsAccountLogic) {
		System.out.println("Deleting SmsAccounts");
		List<SmsAccount> smsAccounts = smsAccountLogic.getAllSmsAccounts();
		
		for (SmsAccount smsAccount : smsAccounts) {
			smsAccountLogic.deleteSmsAccount(smsAccount);
		}
	}

	private  void deleteSmsTransactions(
			SmsTransactionLogic smsTransactionLogic) {
		System.out.println("Deleting SmsTransactions");
		List<SmsTransaction> smsTransactionsToDelete = smsTransactionLogic
				.getAllSmsTransactions();

		for (SmsTransaction smsTransaction : smsTransactionsToDelete) {
			smsTransactionLogic.deleteSmsTransaction(smsTransaction);
		}
	}

	private  void deleteSmsTasks(SmsTaskLogic smsTaskLogic) {
		System.out.println("Deleting SmsTasks");
		List<SmsTask> smsTasksToDelete = smsTaskLogic.getAllSmsTask();

		for (SmsTask smsTask : smsTasksToDelete) {
			smsTaskLogic.deleteSmsTask(smsTask);
		}
	}

	private  void deleteSmsMessages(SmsMessageLogic smsMessageLogic) {
		System.out.println("Deleting SmsMessages");
		List<SmsMessage> smsMessagesToDelete = smsMessageLogic
				.getAllSmsMessages();

		for (SmsMessage smsMessage : smsMessagesToDelete) {
			smsMessageLogic.deleteSmsMessage(smsMessage);
		}
	}
}
