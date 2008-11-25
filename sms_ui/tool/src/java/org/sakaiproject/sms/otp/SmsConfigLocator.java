package org.sakaiproject.sms.otp;

import java.util.HashMap;
import java.util.Map;

import org.sakaiproject.sms.hibernate.model.SmsMessage;

import uk.org.ponder.beanutil.BeanLocator;


public class SmsConfigLocator implements BeanLocator{

	/** The Constant LOCATOR_NAME. */
	public static final String LOCATOR_NAME = "SmsConfigLocator";
	
	public static final String NEW_PREFIX = "new ";

	/** The Constant NEW_1. */
	public static final String NEW_1 = NEW_PREFIX + "1";

	/** The delivered map (used to store beans). */
	private final Map<String, SmsMessage> delivered = new HashMap<String, SmsMessage>();

	/**
	 * Retrieves SmsMessage bean
	 * 
	 * @see uk.org.ponder.beanutil.BeanLocator#locateBean(java.lang.String)
	 */
	public Object locateBean(String name) {
		SmsMessage togo = delivered.get(name);
		if (togo == null) {
			if (name.startsWith(NEW_PREFIX)) {
				togo = new SmsMessage("", "");
			} else {
				// TODO: Retrieve it from database
			}
			delivered.put(name, togo);
		}
		return togo;
	}
}
