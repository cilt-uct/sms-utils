package org.sakaiproject.sms.util;

import org.sakaiproject.sms.hibernate.logic.SmsAccountLogic;
import org.sakaiproject.sms.hibernate.model.SmsAccount;

import uk.org.ponder.beanutil.BeanGetter;

public class SmsAccountHelper {
	private BeanGetter ELEvaluator;
	private SmsAccountLogic smsAccountLogic;

	/**
	 * Retrieves the EL path (which must be the accountId of a SmsTask.
	 * Retrieves the SmsAccount that corresponds to the id and returns the
	 * balance.
	 */
	public SmsAccount retrieveAccount(String path) {
		Long accountId = (Long) ELEvaluator.getBean(path);
		SmsAccount smsAccount = smsAccountLogic.getSmsAccount(accountId);
		return smsAccount;
	}

	public void setELEvaluator(BeanGetter ELEvaluator) {
		this.ELEvaluator = ELEvaluator;
	}

	public void setSmsAccountLogic(SmsAccountLogic smsAccountLogic) {
		this.smsAccountLogic = smsAccountLogic;
	}
}
