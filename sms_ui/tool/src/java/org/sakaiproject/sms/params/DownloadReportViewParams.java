package org.sakaiproject.sms.params;

import uk.org.ponder.rsf.viewstate.SimpleViewParameters;

public class DownloadReportViewParams extends SimpleViewParameters{
	
	public Long templateId; 
	public String filename;

	public DownloadReportViewParams() {}

	public DownloadReportViewParams(String viewID, Long templateId, String filename) {
		this.viewID = viewID;
		this.templateId = templateId;
		this.filename = filename;
	}
}
