package org.sakaiproject.sms.hibernate.test;

import org.sakaiproject.sms.hibernate.logic.impl.SmsConfigLogicImpl;
import org.sakaiproject.sms.hibernate.logic.impl.SmsDataLogicImpl;
import org.sakaiproject.sms.hibernate.model.BaseModel;
import org.sakaiproject.sms.hibernate.model.SmsConfig;
import org.sakaiproject.sms.hibernate.model.SmsTransaction;

import junit.framework.TestCase;

public class SmsConfigTest extends TestCase {
	private SmsConfigLogicImpl logic = null;
	
	private SmsConfig insertSmsConfig;
	
	{
		logic = new SmsConfigLogicImpl();
		
		insertSmsConfig.setSakaiSiteId("sakaiSiteId");
		insertSmsConfig.setSakaiToolId("sakaiToolId");
		insertSmsConfig.setNotificationEmail("notification@Email.Address");
		insertSmsConfig.setSmsEnabled(true);
	}
	
	public SmsConfigTest() {
	}

	public SmsConfigTest(String name) {
		super(name);
	}
	
	public void testInsertSmsConfig(){
		logic.persistSmsConfig(insertSmsConfig);
		
		//Check the record was created on the DB
		assertTrue("Object not persisted correclty", insertSmsConfig.exists());
		
		System.out.println("");
	}
	
	public void testGetSmsConfig(){
		logic.getSmsConfig(insertSmsConfig.getId());
	}
	
	
	public void testUpdateSmsConfig(){
		
	}
	
	
	public void testGetSmsConfigById(){
		
	}
	
	public void testDeleteSmsConfig(){
		
	}
	
	

}
