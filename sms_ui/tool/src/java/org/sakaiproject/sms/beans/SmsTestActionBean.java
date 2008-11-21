/***********************************************************************************
 * SmsTestActionBean.java
 * Copyright (c) 2008 Sakai Project/Sakai Foundation
 * 
 * Licensed under the Educational Community License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * 
 *      http://www.osedu.org/licenses/ECL-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 *
 **********************************************************************************/

package org.sakaiproject.sms.beans;

import org.sakaiproject.sms.api.SmsSmpp;
import org.sakaiproject.sms.hibernate.model.SmsMessage;
import org.sakaiproject.sms.otp.SmsMessageLocator;

/**
 * The Class SmsTestActionBean
 */
public class SmsTestActionBean {

	/** The sms message locator. */
	private SmsMessageLocator smsMessageLocator;

	/** The sms smpp. */
	private SmsSmpp smsSmpp;

	/**
	 * Retrieve the new SmsMessage from the sms message locator Send message to
	 * gateway
	 * 
	 * @return the string
	 */
	public String send() {
		System.out.println("CALLED");
		// Get the new bean created to send
		SmsMessage msg = (SmsMessage) smsMessageLocator
				.locateBean(SmsMessageLocator.NEW_1);
		smsSmpp.sendMessageToGateway(msg);
		return ActionResults.SUCCESS;
	}

	/**
	 * Sets the sms message locator.
	 * 
	 * @param smsMessageLocator
	 *            the new sms message locator
	 */
	public void setSmsMessageLocator(SmsMessageLocator smsMessageLocator) {
		this.smsMessageLocator = smsMessageLocator;
	}

	/**
	 * Sets the sms smpp.
	 * 
	 * @param smsSmpp
	 *            the new sms smpp
	 */
	public void setSmsSmpp(SmsSmpp smsSmpp) {
		this.smsSmpp = smsSmpp;
	}
}
