package org.sakaiproject.sms.util;

import java.sql.Timestamp;
import java.util.Date;

public class NullHandling {

	public static String safeToString(Number integer){
		
		if(integer == null)
			return "N/A";
		else
			return integer.toString();
	}
	
	public static String safeToString(Timestamp timestamp){
		
		if(timestamp == null)
			return "N/A";
		else
			return timestamp.toString();
	}
	
	public static boolean safeDateCheck(Date from, Date to){
		
		if(from == null || to == null)
			return false;
		
		if(from.compareTo(to) > 0)
			return false;
		
		return true;
	}
	
}
