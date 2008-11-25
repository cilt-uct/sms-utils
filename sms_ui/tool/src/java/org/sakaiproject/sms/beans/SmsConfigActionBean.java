/***********************************************************************************
 * SMSConfigActionBean.java
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.sms.hibernate.logic.SmsConfigLogic;
import org.sakaiproject.sms.hibernate.model.SmsConfig;
import org.sakaiproject.sms.otp.SmsConfigLocator;
import org.sakaiproject.sms.otp.SmsMessageLocator;
import org.springframework.util.Assert;


public class SmsConfigActionBean {

	private static Log log = LogFactory.getLog(SmsConfigActionBean.class);
	
	private SmsConfigLocator smsConfigLocator;
	private SmsConfigLogic smsConfigLogic;

	public void setSmsConfigLocator(SmsConfigLocator smsConfigLocator) {
		this.smsConfigLocator = smsConfigLocator;
	}
	
	public void setSmsConfigLogic(SmsConfigLogic smsConfigLogic) {
		this.smsConfigLogic = smsConfigLogic;
	}

	public void init(){
		Assert.notNull(smsConfigLocator);
		Assert.notNull(smsConfigLogic);
	}
	
	public String save(){		
		
		SmsConfig smsConfig = (SmsConfig) smsConfigLocator
		.locateBean(SmsMessageLocator.NEW_1);
		
		if(smsConfig.getSmsEnabled().equals(Boolean.FALSE))
			smsConfig.setNotificationEmail("");
		
		//don't have the sakaiSiteId or sakaiToolId Yet
		smsConfig.setSakaiSiteId("sakaiSiteId");
		smsConfig.setSakaiToolId("sakaiToolId");
		
		if(log.isInfoEnabled()){			
			log.info("Persisting smsConfig");
		}
		
		smsConfigLogic.persistSmsConfig(smsConfig);
		
		return ActionResults.SUCCESS;
	}
}
