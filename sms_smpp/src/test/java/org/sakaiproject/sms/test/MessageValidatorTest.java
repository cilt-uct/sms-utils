package org.sakaiproject.sms.test;

import java.util.ArrayList;

import org.sakaiproject.sms.hibernate.model.SmsMessage;
import org.sakaiproject.sms.hibernate.model.SmsTask;
import org.sakaiproject.sms.hibernate.model.constants.SmsConst_DeliveryStatus;
import org.sakaiproject.sms.hibernate.model.constants.SmsHibernateConstants;
import org.sakaiproject.sms.hibernate.model.constants.ValidationConstants;
import org.sakaiproject.sms.hibernate.util.AbstractBaseTestCase;
import org.sakaiproject.sms.hibernate.util.HibernateUtil;
import org.sakaiproject.sms.impl.validate.MessageValidator;

// TODO: Auto-generated Javadoc
/**
 * The Class SmsMessageValidationTest. Runs tests for {@link SmsMessage}
 * validation that is run by {@link SmsMessageValidator}
 */
public class MessageValidatorTest extends AbstractBaseTestCase {

	/** The msg. */
	private SmsMessage msg;

	static {
		HibernateUtil.createSchema();
	}

	/** The errors. */
	ArrayList<String> errors = new ArrayList<String>();

	/**
	 * setUp to run before every test. Create SmsMessage + validator + errors
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	public void setUp() {
		msg = new SmsMessage();
		msg.setSakaiUserId("sakaiUserId");
		msg.setStatusCode(SmsConst_DeliveryStatus.STATUS_DELIVERED);
		msg.setSmsTask(new SmsTask());
		msg.setMobileNumber("072 1889 987");
	}

	/**
	 * Test valid message.
	 */
	public void testValidMessage() {
		errors = MessageValidator.validateMessage(msg);
		assertTrue(errors.size() == 0);
	}

	/**
	 * Test mobile number_empty.
	 */
	public void testMobileNumber_empty() {
		msg.setMobileNumber("");
		errors = MessageValidator.validateMessage(msg);
		assertTrue(errors.size() > 0);
		assertTrue(errors.contains(ValidationConstants.MOBILE_NUMBER_EMPTY));
	}

	/**
	 * Test mobile number_invalid.
	 */
	public void testMobileNumber_invalid() {
		msg.setMobileNumber("this is text");
		errors = MessageValidator.validateMessage(msg);
		assertTrue(errors.size() > 0);
		assertTrue(errors.contains(ValidationConstants.MOBILE_NUMBER_INVALID));
	}

	/**
	 * Test mobile number_invalid plus location.
	 */
	public void testMobileNumber_invalidPlusLocation() {
		msg.setMobileNumber("012345+678");
		errors = MessageValidator.validateMessage(msg);
		assertTrue(errors.size() > 0);
		assertTrue(errors.contains(ValidationConstants.MOBILE_NUMBER_INVALID));
	}

	/**
	 * Test mobile number_null.
	 */
	public void testMobileNumber_null() {
		msg.setMobileNumber(null);
		errors = MessageValidator.validateMessage(msg);
		assertTrue(errors.size() > 0);
		assertTrue(errors.contains(ValidationConstants.MOBILE_NUMBER_EMPTY));
	}

	/**
	 * Test mobile number_too long.
	 */
	public void testMobileNumber_tooLong() {
		msg.setMobileNumber("012345678901234567890123456789");
		assertTrue(msg.getMobileNumber().length() > SmsHibernateConstants.MAX_MOBILE_NR_LENGTH);
		errors = MessageValidator.validateMessage(msg);
		assertTrue(errors.size() > 0);
		assertTrue(errors.contains(ValidationConstants.MOBILE_NUMBER_TOO_LONG));
	}

	/**
	 * Test mobile number_valid with plus.
	 */
	public void testMobileNumber_validWithPlus() {
		msg.setMobileNumber("+2712 345 6789");
		errors = MessageValidator.validateMessage(msg);
		assertTrue(errors.size() == 0);
	}

	/**
	 * Test mobile number_valid with whitepsace.
	 */
	public void testMobileNumber_validWithWhitepsace() {
		msg.setMobileNumber(" 012 345 6785 ");
		errors = MessageValidator.validateMessage(msg);
		assertTrue(errors.size() == 0);
	}

	/**
	 * Test sakai user id.
	 */
	public void testSakaiUserId() {

		// null
		msg.setSakaiUserId(null);
		errors = MessageValidator.validateMessage(msg);
		assertTrue(errors.size() > 0);
		assertTrue(errors
				.contains(ValidationConstants.MESSAGE_SAKAI_USER_ID_EMPTY));

		// empty String
		msg.setSakaiUserId("");
		errors = MessageValidator.validateMessage(msg);
		assertTrue(errors.size() > 0);
		assertTrue(errors
				.contains(ValidationConstants.MESSAGE_SAKAI_USER_ID_EMPTY));

		// Blank space
		msg.setSakaiUserId("   ");
		errors = MessageValidator.validateMessage(msg);
		assertTrue(errors.size() > 0);
		assertTrue(errors
				.contains(ValidationConstants.MESSAGE_SAKAI_USER_ID_EMPTY));
	}

	/**
	 * Test status code.
	 */
	public void testStatusCode() {

		// null
		msg.setStatusCode(null);
		errors = MessageValidator.validateMessage(msg);
		assertTrue(errors.size() > 0);
		assertTrue(errors
				.contains(ValidationConstants.MESSAGE_STATUS_CODE_EMPTY));

		// empty String
		msg.setStatusCode("");
		errors = MessageValidator.validateMessage(msg);
		assertTrue(errors.size() > 0);
		assertTrue(errors
				.contains(ValidationConstants.MESSAGE_STATUS_CODE_EMPTY));

		// Blank space
		msg.setStatusCode("   ");
		errors = MessageValidator.validateMessage(msg);
		assertTrue(errors.size() > 0);
		assertTrue(errors
				.contains(ValidationConstants.MESSAGE_STATUS_CODE_EMPTY));
	}

	/**
	 * Test sms task.
	 */
	public void testSmsTask() {

		msg.setSmsTask(null);
		errors = MessageValidator.validateMessage(msg);
		assertTrue(errors.size() > 0);
		assertTrue(errors.contains(ValidationConstants.MESSAGE_TASK_ID_EMPTY));

	}

}