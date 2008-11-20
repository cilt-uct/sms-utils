package org.sakaiproject.sms.beans;

import org.sakaiproject.sms.api.SmsSmpp;
import org.sakaiproject.sms.hibernate.model.SmsMessage;
import org.sakaiproject.sms.otp.SmsMessageLocator;

public class SmsTestActionBean {

	private SmsMessageLocator smsMessageLocator;
	private SmsSmpp smsSmpp;

	public void send() {
		SmsMessage msg = (SmsMessage) smsMessageLocator
				.locateBean(SmsMessageLocator.NEW_1);
		smsSmpp.sendMessageToGateway(msg);
	}

	public void setSmsMessageLocator(SmsMessageLocator smsMessageLocator) {
		this.smsMessageLocator = smsMessageLocator;
	}

	public void setSmsSmpp(SmsSmpp smsSmpp) {
		this.smsSmpp = smsSmpp;
	}
}
