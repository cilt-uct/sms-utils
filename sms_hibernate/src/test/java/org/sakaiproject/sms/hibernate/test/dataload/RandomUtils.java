package org.sakaiproject.sms.hibernate.test.dataload;

import java.sql.Timestamp;
import java.util.Random;

public class RandomUtils {

	Random random = new Random();

	public RandomUtils() {
		super();
	}

	public Float getRandomFloat(int maxSize){	
		return random.nextFloat() * random.nextInt(maxSize);
	}
	
	public Timestamp getBoundRandomDate(int year){
		
		int month = random.nextInt(11) + 1;
		int day = random.nextInt(27) + 1;
		int hour = random.nextInt(23);
		int min = random.nextInt(59);
		int sec = random.nextInt(59);
		
		return Timestamp.valueOf(year + "-" + padNumber(month) + "-" + padNumber(day)
				+ " " + padNumber(hour) + ":" + padNumber(min) + ":" + padNumber(sec));
	}
	
	public Integer getRandomInteger(int maxSize){
		return random.nextInt(maxSize);
	}
	
	private String padNumber(int numToPad){
		if(numToPad < 10)
			return "0" + numToPad;
			
		return "" +numToPad;
			
	}
	
}
