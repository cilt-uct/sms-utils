/***********************************************************************************
 * HibernateUtil.java
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
package org.sakaiproject.sms.hibernate.util;

import java.io.IOException;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.sakaiproject.sms.hibernate.model.BaseModel;

/**
 * Configures hibernate with mapping definitions and configuration properties
 * for use in the application. Implements a singleton for creating a hibernate
 * template that creates individual hibernate sessions on a per thread basis.
 * <p>
 * The hibernate session factory is initialised from the database mapping file
 * hibernate-mappings.hbm.xml and from the hibernate configuration properties,
 * hibernate.properties. Both files are expected to be available on the
 * classpath.
 * 
 * @author Julian Wyngaard
 * @version 1.0
 * @created 24-Nov-2008
 */
public class HibernateUtil {

	/**
	 * Hibernate mappings file name.
	 * 
	 * private static final String HIB_MAPPINGS_FILE_NAME =
	 * "/hibernate-mappings.hbm.xml";
	 */

	/**
	 * Hibernate configuration file name.
	 */
	private static final String HIB_PROPERTIES_FILE_NAME = "/hibernate.properties";

	/**
	 * The Constant HIB_TEST_PROPERTIES_FILE_NAME.
	 */
	private static final String HIB_TEST_PROPERTIES_FILE_NAME = "/hibernate-test.properties";

	/**
	 * Location of hibernate.cfg.xml file.
	 */
	private static String CONFIG_FILE_LOCATION = "hibernate.cfg.xml";

	/**
	 * Hibernate session factory - singleton.
	 */
	private static SessionFactory sessionFactory;

	/**
	 * Container for thread-scoped sessions.
	 */
	private static final ThreadLocal<Session> session = new ThreadLocal<Session>();

	/** The test configuration. */
	private static boolean testConfiguration = false;

	/**
	 * Loads the given properties file from the classpath.
	 * 
	 * @param file
	 *            file name and path (on the classpath)
	 * 
	 * @return properties initialised from the given file
	 * 
	 * @throws IOException
	 *             if any error occurs locating and/or reading the given file
	 *             from the classpath
	 */
	private static Properties loadPropertiesFromClasspath(String file)
			throws IOException {
		Properties properties = new Properties();
		properties.load(HibernateUtil.class.getResourceAsStream(file));

		return properties;
	}

	/**
	 * Gets the configuration.
	 * 
	 * @return the configuration
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private static Configuration getConfiguration() throws IOException {

		Configuration configuration = new Configuration();

		// load bean mappings
		configuration.configure(CONFIG_FILE_LOCATION);
		// load hibernate propeties
		Properties properties = null;
		if (testConfiguration) {
			properties = loadPropertiesFromClasspath(HIB_TEST_PROPERTIES_FILE_NAME);
		} else {
			properties = loadPropertiesFromClasspath(HIB_PROPERTIES_FILE_NAME);
		}
		configuration.setProperties(properties);

		return configuration;
	}

	/**
	 * Returns the single session factory in the utils. If no session factory
	 * exists, a new one is created.
	 * 
	 * @return hibernate session factory
	 * 
	 * @exception HibernateException
	 *                if any error occurs reading the hibernate configuration
	 *                files
	 */
	private static SessionFactory getSessionFactory() {
		try {
			if (sessionFactory == null) {
				sessionFactory = getConfiguration().buildSessionFactory();
			}
		} catch (HibernateException ex) {
			throw new RuntimeException("Exception building SessionFactory: "
					+ ex.getMessage(), ex);
		} catch (IOException e) {
			e.printStackTrace();
			throw new HibernateException("Error reading hibernate properties: "
					+ e.getMessage(), e);
		}
		return sessionFactory;
	}

	/**
	 * Creates a database schema
	 */
	public static void createSchema() {
		try {
			// currentSession();
			new SchemaExport(getConfiguration()).create(false, true);
			// closeSession();
		} catch (IOException e) {
			e.printStackTrace();
			throw new HibernateException("Error reading hibernate properties: "
					+ e.getMessage(), e);
		}
	}

	/**
	 * Returns the hibernate session for the current thread.
	 * 
	 * @return the hibernate session for the current thread
	 * 
	 * @exception HibernateException
	 *                if any error occurs opening the hibernate session
	 */
	public static Session currentSession() throws HibernateException {
		Session s = session.get();
		// Open a new Session, if this Thread has none yet
		if (s == null) {
			s = getSessionFactory().openSession();
			session.set(s);
		}
		return s;
	}

	public static void closeSession() throws HibernateException {
		Session s = session.get();
		session.set(null);
		if (s != null)
			s.close();
	}

	/**
	 * Sets the test configuration.
	 * 
	 * @param testConfiguration
	 *            the new test configuration
	 */
	public static void setTestConfiguration(boolean testConfiguration) {
		HibernateUtil.testConfiguration = testConfiguration;
	}

	/**
	 * Clears the session cache
	 */
	public static void clear() {
		session.get().clear();
	}

	/**
	 * Evicts the object from the session
	 * 
	 * @param model
	 */
	public static void evict(BaseModel model) {
		session.get().evict(model);
	}

}