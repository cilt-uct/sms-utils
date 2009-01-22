package org.sakaiproject.sms.validators;

import org.sakaiproject.sms.beans.DebitAccountBean;
import org.sakaiproject.sms.hibernate.logic.SmsAccountLogic;
import org.sakaiproject.sms.hibernate.logic.impl.HibernateLogicFactory;
import org.sakaiproject.sms.hibernate.model.SmsAccount;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class DebitAccountValidator implements Validator{

	private SmsAccountLogic smsAccountLogic;
	
	public void setSmsAccountLogic(SmsAccountLogic smsAccountLogic) {
		this.smsAccountLogic = smsAccountLogic;
	}

	@SuppressWarnings("unchecked")
	public boolean supports(Class clazz) {
		if (DebitAccountBean.class.equals(clazz.getClass())) {
			return true;
		}
		return false;
	}

	public void validate(Object target, Errors errors) {
		DebitAccountBean account = (DebitAccountBean) target;
		
		//check account exsists
		SmsAccount smsAccount = smsAccountLogic.getSmsAccount(account.getAccountId());
		
		if(smsAccount == null){
			errors.reject("sms.debit.account.errors.no.account");
		}
		
		if(account.getAmountToDebit() < 0){
			errors.rejectValue("amountToDebit", "sms.debit.account.errors.debit.amount");
		}
		
	}

}
