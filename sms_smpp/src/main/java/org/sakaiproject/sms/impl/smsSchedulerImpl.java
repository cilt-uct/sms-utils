package org.sakaiproject.sms.impl;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Level;
import org.sakaiproject.sms.api.SmsScheduler;
import org.sakaiproject.sms.hibernate.logic.impl.SmsTaskLogicImpl;

public class smsSchedulerImpl extends TimerTask implements SmsScheduler {
	static Timer schedulerTimer;

	static int schedulerInterval = 10; // seconds
	static SmsCoreImpl smsCoreImpl = new SmsCoreImpl();
	static SmsSmppImpl smsSmppImpl = new SmsSmppImpl();
	private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger
			.getLogger(SmsSmppImpl.class);

	public static void main(String... arguments) {

		smsSmppImpl.init();
		smsCoreImpl.setSmsSmpp(smsSmppImpl);
		smsCoreImpl.setSmsTaskLogic(new SmsTaskLogicImpl());
		LOG.setLevel(Level.ALL);
		schedulerTimer = new Timer();
		smsSchedulerImpl scheduler = new smsSchedulerImpl();
		schedulerTimer.schedule(scheduler, 1 * 1000, schedulerInterval * 1000);
	}

	public smsSchedulerImpl() {

	}

	public Date getLastProcessTime() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getStatus() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void insertIntoDebugLog() {
		// TODO Auto-generated method stub

	}

	public void processQueue() {
		// TODO Auto-generated method stub

	}

	public void processQueueNow() {
		// TODO Auto-generated method stub

	}

	public void setInterval(int seconds) {
		// TODO Auto-generated method stub

	}

	@Override
	public void run() {
		LOG.info("Scheduler Firing...");
		smsCoreImpl.processNextTask();

	}

}
