/***********************************************************************************
 * SmsTaskLogic.java
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
import org.sakaiproject.sms.hibernate.model.SmsTask;
import org.sakaiproject.sms.hibernate.model.SmsTransaction;

/**
 * The data service will handle all sms task database transactions for the sms
 * tool in Sakai.
 * 
 * @author julian@psybergate.com
 * @version 1.0
 * @created 25-Nov-2008
 */
public interface SmsTaskLogic {

	/**
	 * Delete sms task.
	 * 
	 * @param smsTask
	 *            the sms task
	 */
	public void deleteSmsTask(SmsTask smsTask);

	/**
	 * Gets the sms task.
	 * 
	 * @param smsTaskId
	 *            the sms task id
	 * 
	 * @return the sms task
	 */
	public SmsTask getSmsTask(Long smsTaskId);

	/**
	 * Gets the next sms task to be processed.
	 * 
	 * @return the next sms task
	 */
	public SmsTask getNextSmsTask();

	/**
	 * Gets the all sms task.
	 * 
	 * @return the all sms task
	 */
	public List<SmsTask> getAllSmsTask();

	/**
	 * Persist sms task.
	 * 
	 * @param smsTask
	 *            the sms task
	 */
	public void persistSmsTask(SmsTask smsTask);

	/**
	 * Gets a list of SmsTask objects for the specified search criteria
	 * 
	 * @param search Bean containing the search criteria
	 * @return List of SmsTasks
	 */
	public List<SmsTask> getSmsTasksForCriteria(SearchFilterBean searchBean) throws SmsSearchException;

	/**
	 * Gets the sms tasks filtered by one or more message statuses.
	 * 
	 * @param messageStatusCode
	 *            the message status code
	 * 
	 * @return the sms tasks filtered by message status
	 */
	public List<SmsTask> getSmsTasksFilteredByMessageStatus(
			String... messageStatusCode);

}
