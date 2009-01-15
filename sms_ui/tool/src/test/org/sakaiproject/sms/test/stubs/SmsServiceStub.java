package org.sakaiproject.sms.test.stubs;

import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

import org.sakaiproject.sms.api.SmsService;
import org.sakaiproject.sms.hibernate.model.SmsTask;

public class SmsServiceStub implements SmsService {

	public SmsTask calculateEstimatedGroupSize(SmsTask arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean checkSufficientCredits(String arg0, String arg1, int arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	public SmsTask getPreliminaryTask(String arg0, Date arg1, String arg2,
			String arg3, String arg4, String arg5) {
		// TODO Auto-generated method stub
		return null;
	}

	public SmsTask getPreliminaryTask(Set<String> arg0, Date arg1, String arg2,
			String arg3, String arg4, String arg5) {
		// TODO Auto-generated method stub
		return null;
	}

	public ArrayList<String> validateTask(SmsTask arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
