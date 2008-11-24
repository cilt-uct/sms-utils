/***********************************************************************************
 * SmsMessageValidationTest.java
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
package org.sakaiproject.sms.test;

import junit.framework.TestCase;

import org.sakaiproject.sms.constants.SMSConstants;
import org.sakaiproject.sms.hibernate.model.SmsMessage;
import org.sakaiproject.sms.validators.SmsMessageValidator;
import org.springframework.validation.BindException;

/**
 * The Class SmsMessageValidationTest. Runs tests for SmsMessage validation
 */
public class SmsMessageValidationTest extends TestCase {

	private SmsMessageValidator validator;
	private BindException errors;
	private SmsMessage msg;

	private static String VALID_MOBILE_NR = "0123456789";
	private static String VALID_MSG_BODY = "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";

	private static String MOBILE_NR_FIELD = "mobileNumber";
	private static String MSG_BODY_FIELD = "messageBody";

	/**
	 * setUp to run before every test. Create SmsMessage + validator + errors
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	public void setUp() {
		validator = new SmsMessageValidator();
		msg = new SmsMessage("", ""); // Should be default constructor
		errors = new BindException(msg, "SmsMessage");
	}

	/**
	 * Test empty message body
	 */
	public void testMessageBody_empty() {
		msg.setMessageBody("");
		msg.setMobileNumber(VALID_MOBILE_NR);
		validator.validate(msg, errors);
		assertTrue(errors.hasFieldErrors(MSG_BODY_FIELD));
		assertEquals("sms.errors.messageBody.empty", errors.getFieldError()
				.getCode());
	}

	/**
	 * Test null message body
	 */
	public void testMessageBody_null() {
		msg.setMessageBody(null);
		msg.setMobileNumber(VALID_MOBILE_NR);
		validator.validate(msg, errors);
		assertTrue(errors.hasFieldErrors(MSG_BODY_FIELD));
		assertEquals("sms.errors.messageBody.empty", errors.getFieldError()
				.getCode());
	}

	/**
	 * Test too long message body
	 */
	public void testMessageBody_tooLong() {
		msg.setMobileNumber(VALID_MOBILE_NR);
		msg.setMessageBody(VALID_MSG_BODY + VALID_MSG_BODY);
		assertTrue(msg.getMessageBody().length() > SMSConstants.MAX_SMS_LENGTH);
		validator.validate(msg, errors);
		assertTrue(errors.hasFieldErrors(MSG_BODY_FIELD));
		assertEquals("sms.errors.messageBody.tooLong", errors.getFieldError()
				.getCode());
	}

	/**
	 * Test empty message body (with whitespace)
	 */
	public void testMessageBody_whitespace() {
		msg.setMessageBody("   ");
		msg.setMobileNumber(VALID_MOBILE_NR);
		validator.validate(msg, errors);
		assertTrue(errors.hasFieldErrors(MSG_BODY_FIELD));
		assertEquals("sms.errors.messageBody.empty", errors.getFieldError()
				.getCode());
	}

	/**
	 * Test empty mobile number
	 */
	public void testMobileNumber_empty() {
		msg.setMobileNumber("");
		msg.setMessageBody(VALID_MSG_BODY);
		validator.validate(msg, errors);
		assertTrue(errors.hasFieldErrors(MOBILE_NR_FIELD));
		assertEquals("sms.errors.mobileNumber.empty", errors.getFieldError()
				.getCode());
	}

	/**
	 * Test invalid mobile number
	 */
	public void testMobileNumber_invalid() {
		msg.setMobileNumber("this is text");
		msg.setMessageBody(VALID_MSG_BODY);
		validator.validate(msg, errors);
		assertTrue(errors.hasFieldErrors(MOBILE_NR_FIELD));
		assertEquals("sms.errors.mobileNumber.invalid", errors.getFieldError()
				.getCode());
	}

	/**
	 * Test invalid mobile number (invalid plus location)
	 */
	public void testMobileNumber_invalidPlusLocation() {
		msg.setMobileNumber("012345+678");
		msg.setMessageBody(VALID_MSG_BODY);
		validator.validate(msg, errors);
		assertTrue(errors.hasFieldErrors(MOBILE_NR_FIELD));
		assertEquals("sms.errors.mobileNumber.invalid", errors.getFieldError()
				.getCode());
	}

	/**
	 * Test null mobile number
	 */
	public void testMobileNumber_null() {
		msg.setMobileNumber(null);
		msg.setMessageBody(VALID_MSG_BODY);
		validator.validate(msg, errors);
		assertTrue(errors.hasFieldErrors(MOBILE_NR_FIELD));
		assertEquals("sms.errors.mobileNumber.empty", errors.getFieldError()
				.getCode());
	}

	/**
	 * Test too long mobile number This is in fact just some arbitary number
	 * specified, no specifics lengths given yet
	 */
	public void testMobileNumber_tooLong() {
		msg.setMobileNumber("012345678901234567890123456789");
		assertTrue(msg.getMobileNumber().length() > SMSConstants.MAX_MOBILE_NR_LENGTH);
		msg.setMessageBody(VALID_MSG_BODY);
		validator.validate(msg, errors);
		assertTrue(errors.hasFieldErrors(MOBILE_NR_FIELD));
		assertEquals("sms.errors.mobileNumber.tooLong", errors.getFieldError()
				.getCode());
	}

	/**
	 * Test valid mobile number with plus sign at start
	 */
	public void testMobileNumber_validWithPlus() {
		msg.setMessageBody(VALID_MSG_BODY);
		msg.setMobileNumber("+2712 345 6789");
		validator.validate(msg, errors);
		assertFalse(errors.hasFieldErrors(MOBILE_NR_FIELD));
	}

	/**
	 * Test valid mobile number with removing whitespace
	 */
	public void testMobileNumber_validWithWhitepsace() {
		msg.setMessageBody(VALID_MSG_BODY);
		msg.setMobileNumber(" 012 345 6785 ");
		validator.validate(msg, errors);
		assertFalse(errors.hasFieldErrors(MOBILE_NR_FIELD));
	}

	/**
	 * Test valid message.
	 */
	public void testValidMessage() {
		msg.setMessageBody(VALID_MSG_BODY);
		msg.setMobileNumber(VALID_MOBILE_NR);
		validator.validate(msg, errors);
		assertFalse(errors.hasErrors());
	}
}
