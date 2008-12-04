/***********************************************************************************
 * DateUtil.java
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
package org.sakaiproject.sms.hibernate.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class containig date utility methods for the sakai sms project
 * 
 * @author julian@psybergate.com
 * @version 1.0
 * @created 01-Dec-2008
 */
public class DateUtil {

	private static final String DATE_TIME_FORMAT_STRING = "MM/dd/yyyy HH:mm:ss";

	private static final String DATE_FORMAT_STRING = "MM/dd/yyyy";

	private static SimpleDateFormat sdf = new SimpleDateFormat(
			DATE_TIME_FORMAT_STRING);

	/**
	 * Creates a Timestamp object from the startDate parameter for use in start
	 * dates searches
	 * 
	 * @param start
	 *            date
	 * @return time stamp for start date
	 * @throws ParseException
	 */
	public static Timestamp getTimestampFromStartDateString(String startDate)
			throws ParseException {
		startDate = startDate.concat(" 00:00:00");
		return new Timestamp(sdf.parse(startDate).getTime());
	}

	/**
	 * Creates a Timestamp object from the endDate parameter for use in end
	 * dates searches
	 * 
	 * @param end
	 *            date
	 * @return time stamp for end date
	 * @throws ParseException
	 */
	public static Timestamp getTimestampFromEndDateString(String endDate)
			throws ParseException {
		endDate = endDate.concat(" 23:59:59");
		return new Timestamp(sdf.parse(endDate).getTime());
	}

	/**
	 * Get string representing the specified date.
	 * 
	 * @param date
	 *            {@link Date}
	 * @return date as MM/dd/yyyy
	 */
	public static String getDateString(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_STRING);
		return dateFormat.format(date);
	}

}
