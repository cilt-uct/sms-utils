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

import org.sakaiproject.chat2.model.ChatChannel;
import org.sakaiproject.chat2.model.ChatManager;
import org.sakaiproject.chat2.model.ChatMessage;
import org.sakaiproject.exception.PermissionException;
import org.sakaiproject.sms.logic.incoming.ParsedMessage;
import org.sakaiproject.sms.logic.incoming.ShortMessageCommand;
import org.sakaiproject.util.ResourceLoader;
import org.sakaiproject.util.api.FormattedText;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChatSmsCommand implements ShortMessageCommand {

	@Setter private ChatManager chatManager;
	@Setter private FormattedText formattedText;

	 
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

		chatmsg.setBody(formattedText.convertPlaintextToFormattedText(body[0].trim()));

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
