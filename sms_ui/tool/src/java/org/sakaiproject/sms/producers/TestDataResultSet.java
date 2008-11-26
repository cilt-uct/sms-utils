package org.sakaiproject.sms.producers;

import java.util.ArrayList;

/**
 *	Temp object to test table generation
 */
public class TestDataResultSet {

	
	public static ArrayList<TestDataRow> testDataSet() {

		TestDataRow row1 = new TestDataRow("Bob", new Integer(12), "Abbey");
		TestDataRow row2 = new TestDataRow("Ann", new Integer(18), "Won");
		TestDataRow row3 = new TestDataRow("Mark", new Integer(12), "Get");
		TestDataRow row4 = new TestDataRow("John", new Integer(77), "JL");
		TestDataRow row5 = new TestDataRow("Sam", new Integer(12), "Jord");
		TestDataRow row6 = new TestDataRow("Max", new Integer(12), "Red");
		TestDataRow row7 = new TestDataRow("Rex", new Integer(12), "Blue");
		
		ArrayList<TestDataRow> resultSet = new ArrayList<TestDataRow>();
		resultSet.add(row1);
		resultSet.add(row2);
		resultSet.add(row3);
		resultSet.add(row4);
		resultSet.add(row5);
		resultSet.add(row6);
		resultSet.add(row7);

		
		return resultSet;
	}
	
	public static ArrayList<TestDataRow> getPage(int pageSize, int pageNum, ArrayList<TestDataRow> input){
		
		int fromIndex = pageSize*(pageNum);
		fromIndex = checkBounds(input, fromIndex);
		
		int toIndex = pageSize*(pageNum+1);
		toIndex = checkBounds(input, toIndex);
		
		
		return new ArrayList<TestDataRow>(input.subList(fromIndex, toIndex));
	}

	private static int checkBounds(ArrayList<TestDataRow> input, int index) {
		if(index > input.size())
			index = input.size();
		
		return index;
	}
	
	public static class TestDataRow {
		
		private String name;
		private Integer house;
		private String street;
		

		public TestDataRow() {
			super();
		}

		public TestDataRow(String name, Integer house, String street) {
			super();
			this.name = name;
			this.house = house;
			this.street = street;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Integer getHouse() {
			return house;
		}

		public void setHouse(Integer house) {
			this.house = house;
		}

		public String getStreet() {
			return street;
		}

		public void setStreet(String street) {
			this.street = street;
		}
	}
}
