package com.note.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class HibernateTransaction {	
	private static final Log LOG = LogFactory.getLog(HibernateTransaction.class);
	
	private Transaction transaction = null;

	public void beginTransaction(){
		if (transaction == null) {
			Session session = HibernateSessionFactory.getSession();
			transaction = session.beginTransaction();
		}
	}
	
	public void commit(){
		if (transaction != null) {
			transaction.commit();
			transaction = null;
		}
	}
	
	public void rollback(){
		rollback(null, null);
	}
	
	public void rollback(String message, Throwable thr){
		if (transaction != null) {
			transaction.rollback();
			transaction = null;
		}
		if (thr != null)
			LOG.error(message, thr);
	}
	
}
