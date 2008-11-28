package org.sakaiproject.sms.renderers;

import org.sakaiproject.sms.hibernate.bean.SearchFilterBean;

import uk.org.ponder.rsf.components.UIContainer;

public interface SearchResultsRenderer {

	public void setSearchFilterBean(SearchFilterBean searchFilterBean);
	public void createTable(UIContainer tofill, String divID, SortPagerViewParams sortViewParams, String viewID);

}