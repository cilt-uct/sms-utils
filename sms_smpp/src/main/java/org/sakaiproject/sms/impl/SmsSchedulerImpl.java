package org.sakaiproject.sms.impl;

import java.util.Date;

import org.sakaiproject.sms.api.SmsCore;
import org.sakaiproject.sms.api.SmsScheduler;
import org.springframework.util.Assert;

public class SmsSchedulerImpl implements SmsScheduler {

	static int schedulerInterval = 60000; // seconds

	private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger
			.getLogger(SmsSmppImpl.class);

	public SmsCore smsCore = null;

	public SmsSchedulerThread smsSchedulerThread = null;

	public SmsSchedulerImpl() {

	}

	public void init() {
		Assert.notNull(smsCore);
		smsSchedulerThread = new SmsSchedulerThread();
		System.out.println("Init of SmsScheduler complete");
	}

	public SmsCore getSmsCore() {
		return smsCore;
	}

	public void setSmsCore(SmsCore smsCore) {
		this.smsCore = smsCore;
	}

	private class SmsSchedulerThread implements Runnable {

		boolean stopScheduler = false;

		SmsSchedulerThread() {
			Thread t = new Thread(this);
			t.start();
		}

		public void run() {
			Work();
		}

		public void Work() {
			while (true) {
				if (stopScheduler) {
					return;
				}
				LOG.info("Searching for tasks to process");
				smsCore.processNextTask();
				try {
					Thread.sleep(schedulerInterval);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}

		}
	}

	public Date getLastProcessTime() {

		return null;
	}

	public int getStatus() {

		return 0;
	}

	public void insertIntoDebugLog() {

	}

	public void processQueue() {

	}

	public void processQueueNow() {

	}

	public void setInterval(int seconds) {
		schedulerInterval = seconds * 60;
	}

}
