package org.sakaiproject.sms.hibernate.test;

import junit.framework.TestCase;

import org.sakaiproject.sms.hibernate.util.HibernateUtil;

public abstract class AbstractBaseTestCase extends TestCase {
	static {
		HibernateUtil.setTestConfiguration(true);
	}

	public AbstractBaseTestCase() {

	}

	public AbstractBaseTestCase(String name) {
		super(name);
	}

}
