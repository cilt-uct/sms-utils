/***********************************************************************************
 * SMSConstants.java
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
package org.sakaiproject.sms.constants;

/**
 * TODO: merge all constant across SMS projects in relevant locations
 */
public class SMSConstants {

	// This should probably defined by SMS API
	public static final int MAX_SMS_LENGTH = 160;
	public static final int MAX_MOBILE_NR_LENGTH = 20; // Just arbitary
	
	//sort table constants
	public static final String SORT_DIR_ASC = "asc";
	public static final String SORT_DIR_DESC = "desc";

	//default paging
	public static final int DEFAULT_START_COUNT = 20;

	//Table Sort Directions
	public static final String SORT_ASC ="asc";
	public static final String SORT_DESC ="desc";	
	
	//Table Sort by Columns
	public static final String  SORT_BY_NAME = "name";
	public static final String  SORT_BY_HOUSE = "house";
	public static final String  SORT_BY_STREET = "street";
	
	
	
	
}
