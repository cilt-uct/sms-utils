package org.sakaiproject.sms.util;

import java.sql.Timestamp;

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
	
}
