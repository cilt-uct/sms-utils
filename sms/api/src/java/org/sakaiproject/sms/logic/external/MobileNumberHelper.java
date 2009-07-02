/**********************************************************************************
 * $URL:$
 * $Id:$
 ***********************************************************************************
 *
 * Copyright (c) 2006, 2007 The Sakai Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 **********************************************************************************/
package org.sakaiproject.sms.logic.external;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface MobileNumberHelper {
	
	/**
	 * Retrieves mobile number for user
	 * 
	 * @param userid user id to retrieve 
	 * @return user's mobile number of null if not available / unset.
	 */
	String getUserMobileNumber(String userid);
	
	/**
	 * Retrieves mobile numbers of multiple users
	 * 
	 * @param userids list of userid's
	 * @return returns map of mobile numbers. Userid is key, value is mobile number or null if
	 *	unset/unavailable.
	 */
	Map<String,String> getUserMobileNumbers(List<String> userids);

	/**
	 * Retrieve userIds with supplied mobile number
	 * 
	 * @param mobileNumber
	 * @return
	 */
	List<String> getUserIdsFromMobileNumber(String mobileNumber);
	
	/**
	 * Retrieve userIds for users who have mobile numbers
	 * @param userids
	 * @return
	 */
	List<String> getUsersWithMobileNumbers(Set<String> userids);

	/**
	 * Normalize a number e.g convert numbers startign with 0 to the int format (e.g 072... to 2772)
	 * @param mobileNumber
	 * @return
	 */
	public String normalizeNumber(String mobileNumber);
	
}
