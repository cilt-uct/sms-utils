
package org.sakaiproject.sms.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.MethodUtils;
import org.sakaiproject.sms.constants.SortDirection;

public class ReflectionBasedSorter {

	public static <T> ArrayList<T> sortByName(ArrayList<T> list, final String fieldToSort, final SortDirection sortDirection){

		Collections.sort(list, new Comparator<T>(){

			public int compare(T o1, T o2) {
				
				Object valueLeft, valueRight;
				try {
					valueLeft = BeanUtils.getProperty(o1, fieldToSort);
					valueRight = BeanUtils.getProperty(o2, fieldToSort);
				} 
				catch (Exception e) {
					throw new RuntimeException(e);
				}
						
				Integer compare;
				try {
					
					if(sortDirection == sortDirection.ASC)
						compare = (Integer) MethodUtils.invokeExactMethod(valueLeft, "compareTo", valueRight);
					else
						compare = (Integer) MethodUtils.invokeExactMethod(valueRight, "compareTo", valueLeft);
						
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
				
				return compare.intValue();
			}	
		});

		return list;
	}
	
}
