/***********************************************************************************
 * SmsTransactionLogic.java
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

package org.sakaiproject.sms.hibernate.logic;

import java.util.List;

import org.sakaiproject.sms.hibernate.bean.SearchFilterBean;
import org.sakaiproject.sms.hibernate.logic.impl.exception.SmsSearchException;
import org.sakaiproject.sms.hibernate.model.SmsTransaction;

/**
 * The data service will handle all sms Account database transactions for the sms tool in
 * Sakai.
 * 
 * @author julian@psybergate.com
 * @version 1.0
 * @created 25-Nov-2008 08:12:41 AM
 */
public interface SmsTransactionLogic {
	/**
	 * Deletes and the given entity from the DB
	 */
	public void deleteSmsCongif(SmsTransaction smsTransaction);

	/**
	 * Gets a SmsTransaction entity for the given id
	 * 
	 * @param Long sms transaction id
	 * @return sms congiguration
	 */
	public SmsTransaction getSmsTransaction(Long smsTransactionId);

	/**
	 * Gets all the sms transaction records
	 * 
	 * @return List of SmsTransaction objects
	 */
	public List<SmsTransaction> getAllSmsTransactions();

	/**
	 * This method will persists the given object.
	 * 
	 * If the object is a new entity then it will be created on the DB. 
	 * If it is an existing entity then the record will be updates on the DB.
	 * 
	 * @param sms confuguration to be persisted
	 */
	public void persistSmsTransaction(SmsTransaction smsTransaction);
	

	/**
	 * Gets a list of SmsTransaction objects for the specified search criteria
	 * 
	 * @param search Bean containing the search criteria
	 * @return List of SmsTransactions
	 */
	public List<SmsTransaction> getSmsTransactionsForCriteria(SearchFilterBean searchBean) throws SmsSearchException;
}
