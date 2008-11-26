/***********************************************************************************
 * SmsTaskLogicImpl.java
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

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.sakaiproject.sms.hibernate.dao.HibernateUtil;
import org.sakaiproject.sms.hibernate.dao.SmsDao;
import org.sakaiproject.sms.hibernate.logic.SmsTaskLogic;
import org.sakaiproject.sms.hibernate.model.SmsTask;
import org.sakaiproject.sms.hibernate.model.constants.SmsConst_DeliveryStatus;

/**
 * The data service will handle all sms task database transactions for the sms
 * tool in Sakai.
 * 
 * @author julian@psybergate.com
 * @version 1.0
 * @created 25-Nov-2008
 */
public class SmsTaskLogicImpl extends SmsDao implements SmsTaskLogic {

	/**
	 * Deletes and the given entity from the DB
	 */
	public void deleteSmsTask(SmsTask smsTask) {
		delete(smsTask);
	}

	/**
	 * Gets a SmsTask entity for the given id
	 * 
	 * @param Long
	 *            sms task id
	 * @return sms task
	 */
	public SmsTask getSmsTask(Long smsTaskId) {
		return (SmsTask) findById(SmsTask.class, smsTaskId);
	}

	/**
	 * Gets all the sms task records
	 * 
	 * @return List of SmsTask objects
	 */
	public List<SmsTask> getAllSmsTask() {
		Session s = HibernateUtil.currentSession();
		Query query = s.createQuery("from SmsTask");
		List<SmsTask> tasks = query.list();
		HibernateUtil.closeSession();
		return tasks;
	}

	/**
	 * This method will persists the given object.
	 * 
	 * If the object is a new entity then it will be created on the DB. If it is
	 * an existing entity then the record will be updated on the DB.
	 * 
	 * @param sms
	 *            task to be persisted
	 */
	public void persistSmsTask(SmsTask smsTask) {
		persist(smsTask);
	}

	/**
	 * Gets the next sms task to be processed.
	 * 
	 * @return next sms task
	 */
	public SmsTask getNextSmsTask() {
		StringBuilder hql = new StringBuilder();
		hql.append(" from SmsTask task where task.dateToSend <= :today ");
		hql.append(" and task.statusCode = :statusCode ");
		hql.append(" order by task.dateToSend ");
		log.debug("getNextSmsTask() HQL: " + hql.toString());
		Query query = HibernateUtil.currentSession()
				.createQuery(hql.toString());
		query.setParameter("today", getTimestampCurrent(), Hibernate.TIMESTAMP);
		query.setParameter("statusCode",
				SmsConst_DeliveryStatus.STATUS_PENDING, Hibernate.STRING);
		List<SmsTask> tasks = query.list();
		HibernateUtil.closeSession();
		if (tasks != null && tasks.size() > 0) {
			// Gets the oldest dateToSend. I.e the first to be processed.
			return tasks.get(0);
		}
		return null;
	}
}
