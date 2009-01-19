package org.sakaiproject.sms.hibernate.model.factory;

import java.util.Date;

import org.sakaiproject.sms.hibernate.logic.impl.HibernateLogicFactory;
import org.sakaiproject.sms.hibernate.model.SmsConfig;
import org.sakaiproject.sms.hibernate.model.SmsTask;
import org.sakaiproject.sms.hibernate.model.constants.SmsConst_Billing;
import org.sakaiproject.sms.hibernate.model.constants.SmsHibernateConstants;

public class SmsTaskFactory {

	public static SmsTask createReserveCreditsTask(String senderUsername, Integer credits, String sakaiSiteId, Long accountId){
		
		SmsTask task = new SmsTask();
		task.setSmsAccountId(accountId);
		task.setCreditEstimate(credits);
		task.setSakaiSiteId(sakaiSiteId);
		task.setSenderUserName(senderUsername);
		task.setMessageBody(SmsConst_Billing.TRANS_RESERVE_CREDITS);

		setDefaultedValues(task);
		
		return task;
	}

	private static void setDefaultedValues(SmsTask task) {
		task.setDateCreated(new Date());
		task.setDateToSend(new Date());
		task.setAttemptCount(1);
		task.setMaxTimeToLive(SmsHibernateConstants.DEFAULT_MAX_TIME_TO_LIVE);
		SmsConfig systemSmsConfig = HibernateLogicFactory.getConfigLogic().getOrCreateSystemSmsConfig();
		task.setDelReportTimeoutDuration(systemSmsConfig.getDelReportTimeoutDuration());
	}
	
}
