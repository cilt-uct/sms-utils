package org.sakaiproject.sms.producers;

import java.util.ArrayList;

/**
 *	Temp object to test table generation
 */
public class TestDataResultSet {

	public static ArrayList<TestDataRow> testDataSet() {

		TestDataRow row1 = new TestDataRow("Bob", "12", "Abbey");
		TestDataRow row2 = new TestDataRow("Ann", "18", "Won");
		TestDataRow row3 = new TestDataRow("Mark", "12", "Get");
		TestDataRow row4 = new TestDataRow("John", "77", "JL");
		TestDataRow row5 = new TestDataRow("Sam", "12", "Jord");

		
		ArrayList<TestDataRow> resultSet = new ArrayList<TestDataRow>();
		resultSet.add(row1);
		resultSet.add(row2);
		resultSet.add(row3);
		resultSet.add(row4);
		resultSet.add(row5);

		
		return resultSet;
	}

	public static class TestDataRow {
		private String name;
		private String house;
		private String street;

		public TestDataRow() {
			super();
		}

		public TestDataRow(String name, String house, String street) {
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

		public String getHouse() {
			return house;
		}

		public void setHouse(String house) {
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
