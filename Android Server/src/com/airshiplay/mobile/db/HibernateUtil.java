/**
 * 
 */
package com.airshiplay.mobile.db;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * @author airshiplay
 * @Create Date 2013-2-3
 * @version 1.0
 * @since 1.0
 */
public class HibernateUtil {

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            return new Configuration().configure().buildSessionFactory();
        }
        catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

}
