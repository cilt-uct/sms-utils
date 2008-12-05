package org.sakaiproject.sms.hibernate.util;

import java.io.IOException;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;

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
	private static SessionFactory sessionFactory = null;

	/**
	 * Container for thread-scoped sessions.
	 */
	private static Session session = null;

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

	private static Configuration getConfiguration() throws IOException {
		Configuration configuration;

		configuration = new Configuration();

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
	private static SessionFactory getSessionFactory() throws HibernateException {
		// if no session factory exists, create a new one
		if (sessionFactory == null) {
			try {

				Configuration configuration = getConfiguration();
				// extract local db uri (which is then used to attach a server
				// to it)
				/*
				 * localDbUri = getConnUriFromJdbcConnString(properties
				 * .getProperty("hibernate.connection.url"));
				 */
				// Properties properties = configuration.getProperties();
				sessionFactory = configuration.buildSessionFactory();

				// extract local db uri (which is then used to attach a server
				// to it)

				/*
				 * localDbUri = getConnUriFromJdbcConnString(properties
				 * .getProperty("hibernate.connection.url"));
				 */

			} catch (IOException e) {
				throw new HibernateException(
						"Error reading hibernate properties: " + e.getMessage(),
						e);
			}
		}
		return sessionFactory;
	}

	/**
	 * Creates a database schema
	 */
	public static void createSchema() {
		try {
			currentSession();
			new SchemaExport(getConfiguration()).create(false, true);
			closeSession();
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
		if (session == null) {
			session = getSessionFactory().openSession();
		}
		return session;
	}

	/**
	 * Closes the current thread's hibernate session.
	 * 
	 * @exception HibernateException
	 *                if any error occurs closing the current thread's hibernate
	 *                session
	 */
	public static void closeSession() throws HibernateException {
		if (session != null) {
			session.flush();
		}
	}

	/**
	 * Clears the current thread's hibernate session cache.
	 * 
	 * @exception HibernateException
	 *                if any error occurs clearing the current thread's
	 *                hibernate session
	 */
	public static void clearSession() throws HibernateException {
		if (session != null) {
			session.clear();
		}
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

}