package org.sakaiproject.sms.hibernate.test;

import org.sakaiproject.sms.hibernate.logic.impl.SmsDataLogicImpl;
import org.sakaiproject.sms.hibernate.model.BaseModel;
import org.sakaiproject.sms.hibernate.model.SmsTransaction;

import junit.framework.TestCase;

public class SmsTransactionTest extends TestCase {
	private SmsDataLogicImpl logic = null;
	
	{
		logic = new SmsDataLogicImpl();
	}
	
	public SmsTransactionTest() {
	}

	public SmsTransactionTest(String name) {
		super(name);
	}
	
	public void testInsertSmsTransaction(){
		SmsTransaction tran = new SmsTransaction();
		tran.setBalance(1.32f);
		tran.setSakaiUserId("Sakai User ID");
		tran.setSmsAccountId(555);
		tran.setTransactionTypeCode("TC");
		tran.setTransactionCredits(666);
		tran.setTransactionAmount(1000.00f);
		//logic.persist(tran);
		if(! tran.exists()){
			fail("Transaction ID was not created on the db correclty.");
		}
	}
	
	public void testUpdateSmsTransaction(){
		
	}
	
	public void testGetSmsTransactions(){
		
	}
	
	public void testGetSmsTransactionById(){
		/*BaseModel tran = logic.getSmsTransaction(new Long(1));
		if(!(tran instanceof SmsTransaction)){
			fail("Method: getSmsTransaction() return incorrect object type.");
		}
		
		//assertEquals(expected, actual);
*/	}
	
	public void testDeleteSmsTransaction(){
		
	}
	
	

}
