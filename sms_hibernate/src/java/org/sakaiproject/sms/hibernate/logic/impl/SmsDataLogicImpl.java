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
import org.sakaiproject.sms.hibernate.logic.SmsDataLogic;
import org.sakaiproject.sms.hibernate.model.SmsAccount;
import org.sakaiproject.sms.hibernate.model.SmsConfig;
import org.sakaiproject.sms.hibernate.model.SmsMessage;
import org.sakaiproject.sms.hibernate.model.SmsTask;
import org.sakaiproject.sms.hibernate.model.SmsTransaction;


public class SmsDataLogicImpl extends SmsDao implements SmsDataLogic {

	public SmsAccount getSmsAccount(Long smsAccountID) {
		// TODO Auto-generated method stub
		return null;
	}

	public SmsConfig getSmsConfig() {
		// TODO Auto-generated method stub
		return null;
	}

	public SmsMessage getSmsMessage(Long smsMessageID) {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<SmsMessage> getSmsMessages(Long smsTaskID) {
		// TODO Auto-generated method stub
		return null;
	}

	public SmsTask getSmsTask(Long smsTaskID) {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<SmsTask> getSmsTasks(String sakaiSiteID, String statusCode) {
		// TODO Auto-generated method stub
		return null;
	}

	public SmsTransaction getSmsTransaction(Long smsTransactionID) {
		return (SmsTransaction) findById(SmsTransaction.class, smsTransactionID);
	}

	public Set<SmsTransaction> getSmsTransactions(Long smsAccountID) {
		// TODO Auto-generated method stub
		return null;
	}

	

}
