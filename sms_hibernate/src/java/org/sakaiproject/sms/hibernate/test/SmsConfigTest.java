package org.sakaiproject.sms.hibernate.test;

import java.util.List;

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
		//Check the record was created on the DB... an id will be assigned.
		assertTrue("Object not persisted", insertSmsConfig.exists());
	}
	
	public void testGetSmsConfigById(){
		SmsConfig getSmsSonfig = logic.getSmsConfig(insertSmsConfig.getId());
		assertTrue("Object not persisted", insertSmsConfig.exists());
		assertNotNull(getSmsSonfig);
		assertEquals(insertSmsConfig, getSmsSonfig);
	}
	
	
	public void testUpdateSmsConfig(){
		SmsConfig smsConfig = logic.getSmsConfig(insertSmsConfig.getId());
		smsConfig.setSakaiSiteId("newSakaiSiteId");
		logic.persistSmsConfig(smsConfig);
		smsConfig = logic.getSmsConfig(insertSmsConfig.getId());
		assertEquals("newSakaiSiteId", smsConfig.getSakaiSiteId());
	}

	public void testGetSmsConfigs() {
		List<SmsConfig> confs = logic.getAllSmsConfig();
		assertNotNull("Returend collection is null", confs);
		assertTrue("No records returned", confs.size() > 0);
	}
	
	public void testDeleteSmsConfig(){
		logic.deleteSmsCongif(insertSmsConfig);
		SmsConfig getSmsConfig = logic.getSmsConfig(insertSmsConfig.getId());
		assertNull(getSmsConfig);
	}

}
