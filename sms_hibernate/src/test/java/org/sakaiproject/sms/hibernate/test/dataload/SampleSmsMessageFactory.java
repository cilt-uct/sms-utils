package org.sakaiproject.sms.hibernate.test.dataload;

import java.util.ArrayList;
import java.util.List;

import org.sakaiproject.sms.hibernate.model.SmsMessage;

public class SampleSmsMessageFactory {

	private List<SmsMessage> smsMessages;

	public SampleSmsMessageFactory() {
		creatSampleSmsMessages();
	}

	private void creatSampleSmsMessages() {
		smsMessages = new ArrayList<SmsMessage>();
		
		SmsMessage message1 = new SmsMessage("083468221", "Test date moved form 12 Jan to 15 Jan");
		message1.setSakaiUserId("SNK111");
		smsMessages.add(message1);
		SmsMessage message2 = new SmsMessage("0823456789", "Matlab tutorial move to Science labs D");
		message2.setSakaiUserId("BIT111");
		smsMessages.add(message2);
		SmsMessage message3 = new SmsMessage("08255552222", "Location of tut changed to Science Block");
		message3.setSakaiUserId("BAT111");
		smsMessages.add(message3);
		SmsMessage message4 = new SmsMessage("08266661122", "Problem set to be handed in by 15 Jan");
		message4.setSakaiUserId("RAT111");
		smsMessages.add(message4);
		SmsMessage message5 = new SmsMessage("084444667", "No tutorial required this month");
		message5.setSakaiUserId("COW111");
		smsMessages.add(message5);	
	}
	
	public List<SmsMessage> getAllTestSmsMessages(){
		return smsMessages;
	}

	public SmsMessage getTestSmsMessage(int index){
		
		if(index >= smsMessages.size())
			throw new RuntimeException("The specified index is too high");
			
		return smsMessages.get(index);	
	}
	
	public int getTotalSmsMessages(){
		return smsMessages.size();
	}
	
}
