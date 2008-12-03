/***********************************************************************************
 * SmsTransactionLogicImpl.java
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
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.sakaiproject.sms.hibernate.bean.SearchFilterBean;
import org.sakaiproject.sms.hibernate.dao.HibernateUtil;
import org.sakaiproject.sms.hibernate.dao.SmsDao;
import org.sakaiproject.sms.hibernate.logic.SmsTransactionLogic;
import org.sakaiproject.sms.hibernate.logic.impl.exception.SmsSearchException;
import org.sakaiproject.sms.hibernate.model.SmsTransaction;
import org.sakaiproject.sms.hibernate.util.DateUtil;

/**
 * The data service will handle all sms Transaction database transactions for
 * the sms tool in Sakai.
 * 
 * @author julian@psybergate.com
 * @version 1.0
 * @created 25-Nov-2008 08:12:41 AM
 */
public class SmsTransactionLogicImpl extends SmsDao implements
		SmsTransactionLogic {

	/**
	 * Deletes and the given entity from the DB
	 */
	public void deleteSmsTransaction(SmsTransaction smsTransaction) {
		delete(smsTransaction);
	}

	/**
	 * Gets a SmsTransaction entity for the given id
	 * 
	 * @param Long
	 *            sms transaction id
	 * @return sms congiguration
	 */
	public SmsTransaction getSmsTransaction(Long smsTransactionId) {
		return (SmsTransaction) findById(SmsTransaction.class, smsTransactionId);
	}

	/**
	 * Gets all the sms transaction records
	 * 
	 * @return List of SmsTransaction objects
	 */
	public List<SmsTransaction> getAllSmsTransactions() {
		Session s = HibernateUtil.currentSession();
		Query query = s.createQuery("from SmsTransaction");
		return query.list();
	}

	/**
	 * This method will persists the given object.
	 * 
	 * If the object is a new entity then it will be created on the DB. If it is
	 * an existing entity then the record will be updates on the DB.
	 * 
	 * @param sms
	 *            confuguration to be persisted
	 */
	public void persistSmsTransaction(SmsTransaction smsTransaction) {
		persist(smsTransaction);
	}

	/**
	 * Gets a list of SmsTransaction objects for the specified search criteria
	 * 
	 * @param search
	 *            Bean containing the search criteria
	 * @return List of SmsTransactions
	 * @throws SmsSearchException
	 *             when an invalid search criteria is specified
	 */
	public List<SmsTransaction> getSmsTransactionsForCriteria(
			SearchFilterBean searchBean) throws SmsSearchException {

		Criteria crit = HibernateUtil.currentSession().createCriteria(
				SmsTransaction.class);

		List<SmsTransaction> transactions = new ArrayList<SmsTransaction>();

		try {
			// Transaction type
			if (searchBean.getTransactionType() != null
					&& !searchBean.getTransactionType().trim().equals("")) {
				crit.add(Restrictions.ilike("transactionTypeCode", searchBean
						.getTransactionType()));
			}

			// Account number
			if (searchBean.getAccountNumber() != null) {
				crit.add(Restrictions.like("smsAccountId", searchBean
						.getAccountNumber()));
			}

			// Transaction date start
			if (searchBean.getDateFrom() != null
					&& !searchBean.getDateFrom().trim().equals("")) {
				Timestamp date = DateUtil
						.getTimestampFromStartDateString(searchBean
								.getDateFrom());
				crit.add(Restrictions.ge("transactionDate", date));
			}

			// Transaction date end
			if (searchBean.getDateTo() != null
					&& !searchBean.getDateTo().trim().equals("")) {
				Timestamp date = DateUtil
						.getTimestampFromEndDateString(searchBean.getDateTo());
				crit.add(Restrictions.le("transactionDate", date));
			}

			// Sender name
			if (searchBean.getSender() != null
					&& !searchBean.getSender().trim().equals("")) {
				crit.add(Restrictions.ilike("sakaiUserId", searchBean
						.getSender()));
			}

			// Ordering
//			if (searchBean.getOrderBy() != null
//					&& !searchBean.getOrderBy().trim().equals("")) {
//				crit.addOrder((searchBean.sortAsc() ? Order.asc(searchBean
//						.getOrderBy()) : Order.desc(searchBean.getOrderBy())));
//			}

		} catch (ParseException e) {
			throw new SmsSearchException(e);
		} catch (Exception e) {
			throw new SmsSearchException(e);
		}

		transactions = crit.list();
		HibernateUtil.closeSession();
		return transactions;
	}

}
