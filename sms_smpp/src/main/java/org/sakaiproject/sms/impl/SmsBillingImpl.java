package org.sakaiproject.sms.impl;

import java.util.Date;

import org.sakaiproject.sms.api.SmsBilling;

public class SmsBillingImpl implements SmsBilling {

	public void allocateCredits(int accountID, int creditCount) {
		// TODO Auto-generated method stub

	}

	public boolean checkSufficientCredits(int accountID, int creditsRequired) {
		// TODO Auto-generated method stub
		return false;
	}

	public double convertCreditsToAmount(int creditCount) {
		// TODO Auto-generated method stub
		return 0;
	}

	public void getAccTransactions(int accountID, Date startDate, Date endDate) {
		// TODO Auto-generated method stub

	}

	public double getAccountBalance(int accountID) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getAccountCredits(int accountID) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getAccountID(Integer siteID, Integer userID, Integer accountType) {
		// TODO Auto-generated method stub
		return 0;
	}

	public Integer getAccountId() {
		// TODO Auto-generated method stub
		return null;
	}

	public void getAllSiteAccounts(String sakaiSiteID) {
		// TODO Auto-generated method stub

	}

	public void insertAccount(String sakaiSiteID) {
		// TODO Auto-generated method stub

	}

	public Boolean insertTransaction(int accountID, int transCodeID,
			int creditAmount) {
		// TODO Auto-generated method stub
		return null;
	}

	public void reserveCredits(int accountID, int credits) {
		// TODO Auto-generated method stub

	}

}
