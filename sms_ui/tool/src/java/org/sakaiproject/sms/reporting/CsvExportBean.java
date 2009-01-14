package org.sakaiproject.sms.reporting;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.sms.hibernate.bean.SearchFilterBean;
import org.sakaiproject.sms.hibernate.logic.impl.HibernateLogicFactory;
import org.sakaiproject.sms.hibernate.logic.impl.exception.SmsSearchException;
import org.sakaiproject.sms.params.DownloadReportViewParams;
import org.sakaiproject.sms.producers.TaskListProducer;
import org.sakaiproject.sms.util.BeanToCSVReflector;

public class CsvExportBean {

	
	private static Log log = LogFactory.getLog(CsvExportBean.class);
	private Map<String, CsvExportStrategy> csvExporters = new TreeMap<String, CsvExportStrategy>();
	

	public CsvExportBean() {
		csvExporters.put(TaskListProducer.VIEW_ID, new SmsTaskExportStrategy());
	}

	public boolean createCsv(DownloadReportViewParams viewparams, HttpServletResponse response) {
		
		System.err.println("Create CSV Data");
		
		if(log.isInfoEnabled())
			log.info("Create csv data");

		// Set the response headers
		response.setHeader("Content-disposition", "inline");
		response.setHeader("filename", viewparams.filename);
		response.setContentType("text/csv");

		try {
			 ServletOutputStream outputStream = response.getOutputStream();
			 createResponse(outputStream, viewparams);
		} catch (IOException e) {
			log.error("Failed to create csv output" + e);
			throw new RuntimeException("Failed to create csv output", e);
		}

		return true;
	}

	private void createResponse(ServletOutputStream outputStream, DownloadReportViewParams viewparams)
			throws IOException {
		
		SearchFilterBean searchFilterBean = viewparams.extractSearchFilter();
		csvExporters.get(viewparams.sourceView).createCsvData(outputStream, searchFilterBean);
	}

	private abstract class CsvExportStrategy{
		
		protected BeanToCSVReflector beanToCSVReflector = new BeanToCSVReflector();

		public void createCsvData(ServletOutputStream outputStream, SearchFilterBean searchFilterBean) {
			List<?> pageResults = null;	
			try {
				 pageResults = getCriteriaResults(searchFilterBean);
			} catch (SmsSearchException e) {
				throw new RuntimeException("Failed to obtain search results", e);
			}
	
			String csvText = beanToCSVReflector.toCSV(pageResults, getCsvColumns());
			
			try {
				outputStream.print(csvText);
			} catch (IOException e) {
				throw new RuntimeException("Failed to write csv to output stream");
			}			
		}
		
		public abstract String[] getCsvColumns();
		public abstract List<?> getCriteriaResults(SearchFilterBean searchFilterBean) throws SmsSearchException;
	}
	
	private final class SmsTaskExportStrategy extends CsvExportStrategy{

		private  final String[] taskListColumns = new String[]{
			"creditEstimate", 
			"dateCreated", 
			"dateProcessed", 
			"dateToSend",
			"deliveryGroupId",
			"deliveryGroupName",
			"deliveryUserId",
			"groupSizeActual",
			"groupSizeEstimate",
			"costEstimate",
			"messageBody",
			"messageTypeId",
			"attemptCount",
			"sakaiSiteId",
			"sakaiToolId",
			"sakaiToolName",
			"senderUserName",
			"smsAccountId",
			"statusCode",
			"maxTimeToLive",
			"delReportTimeoutDuration"};
		

		@Override
		public String[] getCsvColumns() {
			return taskListColumns;
		}

		@Override
		public List<?> getCriteriaResults(SearchFilterBean searchFilterBean) throws SmsSearchException {
			return HibernateLogicFactory.getTaskLogic().getAllSmsTasksForCriteria(searchFilterBean);
		}
	}
	
}
