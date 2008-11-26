package org.sakaiproject.sms.renderers;


import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIInternalLink;
import uk.org.ponder.rsf.components.UIJointContainer;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.viewstate.ViewParameters;

public class TablePagerRenderer {

	public Integer currentStart = 1;
	public Integer currentCount = 1;
	
	public void createPager(UIContainer tofill, String divID, SortPagerViewParams sortParams, String viewId) {
	
		this.currentStart = sortParams.current_start;
		this.currentCount = sortParams.current_count;
		
		UIJointContainer joint = new UIJointContainer(tofill, divID, "table-pager-component:", ""+1);
		
		ViewParameters new_params = sortParams.copyBase();
		
		if(currentStart != 0){
			((SortPagerViewParams)new_params).current_start -= 1;
			UIInternalLink.make(joint, "prev", new_params);
		}
		
		UIOutput.make(joint, "page-count", " Page " + (currentStart+1)  + " of " + (currentCount+1) + " ");

		if(currentStart != currentCount){
			new_params = sortParams.copyBase();		
			((SortPagerViewParams)new_params).current_start += 1;
			UIInternalLink.make(joint, "next", new_params);
		}
	}
	

}
