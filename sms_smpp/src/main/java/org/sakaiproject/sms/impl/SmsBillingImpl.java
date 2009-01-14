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
import java.util.Set;

import org.sakaiproject.sms.api.SmsBilling;
import org.sakaiproject.sms.hibernate.logic.impl.HibernateLogicFactory;
import org.sakaiproject.sms.hibernate.model.SmsAccount;

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
	public void allocateCredits(int accountID, int creditCount) {
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
	public boolean checkSufficientCredits(int accountID, int creditsRequired) {
		SmsAccount account = HibernateLogicFactory.getAccountLogic()
				.getSmsAccount(new Long(new Integer(accountID)));

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
	 * Convert the given credits to currency base on the defined conversion
	 * value at the given time.
	 * 
	 * @param creditCount
	 *            the credit count
	 * 
	 * @return the double
	 */
	public Double convertCreditsToAmount(int creditCount) {
		// TODO Auto-generated method stub
		return 0D;
	}

	/**
	 * Return the currency amount available in the account.
	 * 
	 * @param accountID
	 *            the account id
	 * 
	 * @return the account balance
	 */
	public double getAccountBalance(int accountID) {
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
	public int getAccountCredits(int accountID) {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sakaiproject.sms.api.SmsBilling#getAccountID(java.lang.String,
	 * java.lang.String, java.lang.Integer)
	 */
	public int getAccountID(String sakaiSiteID, String sakaiUserID,
			Integer accountType) {
		// TODO Auto-generated method stub
		return 1;
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
	public Set getAccTransactions(int accountID, Date startDate, Date endDate) {
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
	public Boolean insertTransaction(int accountID, int transCodeID,
			int creditAmount) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Insert a new transaction and indicate that the credits are reserved. If
	 * the request is pending and the administrator delete the request, the
	 * reservation must be rolled back with another transaction.
	 * 
	 * @param accountID
	 *            the account id
	 * @param credits
	 *            the credits
	 * 
	 * @return true, if reserve credits
	 */
	public boolean reserveCredits(int accountID, int credits) {
		return true;
	}
}
