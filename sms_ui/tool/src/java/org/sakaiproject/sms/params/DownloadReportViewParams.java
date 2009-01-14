package org.sakaiproject.sms.params;

import uk.org.ponder.rsf.viewstate.SimpleViewParameters;

public class DownloadReportViewParams extends SimpleViewParameters{
	
	public String filename;

	public DownloadReportViewParams() {}

	public DownloadReportViewParams(String viewID, String filename) {
		this.viewID = viewID;
		this.filename = filename;
	}
}
