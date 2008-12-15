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
import java.util.Calendar;
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

	private static final long MILLS_PER_WEEK = 10080000;

	private static SimpleDateFormat sdf = new SimpleDateFormat(
			DATE_TIME_FORMAT_STRING);

	/**
	 * Calculate the date one week before the current date
	 * 
	 * 
	 * @param date
	 * @return the date a week before
	 */
	public static Date getDateOneWeekBeforeNow() {
		return getDateFromNow(-7);
	}

	
	
	/**
	 * Get the date at the specified number of days from now
	 * 
	 * @param daysFromNow
	 * @return
	 */
	public static Date getDateFromNow(int daysFromNow) {
		Calendar date1 = Calendar.getInstance();
		date1.add(Calendar.DAY_OF_YEAR, daysFromNow);
		Date weekBefore = date1.getTime();
		return weekBefore;
	}

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
		return getUsableTimeStamp(new Timestamp(sdf.parse(startDate).getTime()));
	}

	/**
	 * Creates a Timestamp object from the startDate parameter for use in start
	 * dates searches
	 * 
	 * @param start
	 *            date
	 * @return time stamp for start date
	 * @throws ParseException
	 */
	public static Timestamp getTimestampFromStartDateString(Date startDate)
			throws ParseException {
		String sStartDate = getDateString(startDate) + (" 00:00:00");
		return getUsableTimeStamp(new Timestamp(sdf.parse(sStartDate).getTime()));
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
		return getUsableTimeStamp(new Timestamp(sdf.parse(endDate).getTime()));
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
	public static Timestamp getTimestampFromEndDateString(Date endDate)
			throws ParseException {
		String sEndDate = getDateString(endDate) + (" 23:59:59");
		return getUsableTimeStamp(new Timestamp(sdf.parse(sEndDate).getTime()));
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

	/**
	 * Gets a usable time stamp.
	 * <p>
	 * When hibernate save a model class to the DB, MySql zero's the
	 * milliseconds. This caused inconsistensies in the equeals methods of the
	 * model classes. We zero the millisecond because it makes things consistent
	 * and becuase it's insignificant for our use.
	 * 
	 * @param dateCreated
	 *            the date created
	 * 
	 * @return the usable time stamp
	 */
	public static Timestamp getUsableTimeStamp(Timestamp timeStamp) {
		if (timeStamp != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(timeStamp.getTime());
			cal.set(Calendar.MILLISECOND, 0);
			return new Timestamp(cal.getTimeInMillis());
		}
		return null;
	}

	/**
	 * Gets usable timestamp with the current date and time.
	 * 
	 * @return the current timestamp
	 */
	public static Timestamp getCurrentTimestamp() {
		return getUsableTimeStamp(new Timestamp(System.currentTimeMillis()));
	}

}
