package org.sakaiproject.sms.adaptor;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.authz.cover.SecurityService;
import org.sakaiproject.announcement.api.AnnouncementChannel;
import org.sakaiproject.announcement.api.AnnouncementMessageEdit;
import org.sakaiproject.announcement.api.AnnouncementMessageHeaderEdit;
import org.sakaiproject.announcement.cover.AnnouncementService;
import org.sakaiproject.event.api.NotificationService;
import org.sakaiproject.exception.IdUnusedException;
import org.sakaiproject.exception.PermissionException;
import org.sakaiproject.site.api.SiteService;
import org.sakaiproject.sms.logic.incoming.ParsedMessage;
import org.sakaiproject.sms.logic.incoming.ShortMessageCommand;
import org.sakaiproject.user.api.UserNotDefinedException;
import org.sakaiproject.user.cover.UserDirectoryService;
import org.sakaiproject.util.FormattedText;
import org.sakaiproject.util.ResourceLoader;

public class AnnounceSmsCommand implements ShortMessageCommand {
	
	private static Log log = LogFactory.getLog(AnnounceSmsCommand.class);
			
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
		msg.setBody(FormattedText.convertPlaintextToFormattedText(body[0].trim()));
		
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
