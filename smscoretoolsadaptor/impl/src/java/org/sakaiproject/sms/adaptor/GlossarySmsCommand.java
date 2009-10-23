package org.sakaiproject.sms.adaptor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.sms.logic.incoming.ParsedMessage;
import org.sakaiproject.sms.logic.incoming.ShortMessageCommand;
import org.sakaiproject.util.ResourceLoader;
import org.theospi.portfolio.help.model.Glossary;
import org.theospi.portfolio.help.model.GlossaryEntry;

public class GlossarySmsCommand implements ShortMessageCommand {
	
	private static Log log = LogFactory.getLog(GlossarySmsCommand.class);
			
	private Glossary glossary;
	
	public void setGlossary(Glossary glossary) {
		this.glossary = glossary;
	}

	private static final String GLOSSARY_COMMAND = "GLOSSARY";
	private static final String GLOSSARY_COMMAND_ALIAS = "DEFINE";
	private static final String SMS_BUNDLE = "org.sakaiproject.sms.adaptor.bundle.sms";
	
	public String execute(ParsedMessage message, String messageType, String mobileNumber) {
		
		String[] body = message.getBodyParameters();
		
		String siteId = message.getSite();
		String siteTitle = message.getSiteTitle() != null ? message.getSiteTitle() : message.getSite();

		GlossaryEntry entry = glossary.find(body[0], siteId);
		
		if (entry == null) {
			return getResourceString("sms.glossary.notfound", 
					new Object[]{ siteTitle, body[0] } );			
		}
		
		String defn = entry.getDescription();
		
		return getResourceString("sms.glossary.result", 
				new Object[]{ siteTitle, body[0], defn } );
	}

	
	public String[] getAliases() {
		return new String[]{GLOSSARY_COMMAND_ALIAS};
	}

	
	public int getBodyParameterCount() {
		return 1;
	}

	
	public String getCommandKey() {
		return GLOSSARY_COMMAND;
	}

	
	public String getHelpMessage(String messageType) {
		return getResourceString("sms.glossary.help");
	}

	public boolean isEnabled() {
		return false;
	}
	
	public boolean isVisible() {
		return true;
	}

	
	public boolean requiresSiteId() {
		return true;
	}

	public boolean requiresUserId() {
		return true;
	}

	private String getResourceString(String key) {
		final ResourceLoader rb = new ResourceLoader(SMS_BUNDLE);
		return rb.getString(key);	
	}
	
	
	private String getResourceString(String key, Object[] replacementValues) {
		final ResourceLoader rb = new ResourceLoader(SMS_BUNDLE);
		return rb.getFormattedMessage(key, replacementValues);
		
	}

	public boolean canExecute(ParsedMessage message) {
		return true;
	}
}
