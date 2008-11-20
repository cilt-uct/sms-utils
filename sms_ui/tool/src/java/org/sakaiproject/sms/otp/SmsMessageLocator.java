package org.sakaiproject.sms.otp;

import java.util.HashMap;
import java.util.Map;

import org.sakaiproject.sms.hibernate.model.SmsMessage;

import uk.org.ponder.beanutil.BeanLocator;

public class SmsMessageLocator implements BeanLocator {

	public static final String LOCATOR_NAME = "SmsMessageLocator";
	public static final String NEW_PREFIX = "new ";
	public static final String NEW_1 = NEW_PREFIX + "1";

	private final Map<String, SmsMessage> delivered = new HashMap<String, SmsMessage>();

	@Override
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
