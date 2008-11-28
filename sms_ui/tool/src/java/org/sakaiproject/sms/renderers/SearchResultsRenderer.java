package org.sakaiproject.sms.renderers;

import uk.org.ponder.rsf.components.UIContainer;

public interface SearchResultsRenderer {

	public void createTable(UIContainer tofill, String divID, SortPagerViewParams sortViewParams, String viewID);
}