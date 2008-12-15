/***********************************************************************************
 * SmsDao.java - created by Sakai App Builder -AZ
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
package org.sakaiproject.sms.hibernate.dao;

import java.util.Date;

import org.apache.log4j.Level;
import org.sakaiproject.sms.hibernate.logic.SmsAccountLogic;
import org.sakaiproject.sms.hibernate.logic.SmsConfigLogic;
import org.sakaiproject.sms.hibernate.logic.SmsMessageLogic;
import org.sakaiproject.sms.hibernate.logic.SmsTaskLogic;
import org.sakaiproject.sms.hibernate.logic.SmsTransactionLogic;
import org.sakaiproject.sms.hibernate.logic.impl.SmsAccountLogicImpl;
import org.sakaiproject.sms.hibernate.logic.impl.SmsConfigLogicImpl;
import org.sakaiproject.sms.hibernate.logic.impl.SmsMessageLogicImpl;
import org.sakaiproject.sms.hibernate.logic.impl.SmsTaskLogicImpl;
import org.sakaiproject.sms.hibernate.logic.impl.SmsTransactionLogicImpl;

/**
 * Implementations of any specialized DAO methods from the specialized DAO that
 * allows the developer to extend the functionality of the generic dao package
 * 
 * @author Sakai App Builder -AZ
 */
public class SmsDao extends BaseDao {

	private SmsMessageLogic messageLogic;
	private SmsTaskLogic taskLogic;
	private SmsAccountLogic accountLogic;
	private SmsTransactionLogic transactionLogic;
	private SmsConfigLogic configLogic;

	static {
		log = org.apache.log4j.Logger.getLogger(SmsDao.class);
		init();
	}

	/** The log. */
	protected static org.apache.log4j.Logger log;

	/**
	 * Gets the current Date.
	 * 
	 * @return the current Date
	 */
	protected Date getCurrentDate() {
		return new Date(System.currentTimeMillis());
	}

	/**
	 * Inits the.
	 */
	public static void init() {
		log.setLevel(Level.ALL);
		log.debug("init");
	}

	/**
	 * Gets the message logic.
	 * 
	 * @return the message logic
	 */
	protected SmsMessageLogic getMessageLogic() {
		if (messageLogic == null) {
			messageLogic = new SmsMessageLogicImpl();
		}
		return messageLogic;
	}

	/**
	 * Gets the task logic.
	 * 
	 * @return the task logic
	 */
	protected SmsTaskLogic getTaskLogic() {
		if (taskLogic == null) {
			taskLogic = new SmsTaskLogicImpl();
		}
		return taskLogic;
	}

	/**
	 * Gets the account logic.
	 * 
	 * @return the account logic
	 */
	protected SmsAccountLogic getAccountLogic() {
		if (accountLogic == null) {
			accountLogic = new SmsAccountLogicImpl();
		}
		return accountLogic;
	}

	/**
	 * Gets the transaction logic.
	 * 
	 * @return the transaction logic
	 */
	protected SmsTransactionLogic getTransactionLogic() {
		if (transactionLogic == null) {
			transactionLogic = new SmsTransactionLogicImpl();
		}
		return transactionLogic;
	}

	/**
	 * Gets the config logic.
	 * 
	 * @return the config logic
	 */
	protected SmsConfigLogic getConfigLogic() {
		if (configLogic == null) {
			configLogic = new SmsConfigLogicImpl();
		}
		return configLogic;
	}
}
