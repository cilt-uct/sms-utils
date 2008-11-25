package org.sakaiproject.sms.hibernate.test;

import org.sakaiproject.sms.hibernate.logic.impl.SmsConfigLogicImpl;
import org.sakaiproject.sms.hibernate.logic.impl.SmsDataLogicImpl;
import org.sakaiproject.sms.hibernate.model.BaseModel;
import org.sakaiproject.sms.hibernate.model.SmsConfig;
import org.sakaiproject.sms.hibernate.model.SmsTransaction;

import junit.framework.TestCase;

public class SmsConfigTest extends TestCase {
	private static SmsConfigLogicImpl logic = null;
	
	private static SmsConfig insertSmsConfig;
	
	static {
		logic = new SmsConfigLogicImpl();
		
		insertSmsConfig = new SmsConfig();
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
		assertTrue("Object not persisted", insertSmsConfig.exists());
		
		System.out.println("");
	}
	
	public void testGetSmsConfigById(){
		SmsConfig getSmsSonfig = logic.getSmsConfig(insertSmsConfig.getId());
		assertTrue("Object not persisted", insertSmsConfig.exists());
		assertNotNull(getSmsSonfig);
		assertEquals(insertSmsConfig, getSmsSonfig);
	}
	
	
	public void testUpdateSmsConfig(){
		SmsConfig origionalSmsSonfig = logic.getSmsConfig(insertSmsConfig.getId());
		insertSmsConfig.setSakaiSiteId("newSakaiSiteId");
		logic.persistSmsConfig(insertSmsConfig);
		if(insertSmsConfig.equals(origionalSmsSonfig)) {
			fail("Persist not succesfull");
		}
	}

	
	public void testDeleteSmsConfig(){
		logic.deleteSmsCongif(insertSmsConfig);
		SmsConfig getSmsConfig = logic.getSmsConfig(insertSmsConfig.getId());
		assertNull(getSmsConfig);
	}
	
	

}
