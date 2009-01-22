/***********************************************************************************
 * SmsBillingImpl.java
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
package org.sakaiproject.sms.impl;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.sakaiproject.sms.api.SmsBilling;
import org.sakaiproject.sms.hibernate.logic.impl.HibernateLogicFactory;
import org.sakaiproject.sms.hibernate.logic.impl.exception.MoreThanOneAccountFoundException;
import org.sakaiproject.sms.hibernate.model.SmsAccount;
import org.sakaiproject.sms.hibernate.model.SmsTask;
import org.sakaiproject.sms.hibernate.model.SmsTransaction;
import org.sakaiproject.sms.hibernate.model.constants.SmsConst_Billing;
import org.sakaiproject.sms.hibernate.model.constants.SmsHibernateConstants;

// TODO: Auto-generated Javadoc
/**
 * The billing service will handle all financial functions for the sms tool in
 * Sakai.
 * 
 * @author Julian Wyngaard
 * @version 1.0
 * @created 12-Dec-2008
 */
public class SmsBillingImpl implements SmsBilling {

	/**
	 * Add extra credits to the specific account by making an entry into
	 * SMS_TRANSACTION Also update the available credits on the account.
	 * 
	 * @param accountID
	 *            the account id
	 * @param creditCount
	 *            the credit count
	 */
	public void allocateCredits(Long accountID, int creditCount) {
		// TODO Auto-generated method stub

	}

	/**
	 * Return true of the account has the required credits available. Take into
	 * account overdraft limits, if applicable.
	 * 
	 * @param accountID
	 *            the account id
	 * @param creditsRequired
	 *            the credits required
	 * 
	 * @return true, if sufficient credits
	 */
	public boolean checkSufficientCredits(Long accountID, int creditsRequired) {
		SmsAccount account = HibernateLogicFactory.getAccountLogic()
				.getSmsAccount(accountID);

		// Account is null or disabled
		if (account == null || !account.getAccountEnabled()) {
			return false;
		}

		boolean sufficientCredit = false;
		if (account.getOverdraftLimit() != null) {
			if ((account.getBalance() + account.getOverdraftLimit()) >= creditsRequired) {
				sufficientCredit = true;
			}
		} else if (account.getBalance() >= creditsRequired) {
			sufficientCredit = true;
		}

		return sufficientCredit;
	}

	/**
	 * Convert amount to credits.
	 * 
	 * @param amount
	 *            the amount
	 * 
	 * @return the double
	 */
	public Integer convertAmountToCredits(Float amount) {
		Float result = (amount / SmsHibernateConstants.COST_OF_CREDIT);
		return result.intValue();
	}

	/**
	 * Convert the given credits to currency base on the defined conversion
	 * value at the given time.
	 * 
	 * @param creditCount
	 *            the credit count
	 * 
	 * @return the credit amount
	 */
	public Float convertCreditsToAmount(int creditCount) {
		return SmsHibernateConstants.COST_OF_CREDIT * creditCount;
	}

	/**
	 * Return the currency amount available in the account.
	 * 
	 * @param accountID
	 *            the account id
	 * 
	 * @return the account balance
	 */
	public double getAccountBalance(Long accountID) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * Return credits available in the account.
	 * 
	 * @param accountID
	 *            the account id
	 * 
	 * @return the account credits
	 */
	public int getAccountCredits(Long accountID) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * Use Sakai siteID, Sakai userID and account type to get a valid account
	 * id. AccountType is only outgoing masses for now.
	 * 
	 * @param sakaiSiteID
	 *            (e.g. !admin)
	 * @param sakaiUserID
	 *            the sakai user id
	 * @param accountType
	 *            the account type
	 * 
	 * @return the account id
	 * @throws MoreThanOneAccountFoundException
	 */
	public Long getAccountID(String sakaiSiteID, String sakaiUserID,
			Integer accountType) throws MoreThanOneAccountFoundException {
		SmsAccount account = HibernateLogicFactory.getAccountLogic()
				.getSmsAccount(sakaiSiteID, sakaiUserID);
		if (account != null) {
			return account.getId();
		}
		return null;
	}

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
	public Set getAccTransactions(Long accountID, Date startDate, Date endDate) {
		// TODO Auto-generated method stub
		return null;

	}

	/**
	 * Return all accounts linked to the given Sakai site.
	 * 
	 * @param sakaiSiteID
	 *            the sakai site id
	 * 
	 * @return the all site accounts
	 */
	public Set getAllSiteAccounts(String sakaiSiteID) {
		// TODO Auto-generated method stub
		return null;

	}

	/**
	 * Insert a new account and return the new account id.
	 * 
	 * @param sakaiSiteID
	 *            the sakai site id
	 * 
	 * @return true, if insert account
	 */
	public boolean insertAccount(String sakaiSiteID) {
		return false;
	}

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
			int creditAmount) {
		// TODO Auto-generated method stub
		return null;
	}

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
	public boolean reserveCredits(SmsTask smsTask) {

		SmsAccount account = HibernateLogicFactory.getAccountLogic()
				.getSmsAccount(smsTask.getSmsAccountId());
		if (account == null) {
			// Account does not exist
			return false;
		}

		SmsTransaction smsTransaction = new SmsTransaction();
		smsTransaction.setBalance(account.getBalance());// Set this to accounts
		// current balance
		smsTransaction.setSakaiUserId(smsTask.getSenderUserName());
		// come from ???
		smsTransaction.setTransactionDate(new Date(System.currentTimeMillis()));
		smsTransaction
				.setTransactionTypeCode(SmsConst_Billing.TRANS_RESERVE_CREDITS);
		smsTransaction.setTransactionCredits(smsTask.getCreditEstimate());
		smsTransaction.setTransactionAmount(convertCreditsToAmount(smsTask
				.getCreditEstimate()));
		smsTransaction.setSmsAccount(account);
		smsTransaction.setSmsTaskId(smsTask.getId());

		// Insert credit transaction
		HibernateLogicFactory.getTransactionLogic().insertCreditTransaction(
				smsTransaction);
		return true;

	}

	/**
	 * Recalculate balance for a specific account.
	 * 
	 * @param accountId
	 *            the account id
	 * @param account
	 *            the account
	 */
	private void recalculateAccountBalance(Long accountId, SmsAccount account) {
		HibernateLogicFactory.getAccountLogic().recalculateAccountBalance(
				accountId, account);
	}

	/**
	 * Recalculate balance for a specific account.
	 * 
	 * @param account
	 *            the account
	 */
	private void recalculateAccountBalance(SmsAccount account) {
		recalculateAccountBalance(null, account);
	}

	/**
	 * Recalculate balance for a specific account.
	 * 
	 * @param accountId
	 *            the account id
	 */
	public void recalculateAccountBalance(Long accountId) {
		recalculateAccountBalance(accountId, null);
	}

	/**
	 * Recalculate balances for all existing accounts.
	 */
	public void recalculateAccountBalances() {
		List<SmsAccount> accounts = HibernateLogicFactory.getAccountLogic()
				.getAllSmsAccounts();
		for (SmsAccount account : accounts) {
			recalculateAccountBalance(account);
		}
	}

	/**
	 * Settle credit difference.
	 * 
	 * @param smsTask
	 *            the sms task
	 * 
	 * @return true, if successful
	 */
	public boolean settleCreditDifference(SmsTask smsTask) {

		SmsAccount account = HibernateLogicFactory.getAccountLogic()
				.getSmsAccount(smsTask.getSmsAccountId());
		if (account == null) {
			// Account does not exist
			return false;
		}

		int creditEstimate = smsTask.getCreditEstimateInt();
		int actualCreditsUsed = smsTask.getGroupSizeActual();
		int creditDifference = creditEstimate - actualCreditsUsed;

		SmsTransaction smsTransaction = new SmsTransaction();
		smsTransaction.setBalance(account.getBalance());// Set this to
		// accounts
		// current balance
		smsTransaction.setSakaiUserId(smsTask.getSenderUserName());// TODO GET
																	// FROM TASK
		smsTransaction.setTransactionDate(new Date(System.currentTimeMillis()));
		smsTransaction
				.setTransactionTypeCode(SmsConst_Billing.TRANS_SETTLE_DIFFERENCE);
		smsTransaction.setTransactionCredits(creditDifference);

		smsTransaction.setSmsAccount(account);
		smsTransaction.setSmsTaskId(smsTask.getId());
		float transactionAmount = convertCreditsToAmount(creditDifference);
		if (transactionAmount >= 0) {
			// Insert debit transaction
			smsTransaction.setTransactionAmount(transactionAmount);
			HibernateLogicFactory.getTransactionLogic().insertDebitTransaction(
					smsTransaction);
		} else {
			// Insert credit transaction and make sure the transaction amount
			// gets passed as a positive value. The insert credit transaction
			// will handle that
			smsTransaction.setTransactionAmount((transactionAmount * -1));
			HibernateLogicFactory.getTransactionLogic()
					.insertCreditTransaction(smsTransaction);
		}
		return true;
	}
}
