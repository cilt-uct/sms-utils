package org.sakaiproject.sms.otp;

import java.util.HashMap;
import java.util.Map;

import org.sakaiproject.sms.hibernate.logic.SmsConfigLogic;
import org.sakaiproject.sms.hibernate.model.SmsConfig;

import uk.org.ponder.beanutil.BeanLocator;

public class SmsSystemConfigLocator implements BeanLocator{

	private SmsConfigLogic smsConfigLogic;

	public void setSmsConfigLogic(SmsConfigLogic smsConfigLogic) {
		this.smsConfigLogic = smsConfigLogic;
	}

	public static final String LOCATOR_NAME = "SmsSystemConfigLocator";
	
	public static final String NEW_PREFIX = "new ";

	public static final String NEW_1 = NEW_PREFIX + "1";

	private final Map<String, SmsConfig> delivered = new HashMap<String, SmsConfig>();

	
	public Object locateBean(String name) {
		SmsConfig togo = delivered.get(name);
		if (togo == null) {
			if (name.startsWith(NEW_PREFIX)) {
				togo = new SmsConfig();
			} else {
			}
			togo = smsConfigLogic.getOrCreateSystemSmsConfig();
			delivered.put(name, togo);
		}
		return togo;
	}
	
	public void save(){		
		for (SmsConfig systemConfig : delivered.values()) {
			//round by 2 decimal places, if logic is placed in SMSConfig a InvocationTargetException occurs
			systemConfig.setCreditCost(Math.round(systemConfig.getCreditCost() * 100) * 0.01f);
			smsConfigLogic.persistSmsConfig(systemConfig);
		}
	}
}
