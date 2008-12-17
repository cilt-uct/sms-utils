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

import org.sakaiproject.sms.api.SmsBilling;

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
	 * @param creditCount
	 */
	public void allocateCredits(int accountID, int creditCount) {
		// TODO Auto-generated method stub

	}

	/**
	 * Return true of the account has the required credits available. Take into
	 * account overdraft limits, if applicable.
	 * 
	 * @param accountID
	 * @param creditsRequired
	 */
	public boolean checkSufficientCredits(int accountID, int creditsRequired) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Convert the given credits to currency base on the defined conversion
	 * value at the given time.
	 * 
	 * @param creditCount
	 */
	public double convertCreditsToAmount(int creditCount) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * Return a list of all transactions between startDate and endDate for the
	 * specific account.
	 * 
	 * @param accountID
	 * @param startDate
	 * @param endDate
	 */
	public void getAccTransactions(int accountID, Date startDate, Date endDate) {
		// TODO Auto-generated method stub

	}

	/**
	 * Return the currency amount available in the account.
	 * 
	 * @param accountID
	 */
	public double getAccountBalance(int accountID) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * Return credits available in the account.
	 * 
	 * @param accountID
	 */
	public int getAccountCredits(int accountID) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getAccountID(String sakaiSiteID, String sakaiUserID,
			Integer accountType) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * Return all accounts linked to the given Sakai site.
	 * 
	 * @param sakaiSiteID
	 */
	public void getAllSiteAccounts(String sakaiSiteID) {
		// TODO Auto-generated method stub

	}

	/**
	 * Insert a new account and return the new account id.
	 * 
	 * @param sakaiSiteID
	 */
	public void insertAccount(String sakaiSiteID) {
		// TODO Auto-generated method stub

	}

	/**
	 * Insert a new transaction for the given account id.
	 * 
	 * @param accountID
	 * @param transCodeID
	 * @param creditAmount
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
	 * @param credits
	 */
	public void reserveCredits(int accountID, int credits) {
		// TODO Auto-generated method stub

	}

}
