package org.sakaiproject.sms.util;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import junit.framework.TestCase;

public class BeanToCSVReflectorTest extends TestCase{

	private BeanToCSVReflector beanToCSVReflector;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		beanToCSVReflector = new BeanToCSVReflector();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testSimpleResultSetNoOrder() throws Exception {
		
		List<SimpleResultSet> list = createSimpleTestRow();
		
		StringBuffer buffer = beanToCSVReflector.toCSV(list);
		
		String csvRow = buffer.toString();

		
		assertTrue(csvRow.contains(","));
		assertTrue(csvRow.contains("Row 1"));
		assertTrue(csvRow.contains("2000"));
		assertTrue(csvRow.contains("255"));
	}

	public void testSimpleResultSetWithOrder() throws Exception {
		List<SimpleResultSet> list = createSimpleTestRow();
		String[] fieldNames = new String []{ "longValue", "label"};
		
		StringBuffer csvBuffer = beanToCSVReflector.toCSV(list, fieldNames);
		String csvRow = csvBuffer.toString();
		
		assertTrue(csvRow.length() > 0);
		
		StringTokenizer stringTokenizer = new StringTokenizer(csvRow, ",");
		
		assertEquals("2000", stringTokenizer.nextElement());
		assertEquals("Row 1\n", stringTokenizer.nextElement());
	}
	
	public void testExtendedResultNoOrder() throws Exception {
		
		List<ExtendedResultSet> list = createExtendedTestRow();
		StringBuffer buffer = beanToCSVReflector.toCSV(list);
		String extendCsvRow = buffer.toString();
		
		assertTrue(extendCsvRow.contains("Row 1"));
		assertTrue(extendCsvRow.contains("More info"));
	}
	
	private List<ExtendedResultSet> createExtendedTestRow(){
		ExtendedResultSet extendedResultSet = new ExtendedResultSet("Row 1", new Long(2000), 255, "More info");
		List<ExtendedResultSet> list = new ArrayList<ExtendedResultSet>();
		list.add(extendedResultSet);
		return list;
	}
	
	
	private List<SimpleResultSet> createSimpleTestRow() {
		SimpleResultSet simpleResultSet1 = new SimpleResultSet("Row 1", new Long(2000), 255);
		List<SimpleResultSet> list = new ArrayList<SimpleResultSet>();
		list.add(simpleResultSet1);
		return list;
	}
	
	static class SimpleResultSet{
		
		private String label;
		private int number;
		private Long longValue;

		public SimpleResultSet(String label, Long longValue, int number) {
			super();
			this.label = label;
			this.longValue = longValue;
			this.number = number;
		}
		
		public String getLabel() {
			return label;
		}
		public void setLabel(String label) {
			this.label = label;
		}
		public int getNumber() {
			return number;
		}
		public void setNumber(int number) {
			this.number = number;
		}
		public Long getLongValue() {
			return longValue;
		}
		public void setLongValue(Long longValue) {
			this.longValue = longValue;
		}
	}
	
	static class ExtendedResultSet extends SimpleResultSet{

		public String additionalInfo;
		
		public ExtendedResultSet(String label, Long longValue, int number, String addtionalInfo) {
			super(label, longValue, number);
			this.additionalInfo = addtionalInfo;
		}

		public String getAdditionalInfo() {
			return additionalInfo;
		}

		public void setAdditionalInfo(String additionalInfo) {
			this.additionalInfo = additionalInfo;
		}	
	}
	
	
}
