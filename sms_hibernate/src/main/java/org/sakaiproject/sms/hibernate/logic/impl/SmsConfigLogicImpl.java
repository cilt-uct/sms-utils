/***********************************************************************************
 * SmsAccountLogicImpl.java
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

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.sakaiproject.sms.hibernate.dao.SmsDao;
import org.sakaiproject.sms.hibernate.logic.SmsConfigLogic;
import org.sakaiproject.sms.hibernate.model.SmsConfig;
import org.sakaiproject.sms.hibernate.model.constants.SmsHibernateConstants;
import org.sakaiproject.sms.hibernate.util.HibernateUtil;

/**
 * The data service will handle all sms config database transactions for the sms
 * tool in Sakai.
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
	 * @param Long
	 *            sms configuration id
	 * @return sms congiguration
	 */
	public SmsConfig getSmsConfig(Long smsConfigId) {
		return (SmsConfig) findById(SmsConfig.class, smsConfigId);
	}

	/**
	 * Gets all the sms configuration records
	 * 
	 * @return List of SmsConfig objects
	 */
	public List<SmsConfig> getAllSmsConfig() {
		List<SmsConfig> configs = new ArrayList<SmsConfig>();
		Session s = HibernateUtil.getSession();
		Query query = s.createQuery("from SmsConfig");
		configs = query.list();
		HibernateUtil.closeSession();
		return configs;
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
	public void persistSmsConfig(SmsConfig smsConfig) {
		persist(smsConfig);
	}

	/**
	 * Gets the sms config by sakai site id.
	 * 
	 * @param id
	 *            the id
	 * 
	 * @return the sms config by sakai site id
	 */
	public SmsConfig getSmsConfigBySakaiSiteId(String sakaiSiteId) {
		List<SmsConfig> configs = new ArrayList<SmsConfig>();
		SmsConfig config = null;
		Session s = HibernateUtil.getSession();
		Query query = s
				.createQuery("from SmsConfig conf where conf.sakaiSiteId = :id");
		query.setParameter("id", sakaiSiteId);
		configs = query.list();
		if (configs.size() == 1) {
			config = configs.get(0);
		}
		HibernateUtil.closeSession();
		if (config == null) {
			config = new SmsConfig();
			config
					.setGateWayReportTimeout(SmsHibernateConstants.GATEWAYREPORTTIMEOUT);
			config
					.setNotificationEmail(SmsHibernateConstants.NOTIFICATIONEMAIL);
			config
					.setNotificationEmailBilling("notificationBilling@instution.com");
			config.setNotificationEmailSent("notificationSent@instution.com");
			config.setPagingSize(SmsHibernateConstants.PAGINGSIZE);
			config.setSakaiSiteId(sakaiSiteId);
			config.setSakaiToolId("DummyToolId");
			config.setSendSmsEnabled(true);
			config.setSmsRetryMaxCount(SmsHibernateConstants.MAXIMUMRETRYCOUNT);
			config
					.setSmsRetryScheduleInterval(SmsHibernateConstants.RETRYSCHEDULEINTERVAL);
			config
					.setSmsTaskMaxLifeTime(SmsHibernateConstants.MAXIMUMTASKLIFETIME);
			persistSmsConfig(config);

		}
		return config;
	}

	/**
	 * Gets the sms config by sakai tool id.
	 * 
	 * @param id
	 *            the id
	 * 
	 * @return the sms config by sakai tool id
	 */
	public SmsConfig getSmsConfigBySakaiToolId(String id) {
		List<SmsConfig> configs = new ArrayList<SmsConfig>();
		SmsConfig config = null;
		Session s = HibernateUtil.getSession();
		Query query = s
				.createQuery("from SmsConfig conf where conf.sakaiToolId = :id");
		query.setParameter("id", id);
		configs = query.list();
		if (configs.size() == 1) {
			config = configs.get(0);
		}
		HibernateUtil.closeSession();
		return config;
	}

}
