/***********************************************************************************
 * SmsDataLogicImpl.java
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

package org.sakaiproject.sms.hibernate.logic.impl;

import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.sakaiproject.sms.hibernate.dao.SmsDao;
import org.sakaiproject.sms.hibernate.logic.SmsConfigLogic;
import org.sakaiproject.sms.hibernate.logic.SmsDataLogic;
import org.sakaiproject.sms.hibernate.model.SmsAccount;
import org.sakaiproject.sms.hibernate.model.SmsConfig;
import org.sakaiproject.sms.hibernate.model.SmsMessage;
import org.sakaiproject.sms.hibernate.model.SmsTask;
import org.sakaiproject.sms.hibernate.model.SmsTransaction;

/**
 * The data service will handle all sms config database transactions for the sms tool in
 * Sakai.
 * 
 * @author julian@psybergate.com
 * @version 1.0
 * @created 25-Nov-2008 08:12:41 AM
 */
public class SmsConfigLogicImpl extends SmsDao implements SmsConfigLogic {

	/**
	 * Deletes and the given entity from the DB
	 */
	public void deleteSmsCongif(SmsConfig smsConfig) {
		delete(smsConfig);
	}

	/**
	 * Gets a SmsConfig entity for the given id
	 * 
	 * @param Long sms configuration id
	 * @return sms congiguration
	 */
	public SmsConfig getSmsConfig(Long smsConfigId) {
		return null;
	}

	/**
	 * Gets all the sms configuration records
	 * 
	 * @return Set of SmsConfig objects
	 */
	public Set<SmsConfig> getSmsConfigs() {
		return null;
	}

	/**
	 * This method will persists the given object.
	 * 
	 * If the object is a new entity then it will be created on the DB. 
	 * If it is an existing entity then the record will be updates on the DB.
	 * 
	 * @param sms confuguration to be persisted
	 */
	public void persistSmsConfig(SmsConfig smsConfig) {
		persist(smsConfig);
	}

	
	
}
