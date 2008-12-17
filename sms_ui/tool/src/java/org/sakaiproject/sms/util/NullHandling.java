package org.sakaiproject.sms.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NullHandling {

	
	public static String safeToString(String value){
		
		if(value == null)
			return "N/A";
		
		return value;
	}
	
	public static String safeToString(Number integer){
		
		if(integer == null)
			return "N/A";
		
		return integer.toString();
	}
	
	public static String safeToString(Timestamp timestamp){
		
		if(timestamp == null)
			return "N/A";
		
		return timestamp.toString();
	}
	
	public static boolean safeDateCheck(Date from, Date to){
		
		if(from == null || to == null)
			return false;
		
		if(from.compareTo(to) > 0)
			return false;
		
		return true;
	}

	public static String safeToStringFormated(Date dateProcessed) {
		if(dateProcessed == null)
			return "N/A";
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy/M/dd hh:mm");
		
		return format.format(dateProcessed);
	}

	public static String safeTruncatedToString(String value, int numberOfCharacters) {

		if(value == null)
			return "N/A";

		if(value.length() <= numberOfCharacters)
			return value;
		
		return value.substring(0, numberOfCharacters) + "...";
	}

	
}
