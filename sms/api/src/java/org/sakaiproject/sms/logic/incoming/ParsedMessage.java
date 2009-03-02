/***********************************************************************************
 * ParsedMessage.java
 * Copyright (c) 2008 Sakai Project/Sakai Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 **********************************************************************************/
package org.sakaiproject.sms.logic.incoming;

public class ParsedMessage {

	private final String tool;
	private final String site;
	private final String command;
	private final String body;
	private final String userId;

	public ParsedMessage(String tool, String site, String userId, String command) {
		this.tool = tool;
		this.site = site;
		this.userId = userId;
		this.command = command;
		this.body = null;

	}

	public ParsedMessage(String tool, String site, String userID, String command,
			String body) {
		this.tool = tool;
		this.site = site;
		this.userId = userID;
		this.command = command;
		this.body = body;
	}

	public String getUserID() {
		return userId;
	}

	public String getTool() {
		return tool;
	}

	public String getSite() {
		return site;
	}

	public String getCommand() {
		return command;
	}

	public String getBody() {
		return body;
	}

	public boolean hasBody() {
		return (body != null);
	}

}