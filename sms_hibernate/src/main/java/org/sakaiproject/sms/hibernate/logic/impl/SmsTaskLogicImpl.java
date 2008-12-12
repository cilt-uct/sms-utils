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

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.sakaiproject.sms.hibernate.bean.SearchFilterBean;
import org.sakaiproject.sms.hibernate.bean.SearchResultContainer;
import org.sakaiproject.sms.hibernate.dao.SmsDao;
import org.sakaiproject.sms.hibernate.logic.SmsTaskLogic;
import org.sakaiproject.sms.hibernate.logic.impl.exception.SmsSearchException;
import org.sakaiproject.sms.hibernate.model.SmsTask;
import org.sakaiproject.sms.hibernate.model.constants.SmsConst_DeliveryStatus;
import org.sakaiproject.sms.hibernate.model.constants.SmsHibernateConstants;
import org.sakaiproject.sms.hibernate.util.DateUtil;
import org.sakaiproject.sms.hibernate.util.HibernateUtil;

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
		Session s = HibernateUtil.getSession();
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
		hql.append(" and task.statusCode IN (:statusCodes) ");
		hql.append(" order by task.dateToSend ");
		Query query = HibernateUtil.getSession().createQuery(hql.toString());
		query.setParameter("today", getTimestampCurrent(), Hibernate.TIMESTAMP);
		query.setParameterList("statusCodes", new Object[] {
				SmsConst_DeliveryStatus.STATUS_PENDING,
				SmsConst_DeliveryStatus.STATUS_INCOMPLETE,
				SmsConst_DeliveryStatus.STATUS_RETRY }, Hibernate.STRING);
		log.debug("getNextSmsTask() HQL: " + query.getQueryString());
		List<SmsTask> tasks = query.list();
		HibernateUtil.closeSession();
		if (tasks != null && tasks.size() > 0) {
			// Gets the oldest dateToSend. I.e the first to be processed.
			return tasks.get(0);
		}
		return null;
	}

	/**
	 * Returns a list of SmsTask objects with messages that have the specified
	 * status code(s)
	 * 
	 * @param message
	 *            status code(s)
	 * @return List of SmsTask objetcs
	 * @deprecated Currently no use for this method.
	 */
	@Deprecated
	public List<SmsTask> getSmsTasksFilteredByMessageStatus(
			String... messageStatusCodes) {

		List<SmsTask> tasks = new ArrayList<SmsTask>();

		// Return empty list if no status codes were passed in
		if (messageStatusCodes.length > 0) {
			StringBuilder hql = new StringBuilder();
			hql.append(" from SmsTask task where task.id in ( ");
			hql
					.append(" 	select distinct message.smsTask.id from SmsMessage message where message.statusCode IN (:statusCodes) ) ");

			log.debug("getSmsTasksFilteredByMessageStatus() HQL: "
					+ hql.toString());
			Query query = HibernateUtil.getSession()
					.createQuery(hql.toString());
			query.setParameterList("statusCodes", messageStatusCodes,
					Hibernate.STRING);
			tasks = query.list();
			HibernateUtil.closeSession();
		}
		return tasks;
	}

	/**
	 * Gets a search results container housing the result set for a particular
	 * displayed page
	 * 
	 * @param searchBean
	 * @return Search result container
	 * @throws SmsSearchException
	 */
	public SearchResultContainer<SmsTask> getSmsTasksForCriteria(
			SearchFilterBean searchBean) throws SmsSearchException {

		Criteria crit = HibernateUtil.getSession()
				.createCriteria(SmsTask.class);

		List<SmsTask> tasks = new ArrayList<SmsTask>();

		try {
			// Message status
			if (searchBean.getStatus() != null
					&& !searchBean.getStatus().trim().equals("")) {
				crit.add(Restrictions.ilike("statusCode", searchBean
						.getStatus()));
			}

			// Sakai tool name
			if (searchBean.getToolName() != null
					&& !searchBean.getToolName().trim().equals("")) {
				crit.add(Restrictions.ilike("sakaiToolName", searchBean
						.getToolName(), MatchMode.ANYWHERE));
			}

			// Date to send start
			if (searchBean.getDateFrom() != null) {
				Timestamp date = DateUtil
						.getTimestampFromStartDateString(searchBean
								.getDateFrom());
				crit.add(Restrictions.ge("dateToSend", date));
			}

			// Date to send end
			if (searchBean.getDateTo() != null) {
				Timestamp date = DateUtil
						.getTimestampFromEndDateString(searchBean.getDateTo());
				crit.add(Restrictions.le("dateToSend", date));
			}

			// Sender name
			if (searchBean.getSender() != null
					&& !searchBean.getSender().trim().equals("")) {
				crit.add(Restrictions.ilike("senderUserName", searchBean
						.getSender(), MatchMode.ANYWHERE));
			}

			// Ordering
			if (searchBean.getOrderBy() != null
					&& !searchBean.getOrderBy().trim().equals("")) {
				crit.addOrder((searchBean.sortAsc() ? Order.asc(searchBean
						.getOrderBy()) : Order.desc(searchBean.getOrderBy())));
			}

			crit.setMaxResults(SmsHibernateConstants.READ_LIMIT);

		} catch (ParseException e) {
			throw new SmsSearchException(e);
		} catch (Exception e) {
			throw new SmsSearchException(e);
		}

		log.debug(crit.toString());
		SearchResultContainer<SmsTask> container = new SearchResultContainer<SmsTask>();
		tasks = crit.list();
		container.setTotalResultSetSize(new Long(tasks.size()));
		container.calculateAndSetPageResults(tasks, searchBean.getCurrentPage());

		HibernateUtil.closeSession();
		return container;
	}

}
