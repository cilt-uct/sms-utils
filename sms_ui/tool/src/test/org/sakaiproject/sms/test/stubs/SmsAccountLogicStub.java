package org.sakaiproject.sms.test.stubs;

import java.util.List;

import org.sakaiproject.sms.hibernate.logic.SmsAccountLogic;
import org.sakaiproject.sms.hibernate.logic.impl.exception.MoreThanOneAccountFoundException;
import org.sakaiproject.sms.hibernate.model.SmsAccount;

public class SmsAccountLogicStub implements SmsAccountLogic{

	public void deleteSmsAccount(SmsAccount arg0) {
		// TODO Auto-generated method stub
		
	}

	public List<SmsAccount> getAllSmsAccounts() {
		// TODO Auto-generated method stub
		return null;
	}

	public SmsAccount getSmsAccount(Long id) {
		
		if(id.equals(1L)){
			SmsAccount testAccout = new SmsAccount(1f, "" , 100f, "Site", "Joe","Joes Account");
			testAccout.setAccountEnabled(true);
		
			return testAccout;
		}
		
		return null;
	}

	public SmsAccount getSmsAccount(String arg0, String arg1)
			throws MoreThanOneAccountFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	public void persistSmsAccount(SmsAccount arg0) {
		// TODO Auto-generated method stub
		
	}

	public void recalculateAccountBalance(Long arg0, SmsAccount arg1) {
		// TODO Auto-generated method stub
		
	}

}
