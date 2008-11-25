package org.sakaiproject.sms.otp;

import java.util.HashMap;
import java.util.Map;

import org.sakaiproject.sms.hibernate.model.SmsConfig;

import uk.org.ponder.beanutil.BeanLocator;


public class SmsConfigLocator implements BeanLocator{

	/** The Constant LOCATOR_NAME. */
	public static final String LOCATOR_NAME = "SmsConfigLocator";
	
	public static final String NEW_PREFIX = "new ";

	/** The Constant NEW_1. */
	public static final String NEW_1 = NEW_PREFIX + "1";

	/** The delivered map (used to store beans). */
	private final Map<String, SmsConfig> delivered = new HashMap<String, SmsConfig>();

	/**
	 * Retrieves SmsMessage bean
	 * 
	 * @see uk.org.ponder.beanutil.BeanLocator#locateBean(java.lang.String)
	 */
	public Object locateBean(String name) {
		SmsConfig togo = delivered.get(name);
		if (togo == null) {
			if (name.startsWith(NEW_PREFIX)) {
				togo = new SmsConfig();
			} else {
				// TODO: Retrieve it from database
			}
			delivered.put(name, togo);
		}
		return togo;
	}
}
