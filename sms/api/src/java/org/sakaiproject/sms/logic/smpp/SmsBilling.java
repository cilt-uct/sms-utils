/***********************************************************************************
 * SmsBilling.java
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

package org.sakaiproject.sms.logic.smpp;

import java.util.Date;
import java.util.Set;

import org.sakaiproject.sms.logic.impl.hibernate.exception.SmsAccountNotFoundException;
import org.sakaiproject.sms.model.hibernate.SmsMessage;
import org.sakaiproject.sms.model.hibernate.SmsTask;

// TODO: Auto-generated Javadoc
/**
 * The billing service will handle all financial functions for the sms tool in
 * Sakai.
 * 
 * @author louis@psybergate.com
 * @version 1.0
 * @created 12-Nov-2008
 */
public interface SmsBilling {

	/**
	 * Debit an account by the supplied amount
	 * 
	 * @param accountId
	 * @param amountToDebit
	 */
	public void debitAccount(Long accountId, Float amountToDebit);

	/**
	 * Add extra credits to the specific account by making an entry into
	 * SMS_TRANSACTION . Also update the available credits on the account.
	 * 
	 * @param accountID
	 *            the account id
	 * @param creditCount
	 *            the credit count
	 */
	void allocateCredits(Long accountID, int creditCount);

	/**
	 * Return true of the account has the required credits available. Take into
	 * account overdraft limits, if applicable.
	 * 
	 * @param accountID
	 *            the account id
	 * @param creditsRequired
	 *            the credits required
	 * 
	 * @return true, if check sufficient credits
	 */
	public boolean checkSufficientCredits(Long accountID,
			Integer creditsRequired);

	/**
	 * 
	 * Return true of the account has the required credits available. Take into
	 * account overdraft limits, if applicable.
	 * 
	 * @param smsTask
	 * @return
	 */
	public boolean checkSufficientCredits(SmsTask smsTask);

	/**
	 * Convert amount to credits.
	 * 
	 * @param amount
	 *            the amount
	 * 
	 * @return the double
	 */
	public Integer convertAmountToCredits(Float amount);

	/**
	 * Convert the given credits to currency base on the defined conversion
	 * value at the given time.
	 * 
	 * @param creditCount
	 *            the credit count
	 * 
	 * @return the credit amount
	 */
	public Float convertCreditsToAmount(int creditCount);

	/**
	 * Return the currency amount available in the account.
	 * 
	 * @param accountID
	 *            the account id
	 * 
	 * @return the account balance
	 */
	public double getAccountBalance(Long accountID);

	/**
	 * Return credits available in the account.
	 * 
	 * @param accountID
	 *            the account id
	 * 
	 * @return the account credits
	 */
	public int getAccountCredits(Long accountID);

	/**
	 * Use Sakai siteID, Sakai userID and account type to get a valid account
	 * id. AccountType is only outgoing masses for now.
	 * 
	 * @param sakaiSiteID
	 *            (e.g. !admin)
	 * @param sakaiUserID
	 *            the sakai user id
	 * 
	 * @return the account id
	 * 
	 * @throws SmsAccountNotFoundException
	 *             the sms account not found exception
	 */
	public Long getAccountID(String sakaiSiteID, String sakaiUserID)
			throws SmsAccountNotFoundException;

	/**
	 * Return a list of all transactions between startDate and endDate for the
	 * specific account.
	 * 
	 * @param accountID
	 *            the account id
	 * @param startDate
	 *            the start date
	 * @param endDate
	 *            the end date
	 * 
	 * @return the acc transactions
	 */
	public Set getAccTransactions(Long accountID, Date startDate, Date endDate);

	/**
	 * Return all accounts linked to the given Sakai site.
	 * 
	 * @param sakaiSiteID
	 *            (e.g. !admin)
	 * 
	 * @return the all site accounts
	 */
	public Set getAllSiteAccounts(String sakaiSiteID);

	/**
	 * Insert a new account and return the new account id.
	 * 
	 * @param sakaiSiteID
	 *            (e.g. !admin)
	 * 
	 * @return true, if insert account
	 */
	public boolean insertAccount(String sakaiSiteID);

	/**
	 * Insert a new transaction for the given account id.
	 * 
	 * @param accountID
	 *            the account id
	 * @param transCodeID
	 *            the trans code id
	 * @param creditAmount
	 *            the credit amount
	 * 
	 * @return true, if insert transaction
	 */
	public Boolean insertTransaction(Long accountID, int transCodeID,
			int creditAmount);

	/**
	 * Insert a new transaction and indicate that the credits are reserved. If
	 * the request is pending and the administrator delete the request, the
	 * reservation must be rolled back with another transaction.
	 * 
	 * @param smsTask
	 *            the sms task
	 * 
	 * @return true, if reserve credits
	 */
	public boolean reserveCredits(SmsTask smsTask);

	/**
	 * Recalculate balance for a specific account.
	 * 
	 * @param accountId
	 *            the account id
	 */
	public void recalculateAccountBalance(Long accountId);

	/**
	 * Cancel pending request.
	 * 
	 * @param smsTaskId
	 *            the sms task id
	 * 
	 * @return true, if successful
	 */
	public boolean cancelPendingRequest(Long smsTaskId);

	/**
	 * Recalculate balances for all existing accounts.
	 * 
	 * @param accountId
	 *            the account id
	 */
	public void recalculateAccountBalances();

	/**
	 * Settle credit difference.
	 * 
	 * @param smsTask
	 *            the sms task
	 * 
	 * @return true, if successful
	 */
	public boolean settleCreditDifference(SmsTask smsTask);

	/**
	 * Credits account for a message that came in late.
	 * 
	 * @param smsMessage
	 * @return
	 */
	public boolean creditLateMessage(SmsMessage smsMessage);

}