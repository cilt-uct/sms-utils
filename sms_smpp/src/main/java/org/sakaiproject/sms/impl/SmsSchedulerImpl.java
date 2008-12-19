package org.sakaiproject.sms.impl;

import java.util.Date;
import java.util.Timer;

import org.sakaiproject.sms.api.SmsCore;
import org.sakaiproject.sms.api.SmsScheduler;

public class SmsSchedulerImpl implements SmsScheduler {
	static Timer schedulerTimer;

	static int schedulerInterval = 10; // seconds
	private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger
			.getLogger(SmsSmppImpl.class);

	public SmsCore smsCore = null;

	public SmsSchedulerImpl() {

	}

	public SmsCore getSmsCore() {
		return smsCore;
	}

	public void setSmsCore(SmsCore smsCore) {
		this.smsCore = smsCore;
	}

	private class BindThread implements Runnable {

		boolean allDone = false;

		BindThread() {
			Thread t = new Thread(this);
			t.start();
		}

		public void run() {
			Work();
		}

		public void Work() {
			while (true) {
				if (allDone) {
					return;
				}
				try {
					Thread.sleep(schedulerInterval);
				} catch (InterruptedException e) {

					e.printStackTrace();
				}
				LOG.info("Trying to rebind");

			}

		}
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

}
