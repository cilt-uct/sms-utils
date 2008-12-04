package org.sakaiproject.sms.hibernate.dao;

import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.sakaiproject.sms.hibernate.model.BaseModel;

/**
 * Implements the database transactions and convenience methods that are common
 * to all DAOs.
 * 
 * @author Julian Wyngaard
 * @version 1.0
 * @created 24-Nov-2008
 */
abstract public class BaseDao {

	/**
	 * Persists the given instance to the database. The save or update operation
	 * is done over a transaction. In case of any errors the transaction is
	 * rolled back.
	 * 
	 * @param object
	 *            object instance to be persisted
	 * @exception HibernateException
	 *                if any error occurs while saving or updating data in the
	 *                database
	 */
	protected void persist(BaseModel object) throws HibernateException {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		try {
			session.saveOrUpdate(object);
			tx.commit();
		} catch (HibernateException e) {
			tx.rollback();
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
	}

	/**
	 * Deletes the given instance from the database.
	 * 
	 * @exception HibernateException
	 *                if any error occurs while saving or updating data in the
	 *                database
	 */
	protected void delete(Object object) throws HibernateException {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		try {
			/*
			 * if (object instanceof Loggable) { Loggable log =
			 * (Loggable)object; BaseModel baseModel = (BaseModel)object;
			 * if(baseModel.getDateDeleted() == null){
			 * baseModel.setDateDeleted(new Date()); }
			 * if(baseModel.getUserDeletedId() == null){
			 * baseModel.setUserDeletedId(currentUser.getId()); }
			 * session.save(new LogEntry(baseModel.getUserDeletedId(),
			 * baseModel.getDateDeleted(), log.getLogMessage(), true,
			 * log.getObjectType())); }
			 */
			session.delete(object);
			tx.commit();
		} catch (HibernateException e) {
			tx.rollback();
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
	}

	/**
	 * Performs a clean 'shut-down' of the database. This method should be used
	 * when the system is closing down to leave the database in a controlled
	 * stated and release the locks.
	 * 
	 * @exception HibernateException
	 *                if any error occurs during database shutdown.
	 */
	protected void dbShutdown() throws HibernateException {
		Session session = HibernateUtil.currentSession();
		session.createSQLQuery("SHUTDOWN").executeUpdate();
	}

	/*
	 * protected void save(Object object) throws HibernateException { Session
	 * session = HibernateUtil.currentSession(); session.flush(); Transaction tx
	 * = session.beginTransaction(); try { session.replicate(object,
	 * ReplicationMode.LATEST_VERSION); tx.commit(); } catch (HibernateException
	 * e) { tx.rollback(); throw e; }finally { HibernateUtil.closeSession(); } }
	 */

	protected Object findById(Class className, Long id) {
		Object obj = null;
		try {
			Session session = HibernateUtil.currentSession();
			// obj = session.load(className, id);
			obj = session.get(className, id);
		} catch (ObjectNotFoundException ex) {
			obj = null;
		} finally {
			HibernateUtil.closeSession();
		}
		return obj;
	}

}