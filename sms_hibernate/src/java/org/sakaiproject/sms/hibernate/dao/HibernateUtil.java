package org.sakaiproject.sms.hibernate.dao;


import java.io.IOException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Configures hibernate with mapping definitions and configuration properties
 * for use in the application. Implements a singleton for creating a hibernate
 * template that creates individual hibernate sessions on a per thread basis.
 * <p>
 * The hibernate session factory is initialised from the database mapping
 * file hibernate-mappings.hbm.xml and from the hibernate configuration properties,
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
     */
    private static final String HIB_MAPPINGS_FILE_NAME =  "/org/sakaiproject/sms/hibernate/hbm/hibernate-mappings.hbm.xml";
    
    /**
     * Hibernate configuration file name.
     */
    private static final String HIB_PROPERTIES_FILE_NAME = "/resources/hibernate.properties";
    
    /**
     * Location of hibernate.cfg.xml file. 
     */
    private static String CONFIG_FILE_LOCATION = "resources/hibernate.cfg.xml";
    
    /**
     * Hibernate session factory - singleton.
     */
    private static SessionFactory sessionFactory = null;
    
    /**
     * Container for thread-scoped sessions.
     */
    private static Session session = null;
    
    /**
     * URI of the local database (extracted from the JDBC connect string).
     */
    private static String localDbUri = null;
   
    
    /**
     * Loads the given properties file from the classpath.
     *
     * @param file      file name and path (on the classpath)
     *
     * @return properties initialised from the given file
     *
     * @throws IOException if any error occurs locating and/or reading the
     *         given file from the classpath
     */
    private static Properties loadPropertiesFromClasspath(String file) throws IOException {        
        Properties properties = new Properties();
        properties.load(HibernateUtil.class.getResourceAsStream(file));
        
        return properties;
    }
    
    
    /**
     * Returns the single session factory in the utils. If no session
     * factory exists, a new one is created.
     *
     * @return hibernate session factory
     *
     * @exception HibernateException if any error occurs reading the hibernate
     *            configuration files
     */
    private static SessionFactory getSessionFactory() throws HibernateException {        
        // if no session factory exists, create a new one
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration();

                //load bean mappings
                configuration.configure(CONFIG_FILE_LOCATION);
                
                // load hibernate properties
                Properties properties = loadPropertiesFromClasspath(HIB_PROPERTIES_FILE_NAME);
                configuration.setProperties(properties);
                
                // extract local db uri (which is then used to attach a server to it)
                localDbUri = getConnUriFromJdbcConnString(properties.getProperty("hibernate.connection.url"));
                
                sessionFactory = configuration.buildSessionFactory();
                
                // extract local db uri (which is then used to attach a server to it)
                localDbUri = getConnUriFromJdbcConnString(properties.getProperty("hibernate.connection.url"));
            }
            catch (IOException e) {
                throw new HibernateException("Error reading hibernate properties: " + e.getMessage(), e);
            }
        }
        
        
        return sessionFactory;
    }
    
    /**
     * Returns the hibernate session for the current thread.
     *
     * @return the hibernate session for the current thread
     *
     * @exception HibernateException if any error occurs opening the 
     *            hibernate session
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
     * @exception HibernateException if any error occurs closing the current
     *            thread's hibernate session
     */
    public static void closeSession() throws HibernateException {        
    	if (session != null) {
            session.flush();
        }
    }
    
    
    
    /**
     * Extracts the connection URI from the JDBC connection string.
     *
     * @param connString    JDBC connection string
     *
     * @return connection URI or <code>null</code> if no connection URI can
     *         be found in the given string
     */
    private static final String getConnUriFromJdbcConnString(String connString) {        
        String uri = null;
        
        if (connString != null) {
            Pattern pattern = Pattern.compile("(.+):(.+):(.+:.+)");
            Matcher matcher = pattern.matcher(connString);

            if (matcher.lookingAt()) {
                uri = matcher.group(3);
            }
        }        
        return uri;
    }
    
    
}