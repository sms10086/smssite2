package com.note.common;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;

public class HibernateSessionFactory {
    private static String CONFIG_FILE_LOCATION = "/hibernate.cfg.xml";
	private static final ThreadLocal<Session> threadLocal = new ThreadLocal<Session>();
    private static Configuration configuration = new Configuration();
    private static org.hibernate.SessionFactory sessionFactory;
    private static String configFile = CONFIG_FILE_LOCATION;

	static {
    	try {
			configuration.configure(configFile);
			sessionFactory = configuration.buildSessionFactory();
		} catch (Exception e) {
			System.err
					.println("%%%% Error Creating SessionFactory %%%%");
			e.printStackTrace();
		}
    }
    private HibernateSessionFactory() {
    }
	
    public static Session openSession() {
    	return sessionFactory.openSession();
    }
    
    public static Session getSession() throws HibernateException {
    	
        Session session = (Session) threadLocal.get();

		if (session == null || ! session.isOpen() ) {
			if (sessionFactory == null) {
				rebuildSessionFactory();
				if (sessionFactory == null)
					throw new HibernateException("can't rebuild SessionFactory");
			}
			session = sessionFactory.openSession();
			if (session == null)
				throw new HibernateException("can't open hibernate session");
			threadLocal.set(session);
		}
        return session;
    }

	public static synchronized void rebuildSessionFactory() {
		if (sessionFactory == null) {
			try {
				configuration.configure(configFile);
				sessionFactory = configuration.buildSessionFactory();
			} catch (Exception e) {
				System.err
						.println("%%%% Error Rebuild SessionFactory %%%%");
				e.printStackTrace();
			}
		}
	}

    public static void closeSession() throws HibernateException {
        Session session = (Session) threadLocal.get();
        if (session != null) {
        	threadLocal.set(null);
            session.close();
        }
    }

	public static org.hibernate.SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public static synchronized void setConfigFile(String configFile) {
		HibernateSessionFactory.configFile = configFile;
		sessionFactory = null;
	}

	public static Configuration getConfiguration() {
		return configuration;
	}

}
