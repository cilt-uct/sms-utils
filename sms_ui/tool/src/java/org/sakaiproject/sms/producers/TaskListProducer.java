package org.sakaiproject.sms.producers;

public class TaskListProducer extends AbstractSearchListProducer{
	
	public static final String VIEW_ID = "TaskList";

	@Override
	public String getViewID() {
		return VIEW_ID;
	}

	@Override
	public String getTitleMessage() {
		return "sms.view-results-task-list.name";
	}
}

