package org.sakaiproject.sms.hibernate.test.dataload;

import java.util.ArrayList;
import java.util.List;

import org.sakaiproject.sms.hibernate.model.SmsTransaction;

public class SampleSmsTransactionFactory implements Listable{

	private List<SmsTransaction> smsTransactionList;
	private RandomUtils randomUtils = new RandomUtils();

	public SampleSmsTransactionFactory() {
		createSampleSmsTransactions();
	}

	public Object getElementAt(int i) {
		return getSmsTransaction(i);
	}
	
	public void refreshList() {
		createSampleSmsTransactions();
	}

	private void createSampleSmsTransactions() {
		smsTransactionList = new ArrayList<SmsTransaction>();

		SmsTransaction smsTransaction1 = new SmsTransaction(randomUtils.getRandomFloat(1000),
				"JOE001", randomUtils.getRandomFloat(1000), randomUtils.getRandomInteger(1000), randomUtils.getBoundRandomDate(2008), "TC");
		smsTransactionList.add(smsTransaction1);
		SmsTransaction smsTransaction2 = new SmsTransaction(randomUtils.getRandomFloat(1000),
				"BOB001", randomUtils.getRandomFloat(1000), randomUtils.getRandomInteger(1000), randomUtils.getBoundRandomDate(2008), "TC");
		smsTransactionList.add(smsTransaction2);
		SmsTransaction smsTransaction3 = new SmsTransaction(randomUtils.getRandomFloat(1000),
				"GAM003", randomUtils.getRandomFloat(1000), randomUtils.getRandomInteger(1000), randomUtils.getBoundRandomDate(2008), "TC");
		smsTransactionList.add(smsTransaction3);
		SmsTransaction smsTransaction4 = new SmsTransaction(randomUtils.getRandomFloat(1000),
				"MAR008", randomUtils.getRandomFloat(1000), randomUtils.getRandomInteger(1000), randomUtils.getBoundRandomDate(2008), "TC");
		smsTransactionList.add(smsTransaction4);
		SmsTransaction smsTransaction5 = new SmsTransaction(randomUtils.getRandomFloat(1000),
				"CCL005", randomUtils.getRandomFloat(1000), randomUtils.getRandomInteger(1000), randomUtils.getBoundRandomDate(2008), "TC");
		smsTransactionList.add(smsTransaction5);
	}

	public List<SmsTransaction> getAllSmsTransaction() {
		return smsTransactionList;
	}

	public SmsTransaction getSmsTransaction(int index) {

		if (index >= smsTransactionList.size())
			throw new RuntimeException("The specified index is too high");

		return smsTransactionList.get(index);
	}

	public int getTotalSmsTransactionList() {
		return smsTransactionList.size();
	}

}
