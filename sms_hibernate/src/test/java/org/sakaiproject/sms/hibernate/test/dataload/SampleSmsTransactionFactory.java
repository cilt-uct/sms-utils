package org.sakaiproject.sms.hibernate.test.dataload;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.sakaiproject.sms.hibernate.model.SmsTransaction;

public class SampleSmsTransactionFactory {

	private List<SmsTransaction> smsTransactionList;

	public SampleSmsTransactionFactory() {
		createSampleSmsTransactions(); 
	}

	private void createSampleSmsTransactions() {
		smsTransactionList = new ArrayList<SmsTransaction>();
		
		SmsTransaction smsTransaction1 = new SmsTransaction(new Float(100), "JOE001", 123456, new Float(50.2f), 200, Timestamp.valueOf("2008-05-13 00:00:00"), "TC");
		smsTransactionList.add(smsTransaction1);
		SmsTransaction smsTransaction2 = new SmsTransaction(new Float(50), "BOB001", 123457, new Float(0.2f), 120, Timestamp.valueOf("2008-05-15 00:30:00"), "TC");
		smsTransactionList.add(smsTransaction2);
		SmsTransaction smsTransaction3 = new SmsTransaction(new Float(67), "GAM003", 123458, new Float(10.8f), 123, Timestamp.valueOf("2008-05-12 12:00:00"), "TC");
		smsTransactionList.add(smsTransaction3);
		SmsTransaction smsTransaction4 = new SmsTransaction(new Float(55), "MAR008", 123459, new Float(20.2f), 333, Timestamp.valueOf("2008-05-19 00:00:00"), "TC");
		smsTransactionList.add(smsTransaction4);
		SmsTransaction smsTransaction5 = new SmsTransaction(new Float(200), "CCL005", 123460, new Float(55.5f), 123, Timestamp.valueOf("2008-05-10 15:00:00"), "TC");
		smsTransactionList.add(smsTransaction5);
	}
	
	public List<SmsTransaction> getAllSmsTransaction(){
		return smsTransactionList;
	}

	public SmsTransaction getSmsTransaction(int index){
		
		if(index >= smsTransactionList.size())
			throw new RuntimeException("The specified index is too high");
			
		return smsTransactionList.get(index);	
	}
	
	public int getTotalSmsTransactionList(){
		return smsTransactionList.size();
	}
}
