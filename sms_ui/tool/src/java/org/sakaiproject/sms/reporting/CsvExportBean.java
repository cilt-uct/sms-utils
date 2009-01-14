package org.sakaiproject.sms.reporting;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.sms.params.DownloadReportViewParams;

public class CsvExportBean {

	private static Log log = LogFactory.getLog(CsvExportBean.class);
	
	//Blank bean before implementation
	public boolean fireAction() {
		System.out.println("Fire CSV Action");
		return true;
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
			 outputStream.print("Test Download Text");
		} catch (IOException e) {
			log.error("Failed to create csv output" + e);
			throw new RuntimeException("Failed to create csv output", e);
		}

		return true;
	}
}
