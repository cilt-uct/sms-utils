package org.sakaiproject.sms.hibernate.test;

import org.sakaiproject.sms.hibernate.logic.impl.HibernateLogicFactory;
import org.sakaiproject.sms.hibernate.logic.impl.exception.SmsAccountNotFoundException;
import org.sakaiproject.sms.hibernate.model.SmsAccount;
import org.sakaiproject.sms.hibernate.util.AbstractBaseTestCase;

public class SmsSchedulingTasksTest extends AbstractBaseTestCase{

	public void testCreateReserveCreditsTaskFail() throws Exception {
		
		try{			
			HibernateLogicFactory.getTaskLogic().reserveCredits("John", new Integer(100), "SakaiSiteId",  new Long(12345));
			fail("Insert should fail since there is no account with id 12345");
		}
		catch (SmsAccountNotFoundException expected) {
		}
		catch (Exception notExpected) {
			fail("An account not found exception should be thrown");
		}
	}	
		
		
	public 	void testCreateReserveCreditsTaskPass(){
	
		SmsAccount testAccount = SmsAccountTest.createTestAccount();	
		HibernateLogicFactory.getAccountLogic().persistSmsAccount(testAccount);
		
		try{			
			HibernateLogicFactory.getTaskLogic().reserveCredits("John", new Integer(100), "SakaiSiteId",  testAccount.getId());
		}
		catch (Exception notExpected) {
			fail("Task should save successfully" + notExpected);
		}
	}
	
}
