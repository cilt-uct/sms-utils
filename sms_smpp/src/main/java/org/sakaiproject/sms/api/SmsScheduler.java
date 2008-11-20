/***********************************************************************************
 * SmsScheduler.java
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

package org.sakaiproject.sms.api;

import java.util.Date;

/**
 * Use Quartz to run at predefined intervals to process the SMS task queue. The
 * service can run in a concurrent environment.
 * 
 * @author louis@psybergate.com
 * @version 1.0
 * @created 12-Nov-2008
 */
public interface SmsScheduler {

	/**
	 * Get the time of the last run. For diagnostic purposes.
	 */
	public Date getLastProcessTime();

	/**
	 * Get the current status of the scheduler.
	 */
	public int getStatus();

	// log all activities
	public void insertIntoDebugLog();

	/**
	 * Fire on timer to process the task queue. Apply row locking (software) on
	 * message queue. Use the SmsCore service to do the actual work.
	 */
	public void processQueue();

	/**
	 * Force a immediate process of the message queue.
	 */
	public void processQueueNow();

	/**
	 * Set the interval of the scheduler.
	 */
	public void setInterval(int seconds);
}