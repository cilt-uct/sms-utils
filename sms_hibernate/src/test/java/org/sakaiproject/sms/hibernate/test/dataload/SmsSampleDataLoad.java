package org.sakaiproject.sms.hibernate.test.dataload;

import java.util.List;

import org.sakaiproject.sms.hibernate.logic.SmsAccountLogic;
import org.sakaiproject.sms.hibernate.logic.SmsMessageLogic;
import org.sakaiproject.sms.hibernate.logic.SmsTaskLogic;
import org.sakaiproject.sms.hibernate.logic.SmsTransactionLogic;
import org.sakaiproject.sms.hibernate.logic.impl.HibernateLogicFactory;
import org.sakaiproject.sms.hibernate.model.SmsAccount;
import org.sakaiproject.sms.hibernate.model.SmsMessage;
import org.sakaiproject.sms.hibernate.model.SmsTask;
import org.sakaiproject.sms.hibernate.model.SmsTransaction;

/**
 * Use the application to insert test data into the database. Increase the value
 * of NUMBER_OF_REPETITIONS if you want to test the performance of the UI,
 * especially paging in the grids.
 * 
 * 
 */
public class SmsSampleDataLoad {

	public static final int NUMBER_OF_REPETITIONS = 20;

	private SmsTransactionLogic smsTransactionLogic;

	private SampleSmsTaskFactory taskFactory;
	private SampleSmsMessageFactory messageFactory;
	private SampleSmsTransactionFactory testSMSTransactionFactory;
	private SampleSmsAccountFactory sampleSmsAccountFactorty;

	public static void main(String[] args) {
		SmsSampleDataLoad sampleDataLoad = new SmsSampleDataLoad();

		sampleDataLoad.persistSmsMessages();
		sampleDataLoad.persistSmsTransactions();
		System.out.println("Done");
	}

	public SmsSampleDataLoad() {
		super();
	}

	private void persistSmsTransactions() {

		deleteSmsAccounts(HibernateLogicFactory.getAccountLogic());
		deleteSmsTransactions(smsTransactionLogic);

		testSMSTransactionFactory = new SampleSmsTransactionFactory();
		System.out.println("Inserting SmsAccounts:");

		persistsSmsAccounts(HibernateLogicFactory.getAccountLogic());

		List<SmsAccount> persistedSmsAccounts = HibernateLogicFactory
				.getAccountLogic().getAllSmsAccounts();

		System.out.println("Inserting SmsTransactions:");
		

		int index = 0;
		for (int i = 0; i < NUMBER_OF_REPETITIONS; i++) {
			List<SmsTransaction> smsTransactions = testSMSTransactionFactory
					.getAllSmsTransaction();

			for (SmsTransaction smsTransaction : smsTransactions) {

				smsTransaction.setSmsAccount(persistedSmsAccounts.get(0));
				smsTransaction.setSmsTaskId(new Long(index +1));
				smsTransactionLogic.persistSmsTransaction(smsTransaction);
				index++;
			}
			testSMSTransactionFactory.refreshList();
		}
	}

	private void persistSmsMessages() {
		taskFactory = new SampleSmsTaskFactory();
		messageFactory = new SampleSmsMessageFactory();

		// deleteSmsMessages(smsMessageLogic);
		// deleteSmsTasks(smsTaskLogic);

		System.out.println("Inserting SmsMessages and Tasks:");

		for (int i = 0; i < NUMBER_OF_REPETITIONS; i++) {

			List<SmsMessage> smsMessages = messageFactory
					.getAllTestSmsMessages();
			List<SmsTask> smsTasks = taskFactory.getAllTestSmsTasks();

			for (SmsTask smsTask : smsTasks) {
				HibernateLogicFactory.getTaskLogic().persistSmsTask(smsTask);
			}

			int index = 0;
			for (SmsMessage smsMessage : smsMessages) {
				smsMessage.setSmsTask(smsTasks.get(index));
				HibernateLogicFactory.getMessageLogic().persistSmsMessage(
						smsMessage);
				index++;
			}

			messageFactory.refreshList();
			taskFactory.refreshList();
		}
	}

	private void persistsSmsAccounts(SmsAccountLogic smsAccountLogic) {
		sampleSmsAccountFactorty = new SampleSmsAccountFactory();

		for (int i = 0; i < 1; i++) {

			List<SmsAccount> smsAccountsToAdd = sampleSmsAccountFactorty
					.getAllTestSmsMessages();
			for (SmsAccount smsAccount : smsAccountsToAdd) {
				smsAccountLogic.persistSmsAccount(smsAccount);
			}

			sampleSmsAccountFactorty.refreshList();
		}
	}

	private void deleteSmsAccounts(SmsAccountLogic smsAccountLogic) {
		System.out.println("Deleting SmsAccounts:");
		List<SmsAccount> smsAccounts = smsAccountLogic.getAllSmsAccounts();

		for (SmsAccount smsAccount : smsAccounts) {
			smsAccountLogic.deleteSmsAccount(smsAccount);
		}
	}

	private void deleteSmsTransactions(SmsTransactionLogic smsTransactionLogic) {
		System.out.println("Deleting SmsTransactions:");
		List<SmsTransaction> smsTransactionsToDelete = smsTransactionLogic
				.getAllSmsTransactions();

		for (SmsTransaction smsTransaction : smsTransactionsToDelete) {
			smsTransactionLogic.deleteSmsTransaction(smsTransaction);
		}
	}

	private void deleteSmsTasks(SmsTaskLogic smsTaskLogic) {
		System.out.println("Deleting SmsTasks:");
		List<SmsTask> smsTasksToDelete = smsTaskLogic.getAllSmsTask();

		for (SmsTask smsTask : smsTasksToDelete) {
			smsTaskLogic.deleteSmsTask(smsTask);
		}
	}

	private void deleteSmsMessages(SmsMessageLogic smsMessageLogic) {
		System.out.println("Deleting SmsMessages:");
		List<SmsMessage> smsMessagesToDelete = smsMessageLogic
				.getAllSmsMessages();

		for (SmsMessage smsMessage : smsMessagesToDelete) {
			smsMessageLogic.deleteSmsMessage(smsMessage);
		}
	}
}
