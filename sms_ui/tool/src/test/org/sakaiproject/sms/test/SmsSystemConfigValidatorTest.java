/***********************************************************************************
 * SmsSystemConfigValidatorTest.java
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
package org.sakaiproject.sms.test;

import junit.framework.TestCase;

import org.sakaiproject.sms.hibernate.model.SmsConfig;
import org.sakaiproject.sms.validators.SmsSystemConfigValidator;
import org.springframework.validation.BindException;

public class SmsSystemConfigValidatorTest extends TestCase{

	private SmsSystemConfigValidator validator;
	private BindException bindException;
	private SmsConfig smsConfig;

	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		smsConfig = new SmsConfig();
		validator = new SmsSystemConfigValidator();
		bindException = new BindException(smsConfig, "SmsConfig");
	}

	public void testInvalidConfigSettings() throws Exception {
		smsConfig.setDelReportTimeoutDuration(null);
		smsConfig.setSchedulerInterval(null);
		validator.validate(smsConfig, bindException);
		assertTrue(bindException.hasErrors());
	}
	
	public void testValidConfigSettings() throws Exception {
		smsConfig.setDelReportTimeoutDuration(new Integer(100));
		smsConfig.setSchedulerInterval(new Integer(100));
		smsConfig.setCreditCost(100.12f);
		validator.validate(smsConfig, bindException);
		assertFalse(bindException.hasErrors());
	}
	
	public void testInvalidCreditCost() throws Exception {
		smsConfig.setDelReportTimeoutDuration(new Integer(100));
		smsConfig.setSchedulerInterval(new Integer(100));
		smsConfig.setSchedulerInterval(null);
		validator.validate(smsConfig, bindException);
		assertTrue(bindException.hasErrors());
	}
}
