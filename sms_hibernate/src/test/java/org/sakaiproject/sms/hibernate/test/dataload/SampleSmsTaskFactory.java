package org.sakaiproject.sms.hibernate.test.dataload;

import java.util.ArrayList;
import java.util.List;

import org.sakaiproject.sms.hibernate.model.SmsTask;
import org.sakaiproject.sms.hibernate.model.constants.SmsConst_DeliveryStatus;

public class SampleSmsTaskFactory implements Listable {

	List<SmsTask> smsTasks;
	RandomUtils randomUtils = new RandomUtils();

	public SampleSmsTaskFactory() {
		createSampleSmsTasks();
	}

	public Object getElementAt(int i) {
		return getTestSmsTask(i);
	}

	public void refreshList() {
		createSampleSmsTasks();

	}

	private SmsTask setTaskProperties(SmsTask task) {
		task.setGroupSizeEstimate((int) (Math.random() * 1000 + 10));
		task.setGroupSizeActual((int) (Math.random() * 1000 + 10));
		// task.setDateToSend(randomUtils.getBoundRandomDate(2008));
		task.setDateProcessed(randomUtils.getBoundRandomDate(2008));
		task.setSakaiToolName("Sakai tool");
		task.setMaxTimeToLive(1000);
		task.setDelReportTimeoutDuration(1000);
		return task;
	}

	private void createSampleSmsTasks() {
		smsTasks = new ArrayList<SmsTask>();

		/*
		 * SmsTask task1 = new SmsTask("3", "SC", "CHEM100-05", 123456,
		 * "Test date moved form 12 Jan to 15 Jan");
		 */
		SmsTask task1 = new SmsTask();
		task1.setSakaiSiteId("3");
		task1.setDeliveryUserId("SC");
		task1.setDeliveryGroupId("CHEM100-05");
		task1.setSmsAccountId(123456);
		task1.setMessageBody("Test date moved form 12 Jan to 15 Jan");
		task1 = setTaskProperties(task1);
		task1.setDateCreated(randomUtils.getBoundRandomDate(2008));
		task1.setDateToSend(randomUtils.getBoundRandomDate(2008));
		task1.setSenderUserName("Prof Blue");
		task1.setStatusCode(SmsConst_DeliveryStatus.STATUS_RETRY);
		smsTasks.add(task1);

		/*
		 * SmsTask task2 = new SmsTask("56", "GM", "EEE475-05", 123457,
		 * "Matlab tutorial move to Science labs D");
		 */
		SmsTask task2 = new SmsTask();
		task2.setSakaiSiteId("56");
		task2.setDeliveryUserId("GM");
		task2.setDeliveryGroupId("EEE475-05");
		task2.setSmsAccountId(123457);
		task2.setMessageBody("Matlab tutorial move to Science labs D");
		task2 = setTaskProperties(task2);
		task2.setDateCreated(randomUtils.getBoundRandomDate(2008));
		task2.setDateToSend(randomUtils.getBoundRandomDate(2008));
		task2.setSenderUserName("Prof Green");
		task2.setStatusCode(SmsConst_DeliveryStatus.STATUS_SENT);
		smsTasks.add(task2);

		/*
		 * SmsTask task3 = new SmsTask("32", "RD", "MAM100-05", 123458,
		 * "Location of tut changed to Science Block");
		 */
		SmsTask task3 = new SmsTask();
		task3.setSakaiSiteId("32");
		task3.setDeliveryUserId("RD");
		task3.setDeliveryGroupId("MAM100-05");
		task3.setSmsAccountId(123458);
		task3.setMessageBody("Location of tut changed to Science Block");
		task3 = setTaskProperties(task3);
		task3.setDateCreated(randomUtils.getBoundRandomDate(2008));
		task3.setDateToSend(randomUtils.getBoundRandomDate(2008));
		task3.setSenderUserName("Prof Red");
		task3.setStatusCode(SmsConst_DeliveryStatus.STATUS_DELIVERED);
		smsTasks.add(task3);

		/*
		 * SmsTask task4 = new SmsTask("67", "EO", "PHY131-05", 123459,
		 * "Problem set to be handed in by 15 Jan");
		 */
		SmsTask task4 = new SmsTask();
		task4.setSakaiSiteId("67");
		task4.setDeliveryUserId("EO");
		task4.setDeliveryGroupId("PHY131-05");
		task4.setSmsAccountId(123459);
		task4.setMessageBody("Problem set to be handed in by 15 Jan");
		task4 = setTaskProperties(task4);
		task4.setDateCreated(randomUtils.getBoundRandomDate(2008));
		task4.setDateToSend(randomUtils.getBoundRandomDate(2008));
		task4.setSenderUserName("Prof Lime");
		task4.setStatusCode(SmsConst_DeliveryStatus.STATUS_FAIL);
		smsTasks.add(task4);

		/*
		 * SmsTask task5 = new SmsTask("42", "FQ", "BUS100-05", 123460,
		 * "No tutorial required this month");
		 */
		SmsTask task5 = new SmsTask();
		task5.setSakaiSiteId("42");
		task5.setDeliveryUserId("FQ");
		task5.setDeliveryGroupId("BUS100-05");
		task5.setSmsAccountId(123460);
		task5.setMessageBody("No tutorial required this month");
		task5 = setTaskProperties(task5);
		task5.setDateCreated(randomUtils.getBoundRandomDate(2008));
		task5.setDateToSend(randomUtils.getBoundRandomDate(2008));
		task5.setSenderUserName("Prof Orange");
		task5.setStatusCode(SmsConst_DeliveryStatus.STATUS_INCOMPLETE);
		smsTasks.add(task5);

	}

	public List<SmsTask> getAllTestSmsTasks() {
		return smsTasks;
	}

	public SmsTask getTestSmsTask(int index) {

		if (index >= smsTasks.size())
			throw new RuntimeException("The specified index is too high");

		return smsTasks.get(index);
	}

	public int getTotalSmsTasks() {
		return smsTasks.size();
	}

}
