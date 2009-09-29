package org.sakaiproject.blogwow.sms;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.blogwow.constants.BlogConstants;
import org.sakaiproject.blogwow.logic.BlogLogic;
import org.sakaiproject.blogwow.logic.EntryLogic;
import org.sakaiproject.blogwow.model.BlogWowBlog;
import org.sakaiproject.blogwow.model.BlogWowEntry;
import org.sakaiproject.sms.logic.incoming.ParsedMessage;
import org.sakaiproject.sms.logic.incoming.ShortMessageCommand;
import org.sakaiproject.util.ResourceLoader;

public class BlogSMSCommand implements ShortMessageCommand {
	
	private static Log log = LogFactory.getLog(BlogSMSCommand.class);
			
	//The command 
	private static final String BLOG_COMMAND = "BLOG";
	private static final String BLOG_COMMAND_ALIAS = "B";
	private static final String SMS_BUNDLE = "org.sakaiproject.blogwow.sms.bundle.sms";
	
	private BlogLogic blogLogic;
	private EntryLogic entryLogic;
	
	public void setBlogLogic(BlogLogic blogLogic) {
		this.blogLogic = blogLogic;
	}

	public void setEntryLogic(EntryLogic entryLogic) {
		this.entryLogic = entryLogic;
	}
	
	public String execute(ParsedMessage message, String messageType, String mobileNumber) {
		
		String[] body = message.getBodyParameters();
		
		// can the user blog in this location?
		String userId = message.getIncomingUserId();
		String siteId = message.getSite();
		
		String locationReference = "/site/" + siteId;
		
		if (! blogLogic.canWriteBlog(null, locationReference, userId)) {
			log.debug(userId + " can't blog in " + locationReference);
			return getResourceString("sms.notAllowed", new Object[]{siteId});
		}
		
		BlogWowBlog blog = blogLogic.makeBlogByLocationAndUser(locationReference, userId);
		
		if (blog == null) {
			log.debug(userId + " can't create blog in " + locationReference);
			return getResourceString("sms.notAllowed", new Object[]{siteId});		
		}
		
		BlogWowEntry entry = new BlogWowEntry();
		entry.setDateCreated(new Date());
		entry.setOwnerId(userId);
		entry.setBlog(blog);
		entry.setText(body[0]);
		entry.setTitle(getTitle(body[0]));
		entry.setPrivacySetting(BlogConstants.PRIVACY_PUBLIC);
		
		entryLogic.saveEntry(entry, locationReference);
		log.info("Posted blog entry from SMS: " + entry.getId());
		
		return getResourceString("sms.success");
	}

	private String getTitle(String string) {
		return StringUtils.abbreviate(string, 25);
	}

	
	public String[] getAliases() {
		return new String[]{BLOG_COMMAND_ALIAS};
	}

	
	public int getBodyParameterCount() {
		return 1;
	}

	
	public String getCommandKey() {
		return BLOG_COMMAND;
	}

	
	public String getHelpMessage(String messageType) {
		return getResourceString("sms.help");
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
