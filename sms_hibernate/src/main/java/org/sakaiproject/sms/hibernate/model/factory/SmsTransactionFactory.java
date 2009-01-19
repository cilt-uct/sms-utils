package org.sakaiproject.sms.hibernate.model.factory;

import org.sakaiproject.sms.hibernate.logic.impl.HibernateLogicFactory;
import org.sakaiproject.sms.hibernate.logic.impl.exception.SmsAccountNotFoundException;
import org.sakaiproject.sms.hibernate.model.SmsAccount;
import org.sakaiproject.sms.hibernate.model.SmsTransaction;
import org.sakaiproject.sms.hibernate.model.constants.SmsConst_Billing;

public class SmsTransactionFactory {

	public static SmsTransaction createReserveCreditsTask(Long smsTaskId, Long smsAccountId, Integer credits) throws SmsAccountNotFoundException{
		
		SmsAccount smsAccount = HibernateLogicFactory.getAccountLogic().getSmsAccount(smsAccountId);
		
		if(smsAccount == null)
			throw new SmsAccountNotFoundException("Account id " + smsAccountId + " does not exsits");
		
		SmsTransaction smsTransaction = new SmsTransaction();
		smsTransaction.setSmsAccount(smsAccount);
		smsTransaction.setSakaiUserId(smsAccount.getSakaiUserId());
		smsTransaction.setTransactionCredits(credits);
		smsTransaction.setTransactionTypeCode(SmsConst_Billing.TRANS_RESERVE_CREDITS);
		smsTransaction.setSmsTaskId(smsTaskId);
		
		//TODO : Check to see if this is the case
		smsTransaction.setTransactionAmount(0.0f);
		smsTransaction.setBalance(0.0f);
		
		return smsTransaction;
	}


	
}
