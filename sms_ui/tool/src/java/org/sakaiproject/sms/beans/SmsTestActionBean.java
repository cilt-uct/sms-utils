/***********************************************************************************
 * SmsSmpp.java
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
