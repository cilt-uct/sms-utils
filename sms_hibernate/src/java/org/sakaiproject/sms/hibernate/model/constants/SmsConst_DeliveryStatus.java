package org.sakaiproject.sms.hibernate.model.constants;

//TODO move to sms_core project later on

public class SmsConst_DeliveryStatus {
	/** The Constant STATUS_DELIVERED. */
	public final static String STATUS_DELIVERED = "D";

	/** The Constant STATUS_ERROR. */
	public final static String STATUS_ERROR = "E";

	/** The Constant STATUS_PENDING. */
	public final static String STATUS_PENDING = "P";

	/** The Constant STATUS_SENT. */
	public final static String STATUS_SENT = "S";

	/** The Constant STATUS_RETRY. */
	public final static String STATUS_RETRY = "R";

	/**
	 * The Constant STATUS_RETRY.During sending some messages returned with
	 * errors
	 */
	public final static String STATUS_INCOMPLETE = "I";

}
