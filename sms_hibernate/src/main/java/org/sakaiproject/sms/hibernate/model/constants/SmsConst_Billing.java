package org.sakaiproject.sms.hibernate.model.constants;

/**
 * Used with SmsTransaction.transactionTypeCode
 * 
 * @author louis@psybergate.co.za
 * 
 * 
 */
public class SmsConst_Billing {
	public static final String TRANS_RESERVE_CREDITS = "RES";

	public static final String TRANS_SETTLE_DIFFERENCE = "RSET";

	public static final String TRANS_CANCEL_RESERVE = "RCAN";
}