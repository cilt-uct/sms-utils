package org.sakaiproject.sms.beans;

import org.sakaiproject.sms.api.SmsBilling;

public class DebitAccountActionBean {

	private DebitAccountBean debitAccountBean;
	private SmsBilling smsBilling;
		
	public void setDebitAccountBean(DebitAccountBean debitAccountBean) {
		this.debitAccountBean = debitAccountBean;
	}
	
	public void setSmsBilling(SmsBilling smsBilling) {
		this.smsBilling = smsBilling;
	}

	public void debitAccount(){
		smsBilling.debitAccount(debitAccountBean.getAccountId(), debitAccountBean.getAmountToDebit());		
	}
}
