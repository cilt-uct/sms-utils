/**********************************************************************************
 * $URL: $
 * $Id: $
 ***********************************************************************************
 *
 * Copyright (c) 2006, 2007 The Sakai Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 **********************************************************************************/
package org.sakaiproject.sms.logic.incoming.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.sms.logic.external.ExternalLogic;
import org.sakaiproject.sms.logic.hibernate.HibernateLogicLocator;
import org.sakaiproject.sms.logic.incoming.DuplicateCommandKeyException;
import org.sakaiproject.sms.logic.incoming.ParsedMessage;
import org.sakaiproject.sms.logic.incoming.SmsCommand;
import org.sakaiproject.sms.logic.incoming.SmsIncomingLogicManager;
import org.sakaiproject.sms.logic.incoming.SmsMessageParser;
import org.sakaiproject.sms.logic.parser.exception.ParseException;
import org.sakaiproject.sms.model.hibernate.constants.SmsConstants;
import org.sakaiproject.sms.model.smpp.SmsPatternSearchResult;

public class SmsIncomingLogicManagerImpl implements SmsIncomingLogicManager {

	// Collection for keeping tool ids with their logic
	private final HashMap<String, RegisteredCommands> toolCmdsMap = new HashMap<String, RegisteredCommands>();

	private final RegisteredCommands allCommands = new RegisteredCommands();

	private static final Log log = LogFactory
	.getLog(SmsIncomingLogicManagerImpl.class);

	private HibernateLogicLocator hibernateLogicLocator;

	public HibernateLogicLocator getHibernateLogicLocator() {
		return hibernateLogicLocator;
	}

	public void setHibernateLogicLocator(
			HibernateLogicLocator hibernateLogicLocator) {
		this.hibernateLogicLocator = hibernateLogicLocator;
	}

	private ExternalLogic externalLogic;

	public void setExternalLogic(ExternalLogic externalLogic) {
		this.externalLogic = externalLogic;
	}

	private SmsMessageParser smsMessageParser;

	public void setSmsMessageParser(SmsMessageParser smsMessageParser) {
		this.smsMessageParser = smsMessageParser;
	}

	public ParsedMessage process(String smsMessageBody, String mobileNumber) {

		ParsedMessage parsedMessage = new ParsedMessage();
		String reply = null;
		Locale incomingUserLocale = null;
		
		// Get the user(s) matching the incoming mobile number
		
		final List<String> userIds = externalLogic.getUserIdsFromMobileNumber(mobileNumber);

		// Get the user locale of the first user which matches (for now)
		
		if (!userIds.isEmpty()) {
			incomingUserLocale = externalLogic.getUserLocale(userIds.get(0));
		}

		if (toolCmdsMap.isEmpty()) {
			// No tools registered
			reply = generateNoCommandsMessage(incomingUserLocale);
			parsedMessage = new ParsedMessage();
			parsedMessage.setBody(smsMessageBody);
			parsedMessage.setBodyReply(formatReply(reply));
			return parsedMessage;
		}
		
		// Get the command
		
		parsedMessage.setBody(smsMessageBody);
		
		try {
			smsMessageParser.parseCommand(parsedMessage);
		} catch (ParseException e) {
			// Null message or empty body
			reply = generateUnknownCommandMessage(parsedMessage, incomingUserLocale);
			parsedMessage.setBodyReply(formatReply(reply));
			return parsedMessage;
		}

		if (parsedMessage.getCommand() == null) {
			// an empty sms was received
			reply = generateHelpMessage(incomingUserLocale);
			parsedMessage.setBody("");
			parsedMessage.setBodyReply(formatReply(reply));
			return parsedMessage;
		}
		
		// Match the command
		
		final String suppliedCommand = parsedMessage.getCommand().toUpperCase();
		SmsPatternSearchResult validCommandMatch = findValidCommand(suppliedCommand, allCommands);
		
		if (validCommandMatch == null || 
			validCommandMatch.getPattern() == null ||
			validCommandMatch.getMatchResult() == null) {
			
			// Unknown command
			reply = generateUnknownCommandMessage(parsedMessage, incomingUserLocale);
			parsedMessage.setBodyReply(formatReply(reply));
			parsedMessage.setCommand(null);
			return parsedMessage;
		}

		if (SmsConstants.HELP.equalsIgnoreCase(validCommandMatch.getPattern())) {
			
			// HELP command
			reply = generateHelpMessage(incomingUserLocale);

		} else if (validCommandMatch.getMatchResult().equals(
				SmsPatternSearchResult.NO_MATCHES)) {
			
			// No matches
			parsedMessage.setCommand(null);
			reply = generateAssistMessage(validCommandMatch.getPossibleMatches(), incomingUserLocale);
			
		} else if (validCommandMatch.getMatchResult().equals(
				SmsPatternSearchResult.MORE_THAN_ONE_MATCH)) {
						
			// Ambiguous
			parsedMessage.setCommand(null);
			reply = generateAssistMessage(validCommandMatch.getPossibleMatches(), incomingUserLocale);
			
		} else { 
						
			// VALID command

			// The canonical command for the supplied command (if uniquely identified)
			parsedMessage.setCommand(validCommandMatch.getPattern());
			
			final SmsCommand cmd = allCommands.getCommand(validCommandMatch.getPattern());

			// Get the site if required
			
			if (cmd.requiresSiteId()) {
				try {
					smsMessageParser.parseSite(parsedMessage);
				} catch (ParseException e) {
					reply = cmd.getHelpMessage();
					parsedMessage.setBodyReply(formatReply(reply));
					
					return parsedMessage;
				}
			} 
			
			// Get the best match for the user and site name / abbreviation	
			if (!getSiteAndUser(parsedMessage, userIds, cmd)) {
				reply = generateInvalidSiteMessage("", incomingUserLocale);
				parsedMessage.setBodyReply(formatReply(reply));
				return parsedMessage;				
			}
			
			// We may have resolved multiple matching users into one, so set the locale again
			incomingUserLocale = externalLogic.getUserLocale(parsedMessage.getIncomingUserId());
			
			// We can execute the command
			try {

				// Get the parameters
				
				smsMessageParser.parseBody(parsedMessage, 
						cmd.getBodyParameterCount(), cmd.requiresSiteId());

				// Execute the message in the appropriate security context									
				reply = externalLogic.executeCommand(cmd, parsedMessage, mobileNumber);

			} catch (ParseException pe) {
				
				if (cmd.isVisible()) {
					// Body parameter count wrong
					reply = cmd.getHelpMessage();
				}
			}
		}
		
		parsedMessage.setBodyReply(formatReply(reply));

		return parsedMessage;
	}

	/**
	 * Finds the best match for user and site, and sets these values in the message.
	 * 
	 * @param suppliedSiteId
	 *            as specified by message
	 * @return
	 */
	private boolean getSiteAndUser(ParsedMessage message, List<String> userIds, SmsCommand cmd) {

		// Get a match for the given site, possible user matches, and command

		String suppliedSiteId = message.getSite();
		
		if (suppliedSiteId == null && cmd.requiresSiteId()) {
			return false;
		}

		// TODO - fix for the case where we select the user based on site membership
		// and tool usage
		if (!userIds.isEmpty()) {
			message.setIncomingUserId(userIds.get(0));
		}

		if (!cmd.requiresSiteId()) {
			return true;
		}
		
		// Lookup by site id
		if (externalLogic.isValidSite(suppliedSiteId)) {
			message.setSite(suppliedSiteId);
			return true;
		}

		// Lookup by site alias
		String siteId = externalLogic.getSiteFromAlias(suppliedSiteId);
		if (siteId != null) {
			message.setSite(siteId);
			return true;
		}

		// Match on site alias
		SmsPatternSearchResult result = getClosestMatch(suppliedSiteId,
				externalLogic.getAllSiteAliases());
		if (result.getMatchResult().equals(
				SmsPatternSearchResult.ONE_MATCH)) {
			message.setSite(externalLogic.getSiteFromAlias(result.getPattern()));
			return true;
		}
		
		// Nothing found - no match or ambiguous
		
		return false;
	}

	/**
	 * Format reply to be returned - truncate to max allowed length
	 */
	private String formatReply(String reply) {
		if (reply == null) {
			return null;
		}
		// Just cut off extra characters
		return StringUtils.left(reply, SmsConstants.MAX_SMS_LENGTH);
	}

	/**
	 * Returns an ArrayList of possible strings that match the requested
	 * command. The idea is based on the Levenshtein distance. Our algorithm
	 * assign the highest scores to matches on the left hand side of the word.
	 * And if the first letter does not match, then its a 0% match for the word.
	 * 
	 * @param valueToMatch
	 * @param values
	 * @return
	 */
	private List<String> getPossibleMatches(String valueToMatch,
			List<String> values) {

		List<String> returnVals = new ArrayList<String>();
		valueToMatch = valueToMatch.toUpperCase();

		String returnVal = null;

		// We first check for matching parts.
		List<String> matchedValues = new ArrayList<String>();

		for (String str : values) {
			if (str.toUpperCase().indexOf(valueToMatch) != -1) {
				matchedValues.add(str);
			}
		}

		if (matchedValues.isEmpty()) {
			// no matching substrings, so look through the whole list
			matchedValues = values;
		}

		// We calculate the largest string's length to be used as weights.
		int largestString = 0;
		for (String str : matchedValues) {
			if (str.length() > largestString) {
				largestString = str.length();
			}
		}

		// We loop through each command and pattern. Left hand matching
		// chars score more points. If the first chars don't match we break the
		// loop.
		int maxStringScore = 0;

		for (String str : matchedValues) {
			boolean skipCommand = false;
			int patternScore = 0;
			char[] commandChars = str.toUpperCase().toCharArray();
			char[] validCommands = valueToMatch.toCharArray();
			int startChar = 0;
			for (int i = 0; i < validCommands.length; i++) {
				if (skipCommand) {
					break;
				}
				for (int e = startChar; e < commandChars.length; e++) {
					if (i == 0 && validCommands[0] != commandChars[0]) {
						skipCommand = true;
						break;
					}
					if (validCommands[i] == commandChars[e]) {
						startChar = e + 1;
						patternScore += (largestString - (e));
						break;
					}
				}
			}

			if (patternScore != 0 && patternScore == maxStringScore) {
				returnVals.add(str);
			}

			if (patternScore > maxStringScore) {
				maxStringScore = patternScore;
				returnVal = str;
			}
		}

		if (returnVal != null) {
			returnVals.add(returnVal);
		}

		return returnVals;
	}

	/**
	 * Finds valid command EXACTLY as it is specified in command keys or HELP,
	 * or find the closest match.
	 * 
	 * @return valid command
	 */
	private SmsPatternSearchResult findValidCommand(String suppliedKey,
			RegisteredCommands commands) {
		
		SmsPatternSearchResult smsPatternSearchResult = null;
		String aliasedCommand = findAlias(suppliedKey, commands);
		if (aliasedCommand != null) {
			suppliedKey = aliasedCommand;
		}

		if (SmsConstants.HELP.equalsIgnoreCase(suppliedKey)) {
			return new SmsPatternSearchResult(SmsConstants.HELP);
		} else {
			if (commands.getCommand(suppliedKey) == null) { 
				// None found in command keys
				List<String> validCommands = new ArrayList<String>(commands.getCommandKeys());
				smsPatternSearchResult = getClosestMatch(suppliedKey,validCommands);
			} else {
				smsPatternSearchResult = new SmsPatternSearchResult(suppliedKey);
			}

			return smsPatternSearchResult;
		}
	}

	/**
	 * Tries to command on alias map (returns command if found)
	 */
	private String findAlias(String supplied, RegisteredCommands commands) {
		// ? is default alias for HELP
		if ("?".equals(supplied)) {
			return SmsConstants.HELP;
		}

		return commands.findAliasCommandKey(supplied);
	}

	public void register(String toolKey, SmsCommand command) {
		toolKey = toolKey.toUpperCase();

		if (SmsConstants.HELP.equalsIgnoreCase(command.getCommandKey())) {
			log.error(SmsConstants.HELP + " is a reserved command key");
		} else if (allCommands.getCommandKeys().contains(
				command.getCommandKey().toUpperCase())) {
			throw new DuplicateCommandKeyException(command.getCommandKey()
					.toUpperCase());
		} else {
			// Toolkey not yet registered
			if (toolCmdsMap.containsKey(toolKey)) {
				// If it tool exist
				RegisteredCommands commands = toolCmdsMap.get(toolKey);
				// Add to set of commands
				commands.addCommand(command);
			} else {
				toolCmdsMap.put(toolKey, new RegisteredCommands(command));
				log.debug("Registered tool: " + toolKey);
			}
			log.info("Registered command " + command.getCommandKey()
					+ " for tool " + toolKey);
			allCommands.addCommand(command);
		}
	}

	public void clearCommands(String toolKey) {
		toolKey = toolKey.toUpperCase();
		if (toolCmdsMap.containsKey(toolKey)) {
			RegisteredCommands commands = toolCmdsMap.get(toolKey);
			for (String commandKey : commands.getCommandKeys()) {
				allCommands.removeByCommandKey(commandKey);
			}
			toolCmdsMap.remove(toolKey);
		}
	}

	public boolean isValidCommand(String command) {
		command = command.toUpperCase();
		return (allCommands.getCommandKeys().contains(command) || SmsConstants.HELP
				.equalsIgnoreCase(command));

	}

	private String generateNoCommandsMessage(Locale preferedLocale) {
		return externalLogic.getLocalisedString("sms.incoming.nocommands", preferedLocale);

	}

	private String generateInvalidSiteMessage(String site, Locale preferedLocale) {
		return externalLogic.getLocalisedString("sms.incoming.unknownsite", preferedLocale);

	}

	private String generateUnknownCommandMessage(ParsedMessage message, Locale preferedLocale) {
		String cmd = "";
		if (message != null && message.getCommand() != null) {
			cmd = message.getCommand();
		}
		String ret = externalLogic.getLocalisedString("sms.incoming.unknown", preferedLocale, new Object[]{cmd});
		ret = ret + " " + generateHelpMessage(preferedLocale); 
	
		return ret;
	}

	private String generateHelpMessage(Locale locale) {
		StringBuilder body = new StringBuilder();
		body.append(externalLogic.getLocalisedString("sms.incoming.validCommands", locale) + " ");
		
		final Iterator<String> i = allCommands.getCommandKeys().iterator();
		while (i.hasNext()) {
			String command = i.next();
			if (allCommands.getCommand(command).isVisible()) {
				body.append(command);
			}

			if (i.hasNext()) {
				body.append(", ");
			} else { // This happens if last command happens to be invisible
				if (", ".equals(body.substring(body.length() - 2))) {
					body.delete(body.length() - 2, body.length());
				}
			}
		}

		return body.toString();
	}

	public String generateAssistMessage(List<String> matches, Locale locale) {
		Collection<String> commands = null;
		StringBuilder body = new StringBuilder();

		body.append(externalLogic.getLocalisedString("sms.incoming.possmatches", locale));
		if (matches == null || matches.isEmpty()
				|| matches.contains(SmsConstants.HELP)) {
			// TODO: not sure what to do here
		} else {
			commands = matches;
		}
		if (commands != null) {
			final Iterator<String> i = commands.iterator();
			while (i.hasNext()) {
				String command = i.next();
				body.append(command);
				if (i.hasNext()) {
					body.append(", ");
				}
			}
		}
		return body.toString();
	}

	public String generateAssistMessage(String tool, Locale locale) {
		if (tool == null || !toolCmdsMap.containsKey(tool.toUpperCase())) {
			return externalLogic.getLocalisedString("sms.incoming.invalidTool", locale);
		} else {
			StringBuilder body = new StringBuilder();
			body.append(externalLogic.getLocalisedString("sms.incoming.possmatches", locale));
			RegisteredCommands commands = toolCmdsMap.get(tool.toUpperCase());
			final Iterator<String> i = commands.getCommandKeys().iterator();
			while (i.hasNext()) {
				String command = i.next();
				body.append(command);
				if (i.hasNext()) {
					body.append(", ");
				}
			}

			return body.toString();
		}

	}

	/**
	 * 
	 * @param valueToMatch
	 * @param values
	 * @return
	 */
	public SmsPatternSearchResult getClosestMatch(String valueToMatch,
			List<String> values) {

		log.debug("Looking for match for " + valueToMatch + " from " + values);

		SmsPatternSearchResult SmsPatternSearchResult = new SmsPatternSearchResult();
		List<String> possibleMatches = getPossibleMatches(valueToMatch,
				values);

		SmsPatternSearchResult.setPossibleMatches(possibleMatches);
		return SmsPatternSearchResult;
	}
}
