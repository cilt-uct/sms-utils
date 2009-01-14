package org.sakaiproject.sms.reporting;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.sms.params.DownloadReportViewParams;

import uk.org.ponder.rsf.processor.HandlerHook;
import uk.org.ponder.rsf.viewstate.ViewParameters;

public class ReportHandlerHook implements HandlerHook{

	private static Log log = LogFactory.getLog(ReportHandlerHook.class);

	private CsvExportBean csvExportBean;
	private HttpServletResponse response;
	private ViewParameters viewparams;

	public void setViewparams(ViewParameters viewparams) {
		this.viewparams = viewparams;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public void setCsvExportBean(CsvExportBean csvExportBean) {
		this.csvExportBean = csvExportBean;
	}

	public boolean handle() {
		if (viewparams instanceof DownloadReportViewParams) {
			log.debug("Handing viewparams and response off to the csvExportBean");
			return csvExportBean.createCsv((DownloadReportViewParams) viewparams, response);
		}
		return false;
	}
}