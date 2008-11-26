/***********************************************************************************
 * SmsMessageLogicImpl.java
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

import org.hibernate.Query;
import org.hibernate.Session;
import org.sakaiproject.sms.hibernate.dao.HibernateUtil;
import org.sakaiproject.sms.hibernate.dao.SmsDao;
import org.sakaiproject.sms.hibernate.logic.SmsMessageLogic;
import org.sakaiproject.sms.hibernate.model.SmsMessage;
import org.sakaiproject.sms.hibernate.model.SmsMessage;

/**
 * The data service will handle all sms Message database transactions for the sms tool in
 * Sakai.
 * 
 * @author julian@psybergate.com
 * @version 1.0
 * @created 25-Nov-2008 
 */
public class SmsMessageLogicImpl extends SmsDao implements SmsMessageLogic {

	/**
	 * Deletes and the given entity from the DB
	 */
	public void deleteSmsMessage(SmsMessage smsMessage) {
		delete(smsMessage);
	}

	/**
	 * Gets a SmsMessage entity for the given id
	 * 
	 * @param Long sms Message id
	 * @return sms Message
	 */
	public SmsMessage getSmsMessage(Long smsMessageId) {
		return (SmsMessage) findById(SmsMessage.class, smsMessageId);
	}

	/**
	 * Gets all the sms Message records
	 * 
	 * @return List of SmsMessage objects
	 */
	public List<SmsMessage> getAllSmsMessages() {
		Session s = HibernateUtil.currentSession();
		Query query = s.createQuery("from SmsMessage");
		List<SmsMessage> messages =  query.list();
		HibernateUtil.closeSession();
		return messages;
	}

	/**
	 * This method will persists the given object.
	 * 
	 * If the object is a new entity then it will be created on the DB. 
	 * If it is an existing entity then the record will be updated on the DB.
	 * 
	 * @param sms Message to be persisted
	 */
	public void persistSmsMessage(SmsMessage smsMessage) {
		persist(smsMessage);
	}

	/**
	 * Returns a message for the given smsc message id or null if nothing found
	 * 
	 * @param smsc message id
	 * @return sms message
	 */
	public SmsMessage getSmsMessageBySmscMessageId(String smscMessageId) {
		Session s = HibernateUtil.currentSession();
		Query query = s.createQuery("from SmsMessage");
		HibernateUtil.closeSession();
		List<SmsMessage> messages = query.list();
		if(messages != null && messages.size() > 0) {
			return messages.get(0);
		}
		return null;
	}
	
}
