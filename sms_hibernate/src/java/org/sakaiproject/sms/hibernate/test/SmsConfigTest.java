package org.sakaiproject.sms.hibernate.test;

import java.util.List;

import junit.framework.TestCase;

import org.sakaiproject.sms.hibernate.logic.impl.SmsConfigLogicImpl;
import org.sakaiproject.sms.hibernate.model.SmsConfig;

/**
 * Some basic crud tests on sms tool configuration.
 */
public class SmsConfigTest extends TestCase {

	/** The logic. */
	private static SmsConfigLogicImpl logic = null;

	/** The insert sms config. */
	private static SmsConfig insertSmsConfig;

	static {
		logic = new SmsConfigLogicImpl();

		insertSmsConfig = new SmsConfig();
		insertSmsConfig.setSakaiSiteId("sakaiSiteId");
		insertSmsConfig.setSakaiToolId("sakaiToolId");
		insertSmsConfig.setNotificationEmail("notification@Email.Address");
		insertSmsConfig.setSmsEnabled(false);
	}

	/**
	 * Instantiates a new sms config test.
	 */
	public SmsConfigTest() {
	}

	/**
	 * Instantiates a new sms config test.
	 * 
	 * @param name
	 *            the name
	 */
	public SmsConfigTest(String name) {
		super(name);
	}

	/**
	 * Test insert sms config.
	 */
	public void testInsertSmsConfig() {
		logic.persistSmsConfig(insertSmsConfig);
		// Check the record was created on the DB... an id will be assigned.
		assertTrue("Object not persisted", insertSmsConfig.exists());
	}

	/**
	 * Test get sms config by id.
	 */
	public void testGetSmsConfigById() {
		SmsConfig getSmsConfig = logic.getSmsConfig(insertSmsConfig.getId());
		assertTrue("Object not persisted", insertSmsConfig.exists());
		assertNotNull(getSmsConfig);
		assertEquals(insertSmsConfig, getSmsConfig);
		assertTrue("Boolean property problem",
				getSmsConfig.getSmsEnabled() == false);
	}

	/**
	 * Test update sms config.
	 */
	public void testUpdateSmsConfig() {
		SmsConfig smsConfig = logic.getSmsConfig(insertSmsConfig.getId());
		smsConfig.setSakaiSiteId("newSakaiSiteId");
		logic.persistSmsConfig(smsConfig);
		smsConfig = logic.getSmsConfig(insertSmsConfig.getId());
		assertEquals("newSakaiSiteId", smsConfig.getSakaiSiteId());
	}

	/**
	 * Test get sms configs.
	 */
	public void testGetSmsConfigs() {
		List<SmsConfig> confs = logic.getAllSmsConfig();
		assertNotNull("Returned collection is null", confs);
		assertTrue("No records returned", confs.size() > 0);
	}

	/**
	 * Test delete sms config.
	 */
	public void testDeleteSmsConfig() {
		logic.deleteSmsCongif(insertSmsConfig);
		SmsConfig getSmsConfig = logic.getSmsConfig(insertSmsConfig.getId());
		assertNull(getSmsConfig);
		assertNull("Object not removed", getSmsConfig);
	}

}
