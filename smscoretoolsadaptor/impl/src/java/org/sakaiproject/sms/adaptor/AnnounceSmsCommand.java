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
package org.sakaiproject.sms.adaptor;

import org.apache.commons.lang3.StringUtils;
import org.sakaiproject.announcement.api.AnnouncementChannel;
import org.sakaiproject.announcement.api.AnnouncementMessageEdit;
import org.sakaiproject.announcement.api.AnnouncementMessageHeaderEdit;
import org.sakaiproject.announcement.cover.AnnouncementService;
import org.sakaiproject.authz.cover.SecurityService;
import org.sakaiproject.event.api.NotificationService;
import org.sakaiproject.exception.IdUnusedException;
import org.sakaiproject.exception.PermissionException;
import org.sakaiproject.site.api.SiteService;
import org.sakaiproject.sms.logic.incoming.ParsedMessage;
import org.sakaiproject.sms.logic.incoming.ShortMessageCommand;
import org.sakaiproject.user.api.UserNotDefinedException;
import org.sakaiproject.user.cover.UserDirectoryService;
import org.sakaiproject.util.ResourceLoader;
import org.sakaiproject.util.api.FormattedText;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AnnounceSmsCommand implements ShortMessageCommand {
	
	@Setter private FormattedText formattedText;
	//The command 
	private static final String ANNOUNCE_COMMAND = "ANNOUNCE";
	private static final String ANNOUNCE_COMMAND_ALIAS = "A";
	private static final String SMS_BUNDLE = "org.sakaiproject.sms.adaptor.bundle.sms";
	
	public String execute(ParsedMessage message, String messageType, String mobileNumber) {
		
		String[] body = message.getBodyParameters();
		
		// can the user post announcements in this location?
		String userId = message.getIncomingUserId();
		String siteId = message.getSite();
		String siteTitle = message.getSiteTitle() != null ? message.getSiteTitle() : message.getSite();

	    String channelId = AnnouncementService.channelReference(siteId, SiteService.MAIN_CONTAINER);

		if (! SecurityService.unlock(userId, AnnouncementService.SECURE_ANNC_ADD, channelId)) {
			log.debug(userId + " can't add announcement in " + channelId);
			return getResourceString("sms.announce.notAllowed", new Object[]{ siteTitle });
		}
				
		AnnouncementChannel aChan = null;
		try {
			aChan = AnnouncementService.getAnnouncementChannel(channelId);
		} catch (IdUnusedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PermissionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		AnnouncementMessageEdit msg = null;
		try {
			msg = aChan.addAnnouncementMessage();
		} catch (PermissionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int noti =  NotificationService.NOTI_NONE;
		msg.setBody(formattedText.convertPlaintextToFormattedText(body[0].trim()));
		
	     AnnouncementMessageHeaderEdit header = msg.getAnnouncementHeaderEdit();
         header.setSubject(getTitle(body[0]));
         try {
			header.setFrom(UserDirectoryService.getUser(userId));
		} catch (UserNotDefinedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        aChan.commitMessage(msg, noti, "org.sakaiproject.announcement.impl.SiteEmailNotificationAnnc");

		return getResourceString("sms.success", 
				new Object[]{ siteTitle } );
	}

	private String getTitle(String string) {
		return StringUtils.abbreviate(string, 25);
	}

	
	public String[] getAliases() {
		return new String[]{ANNOUNCE_COMMAND_ALIAS};
	}

	
	public int getBodyParameterCount() {
		return 1;
	}

	
	public String getCommandKey() {
		return ANNOUNCE_COMMAND;
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
