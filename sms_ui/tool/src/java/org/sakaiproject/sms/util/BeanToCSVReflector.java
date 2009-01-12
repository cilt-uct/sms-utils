package org.sakaiproject.sms.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * A Utility class to convert a List of java beans to a CSV list does not fetch nested objects
 *
 */
public class BeanToCSVReflector {


	public StringBuffer toCSV(List<?> list) {
		return toCSV(list, (String[])null);
	}

	public StringBuffer toCSV(List<?> list, String[] fieldNames) {
		
		StringBuffer buffer = new StringBuffer();
		
		if(list.size() == 0){
			return buffer;
		}
		
		Object objectToReflectOn = list.get(0);
		Method[] methodArray =  getAllPublicAccesorMethods(objectToReflectOn);
		methodArray = createOrderedMethodArray(methodArray, fieldNames);
		
		
		for (Object object : list) {
			for (int i = 0; i < methodArray.length; i++) {
				try {
					String value = methodArray[i].invoke(object).toString();
					
					if(i == methodArray.length - 1){
						buffer.append(value + "\n");
					}
					else{
						buffer.append(value + ",");
					}
					
				} catch (Exception e) {
					throw new RuntimeException("Faile to obtain value from method"  + methodArray[i].toString());
				} 
			}
		}
			
		return buffer;
	}

	private Method[] getAllPublicAccesorMethods(Object objectToReflectOn) {
		
		Method[] allMethods = objectToReflectOn.getClass().getMethods();
		ArrayList<Method> accesorMethods = new ArrayList<Method>();

		for (int i = 0; i < allMethods.length; i++) {
			
			String methodName = allMethods[i].getName();
			
			if(methodName.startsWith("get"))
				accesorMethods.add(allMethods[i]);
		}
		
		Method[] methodList = new Method[accesorMethods.size()];
		
		for(int i = 0 ; i < methodList.length ; i++){
			methodList[i] = accesorMethods.get(i);
		}
		
		return methodList;
	}

	private Method[] createOrderedMethodArray(Method[] methodArray, String[] fieldNames) {
		
		
		if(fieldNames == null){
			return methodArray;
		}
		
		Map<String, Method> methodMap = new TreeMap<String, Method>();
		for (int i = 0; i < methodArray.length; i++) {
			
			String methodName = methodArray[i].getName();
			String fieldName = convertMethodNameToFieldName(methodName);			
			methodMap.put(fieldName, methodArray[i]);
		}
		
		Method[] methodsToInvoke = new Method[fieldNames.length];
		for (int i = 0; i < fieldNames.length; i++) {
			
			Method tempMethod = methodMap.get(fieldNames[i]);
			
			if(tempMethod == null){
				throw new RuntimeException("The Field value " + fieldNames[i]  + " does not exsist in the supplied Object List");
			}
			
			methodsToInvoke[i] = tempMethod;
		}
		
		return methodsToInvoke;
	}

	private String convertMethodNameToFieldName(String methodName) {
		//remove "get"
		methodName = methodName.substring(3);
		
		if(methodName.length() >= 2)
			methodName = methodName.substring(0, 1).toLowerCase() + methodName.substring(1);
		else
			methodName = methodName.toUpperCase();
		return methodName;
	}

}
