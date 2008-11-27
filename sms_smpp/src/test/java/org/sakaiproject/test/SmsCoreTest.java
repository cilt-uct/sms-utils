package org.sakaiproject.test;

import java.sql.Timestamp;

import junit.framework.TestCase;

import org.sakaiproject.sms.hibernate.logic.impl.SmsTaskLogicImpl;
import org.sakaiproject.sms.hibernate.model.SmsTask;
import org.sakaiproject.sms.hibernate.model.constants.SmsConst_DeliveryStatus;
import org.sakaiproject.sms.impl.SmsCoreImpl;
import org.sakaiproject.sms.impl.SmsSmppImpl;

public class SmsCoreTest extends TestCase {
	SmsCoreImpl smsCoreImpl = new SmsCoreImpl();

	public void testGetDeliveryGroup() {

		SmsTask smsTask = new SmsTask();
		smsTask.setMessageBody("tesing sms");
		smsCoreImpl.getDeliveryGroup("1234566789", "group1", smsTask);

	}

	public void testInsertNewTask() {
		SmsTask insertTask = new SmsTask();
		insertTask.setSakaiSiteId("sakaiSiteId");
		insertTask.setSmsAccountId(0);
		insertTask.setDateCreated(new Timestamp(System.currentTimeMillis()));
		insertTask.setDateToSend(new Timestamp(System.currentTimeMillis()));
		insertTask.setStatusCode(SmsConst_DeliveryStatus.STATUS_PENDING);
		insertTask.setAttemptCount(0);
		insertTask.setMessageBody("testing1234567");
		insertTask.setSenderUserName("administrator");
		smsCoreImpl.setSmsTaskLogic(new SmsTaskLogicImpl());
		SmsSmppImpl smsSmppImpl = new SmsSmppImpl();
		smsSmppImpl.init();
		smsCoreImpl.setSmsSmpp(smsSmppImpl);
		smsCoreImpl.getSmsTaskLogic().persistSmsTask(insertTask);
		smsCoreImpl.processTask(insertTask);

	}

}
