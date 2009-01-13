/***********************************************************************************
 * SmsAccountValidatorTest.java
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

import java.util.Date;

import junit.framework.TestCase;

import org.sakaiproject.sms.constants.SmsUiConstants;
import org.sakaiproject.sms.hibernate.model.SmsAccount;
import org.sakaiproject.sms.validators.SmsAccountValidator;
import org.springframework.validation.BindException;

/**
 * The Class SmsAccountValidatorTest. Runs tests for {@link SmsAccount}
 * validation that is run by {@link SmsAccountValidator}
 */
public class SmsAccountValidatorTest extends TestCase {

	private SmsAccountValidator validator;
	private BindException errors;
	private SmsAccount account;

	private static String ACCOUNT_NAME_FIELD = "accountName";
	private static String SAKAI_SITE_ID_FIELD = "sakaiSiteId";
	private static String SAKAI_USER_ID_FIELD = "sakaiUserId";
	private static String OVERDRAFT_LIMIT_FIELD = "overdraftLimit";
	private static String BALANCE_FIELD = "balance";

	private static int VALID_MAX_FIELD_SIZE = 99;

	private String generateString(int length) {
		String toReturn = "";
		for (int i = 0; i < length; i++) {
			toReturn += "x";
		}

		return toReturn;
	}

	/**
	 * Run before every test
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	public void setUp() {
		validator = new SmsAccountValidator();
		account = new SmsAccount();
		account.setAccountEnabled(true);
		account.setAccountName("account name");
		account.setSakaiSiteId("site 1");
		account.setSakaiUserId("userid 1");
		account.setStartdate(new Date());
		account.setEnddate(new Date());
		account.setBalance(100f);
		account.setOverdraftLimit(10f);
		account.setMessageTypeCode(SmsUiConstants.MESSAGE_TYPE_CODE);
		errors = new BindException(account, "SmsAccount");
	}

	/**
	 * Test empty account name
	 */
	public void testAccountName_empty() {
		account.setAccountName("");
		validator.validate(account, errors);
		assertTrue(errors.hasFieldErrors(ACCOUNT_NAME_FIELD));
		assertEquals("sms.errors.accountName.empty", errors.getFieldError()
				.getCode());
	}

	/**
	 * Test accountName with large size
	 */
	public void testAccountName_invalid() {
		account.setAccountName(generateString(VALID_MAX_FIELD_SIZE + 1));
		validator.validate(account, errors);
		assertTrue(errors.hasFieldErrors(ACCOUNT_NAME_FIELD));
		assertEquals("sms.errors.accountName.tooLong", errors.getFieldError()
				.getCode());
	}

	/**
	 * Test null account name
	 */
	public void testAccountName_null() {
		account.setAccountName(null);
		validator.validate(account, errors);
		assertTrue(errors.hasFieldErrors(ACCOUNT_NAME_FIELD));
		assertEquals("sms.errors.accountName.empty", errors.getFieldError()
				.getCode());
	}

	/**
	 * Test accountName with max size
	 */
	public void testAccountName_valid() {
		account.setAccountName(generateString(VALID_MAX_FIELD_SIZE));
		validator.validate(account, errors);
		assertFalse(errors.hasFieldErrors(ACCOUNT_NAME_FIELD));
	}

	/**
	 * Test null balance
	 */
	public void testBalance_null() {
		account.setBalance(null);
		validator.validate(account, errors);
		assertTrue(errors.hasFieldErrors(BALANCE_FIELD));
		assertEquals("sms.errors.balance.invalid", errors.getFieldError()
				.getCode());
	}

	/**
	 * If MessageTypeCode is null, none of the validation should run We are
	 * actually dependant on this behaviour for the datepicker + cancel button
	 * to work on the Account page
	 */
	public void testMessageTypeCode_null() {
		account.setMessageTypeCode(null);
		account.setBalance(null);
		account.setAccountName(null);
		account.setOverdraftLimit(null);
		account.setSakaiSiteId(null);
		validator.validate(account, errors);
		assertFalse(errors.hasErrors());
		assertFalse(errors.hasFieldErrors());
	}

	/**
	 * Test null OverdraftLimit
	 */
	public void testOverdraftLimit_null() {
		account.setOverdraftLimit(null);
		validator.validate(account, errors);
		assertTrue(errors.hasFieldErrors(OVERDRAFT_LIMIT_FIELD));
		assertEquals("sms.errors.overdraftLimit.invalid", errors
				.getFieldError().getCode());

	}

	/**
	 * Test empty SakaiSiteId
	 */
	public void testSakaiSiteId_empty() {
		account.setSakaiSiteId("");
		validator.validate(account, errors);
		assertTrue(errors.hasFieldErrors(SAKAI_SITE_ID_FIELD));
		assertEquals("sms.errors.sakaiSiteId.empty", errors.getFieldError()
				.getCode());
	}

	/**
	 * Test SakaiSiteId with large size
	 */
	public void testSakaiSiteId_invalid() {
		account.setSakaiSiteId(generateString(VALID_MAX_FIELD_SIZE + 1));
		validator.validate(account, errors);
		assertTrue(errors.hasFieldErrors(SAKAI_SITE_ID_FIELD));
		assertEquals("sms.errors.sakaiSiteId.tooLong", errors.getFieldError()
				.getCode());
	}

	/**
	 * Test null SakaiSiteId
	 */
	public void testSakaiSiteId_null() {
		account.setSakaiSiteId(null);
		validator.validate(account, errors);
		assertTrue(errors.hasFieldErrors(SAKAI_SITE_ID_FIELD));
		assertEquals("sms.errors.sakaiSiteId.empty", errors.getFieldError()
				.getCode());

	}

	/**
	 * Test SakaiSiteId with max size
	 */
	public void testSakaiSiteId_valid() {
		account.setSakaiSiteId(generateString(VALID_MAX_FIELD_SIZE));
		validator.validate(account, errors);
		assertFalse(errors.hasFieldErrors(SAKAI_SITE_ID_FIELD));
	}

	/**
	 * Test empty SakaiUserId
	 */
	public void testSakaiUserId_empty() {
		account.setSakaiUserId("");
		validator.validate(account, errors);
		assertFalse(errors.hasFieldErrors(SAKAI_USER_ID_FIELD));
	}

	/**
	 * Test SakaiUserId with large size
	 */
	public void testSakaiUserId_invalid() {
		account.setSakaiUserId(generateString(VALID_MAX_FIELD_SIZE + 1));
		validator.validate(account, errors);
		assertTrue(errors.hasFieldErrors(SAKAI_USER_ID_FIELD));
		assertEquals("sms.errors.sakaiUserId.tooLong", errors.getFieldError()
				.getCode());
	}

	/**
	 * Test null SakaiUserId
	 */
	public void testSakaiUserId_null() {
		account.setSakaiUserId(null);
		validator.validate(account, errors);
		assertFalse(errors.hasFieldErrors(SAKAI_USER_ID_FIELD));
	}

	/**
	 * Test SakaiUserId with max size
	 */
	public void testSakaiUserId_valid() {
		account.setSakaiUserId(generateString(VALID_MAX_FIELD_SIZE));
		validator.validate(account, errors);
		assertFalse(errors.hasFieldErrors(SAKAI_USER_ID_FIELD));
	}
}
