package org.sakaiproject.sms.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import sun.reflect.misc.FieldUtil;

/**
 * A Utility class to convert a List of objects to a CSV list
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
		Field[] fieldArray = FieldUtil.getDeclaredFields(objectToReflectOn.getClass());
		
		fieldArray = createOrderedFieldArray(fieldArray, fieldNames);
		
		
		Method[] method =  objectToReflectOn.getClass().getMethods();
		for (int i = 0; i < method.length; i++) {
			System.err.println(method[i].getName());
		}
		
		
		
		for (int i = 0; i < fieldArray.length; i++) {
			fieldArray[i].setAccessible(true);
		}
		
		for (Object object : list) {
			for (int i = 0; i < fieldArray.length; i++) {
				try {
					String value = fieldArray[i].get(object).toString();
					
					if(i == fieldArray.length - 1){
						buffer.append(value + "\n");
					}
					else{
						buffer.append(value + ",");
					}
					
				} catch (Exception e) {
					throw new RuntimeException("Faile to obtain value from field"  + fieldArray[i].toString());
				} 
			}
		}
			
		return buffer;
	}

	private Field[] createOrderedFieldArray(Field[] fieldArray, String[] fieldNames) {
		
		if(fieldNames == null){
			return fieldArray;
		}
		
		Map<String, Field> fieldMap = new TreeMap<String, Field>();
		for (int i = 0; i < fieldArray.length; i++) {
			fieldMap.put(fieldArray[i].getName(), fieldArray[i]);
		}
		
		Field[] fieldsToReflectOn = new Field[fieldNames.length];
		for (int i = 0; i < fieldNames.length; i++) {
			
			Field tempField = fieldMap.get(fieldNames[i]);
			
			if(tempField == null){
				throw new RuntimeException("The Field value " + fieldNames[i]  + " does not exsist in the supplied Object List");
			}
			
			fieldsToReflectOn[i] = tempField;
		}
		
		return fieldsToReflectOn;
	}

}
