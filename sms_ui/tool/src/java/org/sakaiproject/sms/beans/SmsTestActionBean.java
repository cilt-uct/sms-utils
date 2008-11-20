package org.sakaiproject.sms.beans;

import org.sakaiproject.sms.api.SmsSmpp;
import org.sakaiproject.sms.hibernate.model.SmsMessage;

public class SmsTestActionBean {

	private SmsSmpp smsSmpp;
	private SmsTestBean smsTestBean;

	public void send() {
		SmsMessage message = new SmsMessage(smsTestBean.getMobileNumber(),
				smsTestBean.getMessageBody());
		smsSmpp.sendMessageToGateway(message);
	}

	public void setSmsSmpp(SmsSmpp smsSmpp) {
		this.smsSmpp = smsSmpp;
	}

	public void setSmsTestBean(SmsTestBean smsTestBean) {
		this.smsTestBean = smsTestBean;
	}
}
