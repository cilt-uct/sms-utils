package org.sakaiproject.sms.tool.producers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.sms.logic.external.ExternalLogic;
import org.sakaiproject.sms.logic.hibernate.SmsAccountLogic;
import org.sakaiproject.sms.logic.hibernate.SmsTaskLogic;
import org.sakaiproject.sms.model.hibernate.SmsAccount;
import org.sakaiproject.sms.model.hibernate.SmsTask;
import org.sakaiproject.sms.tool.params.SmsParams;

import uk.org.ponder.messageutil.MessageLocator;
import uk.org.ponder.rsf.components.UIBoundBoolean;
import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIInput;
import uk.org.ponder.rsf.components.UILink;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.components.UIOutputMany;
import uk.org.ponder.rsf.components.UISelect;
import uk.org.ponder.rsf.components.UISelectChoice;
import uk.org.ponder.rsf.components.UISelectLabel;
import uk.org.ponder.rsf.components.decorators.UIFreeAttributeDecorator;
import uk.org.ponder.rsf.components.decorators.UIIDStrategyDecorator;
import uk.org.ponder.rsf.components.decorators.UILabelTargetDecorator;
import uk.org.ponder.rsf.components.decorators.UITooltipDecorator;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParamsReporter;

public class ChooseRecipientsProducer implements ViewComponentProducer, ViewParamsReporter {
	
	public static Log log = LogFactory.getLog(ChooseRecipientsProducer.class);
	
	public static final String VIEW_ID = "choose-recipients";
	
	
	public String getViewID() {
		return VIEW_ID;
	}
	
	private ExternalLogic externalLogic;
	public void setExternalLogic(ExternalLogic externalLogic) {
		this.externalLogic = externalLogic;
	}

	private SmsAccountLogic smsAccountLogic;
	public void setSmsAccountLogic(SmsAccountLogic smsAccountLogic) {
		this.smsAccountLogic = smsAccountLogic;
	}
	
	private MessageLocator messageLocator;
	public void setMessageLocator(MessageLocator messageLocator) {
		this.messageLocator = messageLocator;
	}
	
	private SmsTaskLogic smsTaskLogic;
	public void setSmsTaskLogic(SmsTaskLogic smsTaskLogic) {
		this.smsTaskLogic = smsTaskLogic;
	}

	public void fillComponents(UIContainer tofill, ViewParameters viewparams,
			ComponentChecker checker) {
		log.info("VIEW"+VIEW_ID);
		//view variables
		String currentSiteId = externalLogic.getCurrentSiteId();
		String currentUserId = externalLogic.getCurrentUserId();
		SmsAccount smsAccount = smsAccountLogic.getSmsAccount(currentSiteId, currentUserId);
		
		if ( ! "".equals(smsAccount.getCredits()) && smsAccount.getCredits() != 0 ){
			
			SmsParams smsParams = (SmsParams) viewparams;
			SmsTask smsTask = new SmsTask();
			if ( smsParams.id != null && ! "".equals(smsParams.id) ){
				smsTask = smsTaskLogic.getSmsTask(Long.parseLong(smsParams.id));
			}
			
			//Filling tab areas on condition
			fillTabs( tofill, new String[] {"roles", "groups", "names", "numbers"});
			
			UIForm form = UIForm.make(tofill, "form", new SmsParams("/direct/sms-task/new"));
			//Checkboxes for Groups and Roles
			Map<String, String> roles = new HashMap<String, String>();
			roles = externalLogic.getSakaiRolesForSite(currentSiteId);
			if ( roles.size() > 0 ){
			
				List<String> rolesValues = new ArrayList<String>(); 
				List<String> rolesLabels = new ArrayList<String>(); 
				UISelect rolesBoxes = UISelect.makeMultiple(form, "role-holder", new String[] {}, null, smsTask.getId() == null ? new String[] {} : new String[] {} ); //TODO: Retrieve list of user Role selections
				String rolesBoxesId = rolesBoxes.getFullID();
				
				Iterator<Map.Entry<String, String>> selector = roles.entrySet().iterator();
				int count = 0;
				while ( selector.hasNext() ) {
                	Map.Entry<String, String> pairs = selector.next();
                	String id = (String) pairs.getValue();
                	rolesValues.add(id);
                	String name = (String) pairs.getValue();
                	rolesLabels.add(name);
                	
                	UIBranchContainer row = UIBranchContainer.make(form, "role-row:", count + "");
                	UISelectChoice choice = UISelectChoice.make(row, "role-box", rolesBoxesId, count);
                	UISelectLabel label = UISelectLabel.make(row, "role-label", rolesBoxesId, count);
                	label.decorate(new UIFreeAttributeDecorator("rolesname", name));
                	label.decorate(new UIFreeAttributeDecorator("name", name));
                	label.decorate(new UIFreeAttributeDecorator("rolesid", id));
                    UILabelTargetDecorator.targetLabel(label, choice);
                	count ++;
				}
				
				rolesBoxes.optionlist = UIOutputMany.make(rolesValues.toArray( new String[rolesValues.size()] ));
				rolesBoxes.optionnames = UIOutputMany.make(rolesLabels.toArray( new String[rolesLabels.size()] ));
				rolesBoxes.selection.fossilize = false;
				}
			else{
				//TODO: Show message that there are no roles in site
			}
			
			Map<String, String> groups = new HashMap<String, String>();
			groups = externalLogic.getSakaiGroupsForSite(currentSiteId);
			if ( groups.size() > 0 ){
			
				List<String> groupsValues = new ArrayList<String>(); 
				List<String> groupsLabels = new ArrayList<String>(); 
				UISelect groupsBoxes = UISelect.makeMultiple(form, "group-holder", new String[] {}, null, smsTask.getId() == null ? new String[] {} : new String[] {} ); //TODO: Retrieve list of user group selections
				String groupBoxesId = groupsBoxes.getFullID();
				
				Iterator<Map.Entry<String, String>> selector = groups.entrySet().iterator();
				int count = 0;
				while ( selector.hasNext() ) {
                	Map.Entry<String, String> pairs = selector.next();
                	String id = (String) pairs.getValue();
                	groupsValues.add(id);
                	String name = (String) pairs.getValue();
                	groupsLabels.add(name);
                	
                	UIBranchContainer row = UIBranchContainer.make(form, "group-row:", count + "");
                	UISelectChoice choice = UISelectChoice.make(row, "group-box", groupBoxesId, count);
                	UISelectLabel label = UISelectLabel.make(row, "group-label", groupBoxesId, count);
                	label.decorate(new UIFreeAttributeDecorator("groupsname", name));
                	label.decorate(new UIFreeAttributeDecorator("name", name));
                	label.decorate(new UIFreeAttributeDecorator("groupsid", id));
                    UILabelTargetDecorator.targetLabel(label, choice);
                	count ++;
				}
				
				groupsBoxes.optionlist = UIOutputMany.make(groupsValues.toArray( new String[groupsValues.size()] ));
				groupsBoxes.optionnames = UIOutputMany.make(groupsLabels.toArray( new String[groupsLabels.size()] ));
				groupsBoxes.selection.fossilize = false;
				}
			else{
				//TODO: Show message that there are no groups in site
			}
			//Textearee for the numbers
			UIInput.make(form, "numbers-box", null)
				.decorate(new UIIDStrategyDecorator("peopleListNumbersBox"));
			
			UIInput.make(form, "names-box", null, "")
				.decorate(new UIFreeAttributeDecorator("name", "sakaiUserIds"));
			UICommand.make(form, "calculate", UIMessage.make("ui.recipients.choose.continue"), null)
			.decorate(new UIIDStrategyDecorator("calculateCmd"));
		
			
			//copy me checkbox
			UIBoundBoolean copy = UIBoundBoolean.make(form, "copy-me", Boolean.FALSE);
			UIMessage.make(form, "copy-me-label", "ui.recipients.choose.copy")
				.decorate(new UILabelTargetDecorator(copy));
			
			if ( smsTask.getId() != null ){
				UIInput.make(tofill, "id", smsTask.getId() + "", null);
			}
			UIInput.make(tofill, "sakaiSiteId", null, currentSiteId)
				.fossilize = false;
			UIInput.make(tofill, "senderUserName", null, externalLogic.getSakaiUserDisplayName(currentUserId))
				.fossilize = false;
			UIInput.make(tofill, "senderUserId", null, currentUserId)
				.fossilize = false;
			
			UICommand.make(form, "continue", UIMessage.make("ui.recipients.choose.continue"), null)
				.decorate(new UIIDStrategyDecorator("recipientsCmd"));
			
		}else{
			UIMessage.make(tofill, "error", "ui.error.cannot.create");
			UICommand.make(tofill, "error-back", UIMessage.make("sms.general.cancel"));
			UIMessage.make(tofill, "error-help", "ui.console.help");
			UIOutput.make(tofill, "error-email"); //TODO show email for credit purchases
		}
		
	}

	private void fillTabs(UIContainer tofill, String[] tabs) {
		log.info("tabs:"+tabs.length);
		for ( int i=0; i < tabs.length; i++){
			String tab = tabs[i];
			log.info("tab:"+tab);
			
			UIBranchContainer branch = UIBranchContainer.make(tofill, tab + ":");
			UILink.make(branch, tab + "-title", messageLocator.getMessage("ui.recipients.choose.prefix") + messageLocator.getMessage("ui.recipients.choose." + tab + ".title"), null); //TODO check that this preserves the HTML defined href
			UIOutput.make(branch, tab + "-selected", 0 + "")
				.decorate(new UITooltipDecorator(UIMessage.make("ui.recipients.choose.selected.tooltip", new Object[] { messageLocator.getMessage("ui.recipients.choose." + tab + ".title") })));
		}
	}

	public ViewParameters getViewParameters() {
		// TODO Auto-generated method stub
		return new SmsParams();
	}
	
}
