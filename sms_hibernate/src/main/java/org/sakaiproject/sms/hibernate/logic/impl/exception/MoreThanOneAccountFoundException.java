/***********************************************************************************
 * MoreThanOneAccountFoundException.java
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
package org.sakaiproject.sms.hibernate.logic.impl.exception;

/**
 * Exception that will be thrown when more than one sms account is returned from
 * a query that should only return one sms account.
 * 
 * @author julian@psybergate.com
 * @version 1.0
 * @created 20-Jan-2009
 */
public class MoreThanOneAccountFoundException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new more than one account found exception.
	 */
	public MoreThanOneAccountFoundException() {
	}

	/**
	 * Instantiates a new more than one account found exception.
	 * 
	 * @param arg0
	 *            the arg0
	 */
	public MoreThanOneAccountFoundException(String arg0) {
		super(arg0);
	}

	/**
	 * Instantiates a new more than one account found exception.
	 * 
	 * @param arg0
	 *            the arg0
	 */
	public MoreThanOneAccountFoundException(Throwable arg0) {
		super(arg0);
	}

	/**
	 * Instantiates a new more than one account found exception.
	 * 
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 */
	public MoreThanOneAccountFoundException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
