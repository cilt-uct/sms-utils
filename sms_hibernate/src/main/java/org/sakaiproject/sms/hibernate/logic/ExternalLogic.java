/***********************************************************************************
 * ExternalLogic.java
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

/**
 * Interface to logic which is external to SMS
 */
public interface ExternalLogic {

	// sample permissions for SMS
	// TODO: Discuss and complete this list

	public final static String SMS_SEND_SMS = "sms.send";
	public final static String SMS_ACCOUNT_CREATE = "sms.account.create";
	public final static String SMS_ACCOUNT_MODIFY = "sms.account.modify";
	public final static String SMS_CONFIG_MODIFY = "sms.config.modify";
	public final static String SMS_TASK_VIEW = "sms.task.view";
	public final static String SMS_MESSAGE_VIEW = "sms.message.view";
	public final static String SMS_TRANSACTION_VIEW = "sms.transaction.view";

	/**
	 * Check if this user has super admin access
	 * 
	 * @param userId
	 *            the internal user id (not username)
	 * @return true if the user has admin access, false otherwise
	 */
	public boolean isUserAdmin(String userId);

	/**
	 * Check if a user has a specified permission within a context, primarily a
	 * convenience method and passthrough
	 * 
	 * @param userId
	 *            the internal user id (not username)
	 * @param permission
	 *            a permission string constant
	 * @param locationId
	 *            a unique id which represents the current location of the user
	 *            (entity reference)
	 * @return true if allowed, false otherwise
	 */
	public boolean isUserAllowedInLocation(String userId, String permission,
			String locationId);
}
