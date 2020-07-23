/**
 * Copyright (c) 2008-2020 The Apereo Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *             http://opensource.org/licenses/ecl2
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sakaiproject.blogwow.sms;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.sakaiproject.blogwow.constants.BlogConstants;
import org.sakaiproject.blogwow.logic.BlogLogic;
import org.sakaiproject.blogwow.logic.EntryLogic;
import org.sakaiproject.blogwow.model.BlogWowBlog;
import org.sakaiproject.blogwow.model.BlogWowEntry;
import org.sakaiproject.sms.logic.incoming.ParsedMessage;
import org.sakaiproject.sms.logic.incoming.ShortMessageCommand;
import org.sakaiproject.util.ResourceLoader;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BlogSMSCommand implements ShortMessageCommand {
			
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
		String siteTitle = message.getSiteTitle() != null ? message.getSiteTitle() : message.getSite();
		
		String locationReference = "/site/" + siteId;
		
		if (! blogLogic.canWriteBlog(null, locationReference, userId)) {
			log.debug(userId + " can't blog in " + locationReference);
			return getResourceString("sms.notAllowed", new Object[]{ siteTitle });
		}
		
		BlogWowBlog blog = blogLogic.makeBlogByLocationAndUser(locationReference, userId);
				
		if (blog == null) {
			log.debug(userId + " can't create blog in " + locationReference);
			return getResourceString("sms.notAllowed", new Object[]{ siteTitle });		
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
		
		return getResourceString("sms.success", 
				new Object[]{ siteTitle } );
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
