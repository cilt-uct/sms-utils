/***********************************************************************************
 * SmsHibernateConstants.java
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
package org.sakaiproject.sms.hibernate.model.constants;

/**
 * Constants class for the sms hibernate project
 * 
 * @author Julian Wyngaard
 * @version 1.0
 * @created 08-Dec-2008
 */
public class SmsHibernateConstants {

	/** The Constant SORT_ASC. */
	public static final String SORT_ASC = "asc";

	/** The Constant SORT_DESC. */
	public static final String SORT_DESC = "desc";

	/** The Constant DEFAULT_PAGE_SIZE. */
	public static final int DEFAULT_PAGE_SIZE = 20;

	/** The Constant READ_LIMIT. */
	public static final int READ_LIMIT = 100;

	/** The Constant MESSAGE_TYPE_OUTGOING. */
	public static final Integer MESSAGE_TYPE_OUTGOING = 0;

	/** The Constant MESSAGE_TYPE_INCOMING. */
	public static final Integer MESSAGE_TYPE_INCOMING = 1;

	/**
	 * Used to indicate if the system is running in development mode.
	 */
	public static boolean SMS_DEV_MODE = true;

	/**
	 * The default sakai_id to be used in development mode.
	 */
	public static final String SMS_DEV_DEFAULT_SAKAI_ID = "SakaiSiteID";

	/** Enable or disable sms functionality for the site or tool. */
	public static final boolean SMS_ENABLED_FOR_SITE = true;

	/**
	 * The email address that will receive the notification of errors or
	 * insufficient credits.
	 */
	public static final String NOTIFICATION_EMAIL = "notification@instution.com";

	/**
	 * The sms gateway report timeout in seconds. If a delivery report takes
	 * longer that this, the message will be marked as failed.
	 */
	public static final Integer GATEWAY_REPORT_TIMEOUT = (60 * 10);

	/**
	 * The maximum amount of time an smsTask can be retried.
	 */
	public static final Integer MAXIMUM_RETRY_COUNT = 5;

	/**
	 * The message may only be delivered in the time frame defined by
	 * [dateToSend] + [MAXIMUMTASKLIFETIME]
	 */
	public static final Integer MAXIMUM_TASK_LIFETIME = 60 * 60 * 24;

	/**
	 * The time to wait before retrying an smsTask.
	 */
	public static final Integer RETRY_SCHEDULE_INTERVAL = 60 * 5;

	/**
	 * Default paging size
	 */
	public static final Integer PAGING_SIZE = 200;

}
