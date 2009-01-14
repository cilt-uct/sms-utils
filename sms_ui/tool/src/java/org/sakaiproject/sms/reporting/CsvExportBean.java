package org.sakaiproject.sms.reporting;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.sms.hibernate.bean.SearchFilterBean;
import org.sakaiproject.sms.hibernate.logic.SmsMessageLogic;
import org.sakaiproject.sms.hibernate.logic.SmsTaskLogic;
import org.sakaiproject.sms.hibernate.logic.SmsTransactionLogic;
import org.sakaiproject.sms.hibernate.logic.impl.exception.SmsSearchException;
import org.sakaiproject.sms.hibernate.model.SmsTask;
import org.sakaiproject.sms.params.DownloadReportViewParams;
import org.sakaiproject.sms.producers.TaskListProducer;
import org.sakaiproject.sms.util.BeanToCSVReflector;

public class CsvExportBean {

	private static String[] taskListColumns = new String[]{
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

	
	private static Log log = LogFactory.getLog(CsvExportBean.class);
	
	private BeanToCSVReflector beanToCSVReflector;
	
	private SmsTaskLogic smsTaskLogic;
	private SmsMessageLogic smsMessageLogic;
	private SmsTransactionLogic smsTransactionLogic;

	
	public void setSmsTaskLogic(SmsTaskLogic smsTaskLogic) {
		this.smsTaskLogic = smsTaskLogic;
	}

	public void setSmsMessageLogic(SmsMessageLogic smsMessageLogic) {
		this.smsMessageLogic = smsMessageLogic;
	}

	public void setSmsTransactionLogic(SmsTransactionLogic smsTransactionLogic) {
		this.smsTransactionLogic = smsTransactionLogic;
	}

	public CsvExportBean() {
		beanToCSVReflector = new BeanToCSVReflector();
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
		if(viewparams.sourceView.equals(TaskListProducer.VIEW_ID)){
			createTaskListCsv(outputStream, searchFilterBean);
		}

		
	}
	
	
	private void createTaskListCsv(ServletOutputStream outputStream, SearchFilterBean searchFilterBean) {
		
		List<SmsTask> pageResults = null;	
		try {
			 pageResults = smsTaskLogic.getSmsTasksForCriteria(searchFilterBean).getPageResults();
		} catch (SmsSearchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			String csvText = beanToCSVReflector.toCSV(pageResults, taskListColumns);
			outputStream.print(csvText);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


//	private abstract class CsvExportStrategy{
//		
//		protected SearchFilterBean searchFilterBean;
//
//		public void setSearchFilterBean(SearchFilterBean searchFilterBean) {
//			this.searchFilterBean = searchFilterBean;
//		}
//		
//		public abstract void createResponse(ServletOutputStream outputStream);
//	}
//	
//	private class SmsTaskExportStrategy extends CsvExportStrategy{
//
//		@Override
//		public void createResponse(ServletOutputStream outputStream) {
//			
//			try {
//				smsTaskLogic.getSmsTasksForCriteria(searchFilterBean);
//			} catch (SmsSearchException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//		}
//		
//	}

	
	

	
	
}
