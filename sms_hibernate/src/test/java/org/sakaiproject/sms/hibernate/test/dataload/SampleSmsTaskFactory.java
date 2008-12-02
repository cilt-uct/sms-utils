package org.sakaiproject.sms.hibernate.test.dataload;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.sakaiproject.sms.hibernate.model.SmsTask;

public class SampleSmsTaskFactory {

	List<SmsTask> smsTasks;
	
	public SampleSmsTaskFactory() {
		createSampleSmsTasks(); 
	}
	
	private void createSampleSmsTasks() {
		smsTasks = new ArrayList<SmsTask>();
		
		SmsTask task1 = new SmsTask("3", "SC", "CHEM100-05", 123456, "Test date moved form 12 Jan to 15 Jan");
		task1.setDateCreated(Timestamp.valueOf("2008-05-13 00:00:00"));
		task1.setDateToSend(Timestamp.valueOf("2008-05-13 00:00:00"));
		task1.setSenderUserName("Prof Blue");
		smsTasks.add(task1);
		
		SmsTask task2 = new SmsTask("56", "GM", "EEE475-05", 123457, "Matlab tutorial move to Science labs D");
		task2.setDateCreated(Timestamp.valueOf("2008-05-13 00:00:00"));
		task2.setDateToSend(Timestamp.valueOf("2008-05-14 00:00:00"));
		task2.setSenderUserName("Prof Green");
		smsTasks.add(task2);

		
		SmsTask task3 = new SmsTask("32", "RD", "MAM100-05", 123458, "Location of tut changed to Science Block");
		task3.setDateCreated(Timestamp.valueOf("2008-05-13 00:00:00"));
		task3.setDateToSend(Timestamp.valueOf("2008-05-13 00:15:00"));
		task3.setSenderUserName("Prof Red");
		smsTasks.add(task3);

		
		SmsTask task4 = new SmsTask("67", "EO", "PHY131-05", 123459, "Problem set to be handed in by 15 Jan");
		task4.setDateCreated(Timestamp.valueOf("2008-05-13 00:00:00"));
		task4.setDateToSend(Timestamp.valueOf("2008-05-13 00:14:00"));
		task4.setSenderUserName("Prof Lime");
		smsTasks.add(task4);
		
		SmsTask task5 = new SmsTask("42", "FQ", "BUS100-05", 123460, "No tutorial required this month");
		task5.setDateCreated(Timestamp.valueOf("2008-05-14 00:00:00"));
		task5.setDateToSend(Timestamp.valueOf("2008-05-14 00:00:00"));
		task5.setSenderUserName("Prof Orange");
		smsTasks.add(task5);
		
	}
	
	public List<SmsTask> getAllTestSmsTasks(){
		return smsTasks;
	}

	public SmsTask getTestSmsTask(int index){
		
		if(index >= smsTasks.size())
			throw new RuntimeException("The specified index is too high");
			
		return smsTasks.get(index);	
	}
	
	public int getTotalSmsTasks(){
		return smsTasks.size();
	}
	
}
