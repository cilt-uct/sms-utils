package org.sakaiproject.sms.hibernate.test.dataload;

import java.util.ArrayList;
import java.util.List;

import org.sakaiproject.sms.hibernate.model.SmsAccount;

public class SampleSmsAccountFactory implements Listable {

	private List<SmsAccount> smsAccounts;
	private RandomUtils randomUtils = new RandomUtils();

	public SampleSmsAccountFactory() {
		createSampleSmsAccounts();
	}

	public Object getElementAt(int i) {
		return getTestSmsMessage(i);
	}

	public void refreshList() {
		createSampleSmsAccounts();
	}

	private void createSampleSmsAccounts() {
		smsAccounts = new ArrayList<SmsAccount>();

		SmsAccount smsAccount1 = new SmsAccount(randomUtils
				.getRandomFloat(2000), "S", randomUtils.getRandomFloat(2000),
				"SAK", "BOB001", "Account 1");
		smsAccounts.add(smsAccount1);

		SmsAccount smsAccount2 = new SmsAccount(randomUtils
				.getRandomFloat(2000), "P", randomUtils.getRandomFloat(2000),
				"MMM", "JOE001", "Account 1");
		smsAccounts.add(smsAccount2);

		SmsAccount smsAccount3 = new SmsAccount(randomUtils
				.getRandomFloat(2000), "R", randomUtils.getRandomFloat(2000),
				"JJT", "MAR001", "Account 1");
		smsAccounts.add(smsAccount3);

		SmsAccount smsAccount4 = new SmsAccount(randomUtils
				.getRandomFloat(2000), "F", randomUtils.getRandomFloat(2000),
				"RMLL", "LEE001", "Account 1");
		smsAccounts.add(smsAccount4);

		SmsAccount smsAccount5 = new SmsAccount(randomUtils
				.getRandomFloat(2000), "S", randomUtils.getRandomFloat(2000),
				"APG", "POP001", "Account 1");
		smsAccounts.add(smsAccount5);
	}

	public List<SmsAccount> getAllTestSmsMessages() {
		return smsAccounts;
	}

	public SmsAccount getTestSmsMessage(int index) {

		if (index >= smsAccounts.size())
			throw new RuntimeException("The specified index is too high");

		return smsAccounts.get(index);
	}

	public int getTotalsmsAccounts() {
		return smsAccounts.size();
	}

}
