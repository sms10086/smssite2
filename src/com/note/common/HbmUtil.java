package com.note.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import com.note.NoteException;

public class HbmUtil {	   
    private static Log LOG = LogFactory.getLog(HbmUtil.class);
   
    public static List load(Class clazz, int startIndex, int endIndex) {
        Session session = HibernateSessionFactory.getSession();
        Criteria crit = session.createCriteria( clazz );
        
        if (startIndex < 0) startIndex = 0;
        else if (startIndex > 0)
        crit.setFirstResult( startIndex );
        
        if (endIndex > 0 && endIndex > startIndex)
            crit.setFetchSize( endIndex - startIndex );
        
        List list = crit.list();
        return list;
    }
    
    public static ArrayList loadAndDelete(Class clazz, int batchSize) throws NoteException {
    	Session session = HibernateSessionFactory.getSession();
    	HibernateTransaction t = new HibernateTransaction();
    	ArrayList array = new ArrayList();
        try {
            t.beginTransaction();
            Criteria crit = session.createCriteria( clazz );
            if (batchSize > 0)
                crit.setFetchSize( batchSize );
            
            List list = crit.list();
            for(int i = 0; i < list.size(); i ++) {
            	Object obj = list.get(i);
            	array.add( obj );
            	session.delete( obj );
            }
            t.commit();
            return array;
        }
        catch (Throwable thr) {
            t.rollback("loadAndDelete", thr);
            throw new NoteException(thr.getMessage(), thr);
        }
    }
    
	public static Object identity(Class clazz, Serializable  key) {
		Session session = null;
		if (key == null) return null;
		session = HibernateSessionFactory.getSession();
		Object object = session.get(clazz, key);
        return object;
	}

	public static boolean update(Object object) {
		Session session = null;
		HibernateTransaction t = new HibernateTransaction();
		try {
			session = HibernateSessionFactory.getSession();
            t.beginTransaction();
			session.update(object);
            t.commit();
			return true;
		}
        catch (Throwable thr) {
        	t.rollback("update", thr);
        }
        return false;
	}
    
    public static boolean update(Object object, Serializable id ) {
    	Session session = HibernateSessionFactory.getSession();
    	HibernateTransaction t = new HibernateTransaction();
        try {
            t.beginTransaction();
            String idn = session.getEntityName(object);
            session.update(idn, object);
            t.commit();
            return true;
        }
        catch (Throwable thr) {
            t.rollback("update", thr);
        }
        return false;
    }
	
    public static boolean saveOrUpdate(Object object) {
    	Session session = HibernateSessionFactory.getSession();
    	HibernateTransaction t = new HibernateTransaction();
        try {
            t.beginTransaction();
            String idn = session.getEntityName(object);
            session.saveOrUpdate(object);
            t.commit();
            return true;
        }
        catch (Throwable thr) {
            t.rollback("saveOrUpdate", thr);
        }
        return false;
    }
    
    public static boolean store(Object object, boolean insertIt) {
        if (insertIt)
            return insert(object);
        return update(object);
    }
    
    public static boolean store(Object object) {
        return saveOrUpdate(object);
    }
    
	public static boolean insert(Object object) {
		Session session = HibernateSessionFactory.getSession();
		HibernateTransaction t = new HibernateTransaction();
		try {
			
            t.beginTransaction();
            if (object instanceof Collection) {
            	Collection coll = (Collection) object;
            	for(Iterator itr = coll.iterator(); itr.hasNext();)
            		session.save( itr.next() );
            } else
            if (object instanceof Object[]) {
            	Object[] objs = (Object[]) object;
            	for(int i = 0; i < objs.length; i ++)
            		session.save(objs[i]);
            } else
			session.save(object);
            t.commit();
			return true;
		}
        catch (Throwable thr) {
            t.rollback("insert", thr);
        }
        return false;
	}
	
    public static boolean delete(Object object) {
    	Session session = HibernateSessionFactory.getSession();
    	HibernateTransaction t = new HibernateTransaction();
        try {
            t.beginTransaction();
            if (object instanceof Collection) {
            	Collection coll = (Collection) object;
            	for(Iterator itr = coll.iterator(); itr.hasNext();)
            		session.delete( itr.next() );
            } else
            session.delete(object);
            t.commit();
            return true;
        }
        catch (Throwable thr) {
            t.rollback("delete", thr);
        }
        return false;
    }

    public static List select(Class clazz, String[] columns, Object[] values) {
    	return select(clazz, columns, values, -1);
    }
    
    public static List select(String sql, String[] columns, Object[] values) {
    	return select(sql, columns, values, -1);
    }
    
    public static List select(Class clazz, String[] columns, Object[] values, int fetchSize) {
        Session session = HibernateSessionFactory.getSession();;
        return _select(session, clazz, columns, values, fetchSize);
    }
    
    public static List select(String sql, String[] columns, Object[] values, int fetchSize) {
    	Session session = HibernateSessionFactory.getSession();;
    	session = HibernateSessionFactory.openSession();
        return _select(session, sql, columns, values, fetchSize);
    }
    
	public static Object first(Class clazz, String[] columns, Object[] values) {
        Object object = null;
        Session session = HibernateSessionFactory.getSession();;
		List list = _select(session, clazz, columns, values, -1);
		if (list.size() > 0) {
		    object = list.get(0);
        }
        return object;
	}
    
    
    private static List _select(Session session, Class clazz, String[] columns, Object[] values, int fetchSize) {
        Criteria crit = session.createCriteria(clazz);
        for(int i = 0; i < columns.length; i ++)
            crit.add( Restrictions.eq(columns[i], values[i]) );
        if (fetchSize > 0)
        	crit.setFetchSize(fetchSize);
        return crit.list();
      
    }

    private static List _select(Session session, String sql, String[] columns, Object[] values, int fetchSize) {
        Query q = session.createQuery(sql);
        for(int i = 0; i < columns.length; i ++)
            q.setParameter( columns[i], values[i] );
        if (fetchSize > 0)
        	q.setFetchSize( fetchSize );
        return q.list();
       
    }
}
