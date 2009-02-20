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
package org.sakaiproject.sms.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.springframework.orm.hibernate3.HibernateTransactionManager;

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
public class StandaloneSmsDaoImpl extends SmsDaoImpl implements SmsDao {
	
	public StandaloneSmsDaoImpl(String propertiesFile) {
		setPropertiesFile(propertiesFile);
		// Automatically build session factory
		this.setSessionFactory(buildSessionFactory());
		init();
	}
	
	protected void init() {
		List<String> persistentClasses = new ArrayList<String>();
		persistentClasses.add("org.sakaiproject.sms.model.hibernate.SmsAccount");
		persistentClasses.add("org.sakaiproject.sms.model.hibernate.SmsConfig");
		persistentClasses.add("org.sakaiproject.sms.model.hibernate.SmsMessage");
		persistentClasses.add("org.sakaiproject.sms.model.hibernate.SmsTask");
		persistentClasses.add("org.sakaiproject.sms.model.hibernate.SmsTransaction");
	
		super.setPersistentClasses(persistentClasses);
		setTransactionManager(new HibernateTransactionManager(getSessionFactory()));
	}
	
	/**
	 * Properties filename
	 */
	private static String propertiesFile;

	private void setPropertiesFile(String propertiesFile) {
		StandaloneSmsDaoImpl.propertiesFile = "/" + propertiesFile;

	}

	/**
	 * Location of hibernate.cfg.xml file.
	 */
	private static String CONFIG_FILE_LOCATION = "hibernate.cfg.xml";

	private static Log LOG = LogFactory.getLog(StandaloneSmsDaoImpl.class);

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
		properties.load(StandaloneSmsDaoImpl.class.getResourceAsStream(file));

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
		properties = loadPropertiesFromClasspath(propertiesFile);
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
	public SessionFactory buildSessionFactory() {
		SessionFactory sessionFactory;
		try {
			sessionFactory = getConfiguration().buildSessionFactory();
		} catch (Throwable ex) {
			// We have to catch Throwable, otherwise we will miss
			// NoClassDefFoundError and other subclasses of Error

			throw new ExceptionInInitializerError(ex);
		}
		return sessionFactory;
	}

	/**
	 * Creates a database schema
	 */
	public static void createSchema() {
		try {
			new SchemaExport(getConfiguration()).create(false, true);
		} catch (IOException e) {
			e.printStackTrace();
			throw new HibernateException("Error reading hibernate properties: "
					+ e.getMessage(), e);
		}
	}
	
}