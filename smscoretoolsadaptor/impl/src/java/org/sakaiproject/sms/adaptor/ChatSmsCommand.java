package org.sakaiproject.sms.adaptor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.chat2.model.ChatChannel;
import org.sakaiproject.chat2.model.ChatManager;
import org.sakaiproject.chat2.model.ChatMessage;
import org.sakaiproject.exception.PermissionException;
import org.sakaiproject.sms.logic.incoming.ParsedMessage;
import org.sakaiproject.sms.logic.incoming.ShortMessageCommand;
import org.sakaiproject.util.FormattedText;
import org.sakaiproject.util.ResourceLoader;

public class ChatSmsCommand implements ShortMessageCommand {
	
	private static Log log = LogFactory.getLog(ChatSmsCommand.class);
			
	private ChatManager chatManager;

	public void setChatManager(ChatManager chatManager) {
		this.chatManager = chatManager;
	}
	 
	// The command
	
	private static final String CHAT_COMMAND = "CHAT";
	private static final String CHAT_COMMAND_ALIAS = "C";
	private static final String SMS_BUNDLE = "org.sakaiproject.sms.adaptor.bundle.sms";
	
	public String execute(ParsedMessage message, String messageType, String mobileNumber) {
		
		String[] body = message.getBodyParameters();
		
		String userId = message.getIncomingUserId();
		String siteId = message.getSite();
		String siteTitle = message.getSiteTitle() != null ? message.getSiteTitle() : message.getSite();

		ChatChannel channel =   chatManager.getDefaultChannel(siteId, null);

		if (channel == null) {
			return getResourceString("sms.chat.nochannel", 
					new Object[]{ siteTitle } );
		}

		if (body.length == 0 || "".equals(body[0].trim())) {
			return getResourceString("sms.chat.empty", 
					new Object[]{ siteTitle } );
		}
		
		ChatMessage chatmsg;
		
		try {
			chatmsg = chatManager.createNewMessage(channel, userId);
		} catch (PermissionException e) {
			return getResourceString("sms.chat.notAllowed", 
					new Object[]{ siteTitle } );
		}

		chatmsg.setBody(FormattedText.convertPlaintextToFormattedText(body[0].trim()));

		chatManager.updateMessage(chatmsg);
		chatManager.sendMessage(chatmsg);

		return getResourceString("sms.chat.success", 
				new Object[]{ siteTitle } );
	}

	
	public String[] getAliases() {
		return new String[]{CHAT_COMMAND_ALIAS};
	}

	
	public int getBodyParameterCount() {
		return 1;
	}

	
	public String getCommandKey() {
		return CHAT_COMMAND;
	}

	
	public String getHelpMessage(String messageType) {
		return getResourceString("sms.chat.help");
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
