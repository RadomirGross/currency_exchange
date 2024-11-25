package com.gross.currency_exchange.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

import java.util.Properties;

public class HibernateSessionFactory {

    // Потокобезопасный Singleton
    private static final HibernateSessionFactory instance = new HibernateSessionFactory();
    private final SessionFactory sessionFactory;

    private HibernateSessionFactory() {
        Properties properties = new Properties();
        properties.put(Environment.DRIVER, "org.postgresql.Driver");
        properties.put(Environment.URL, "jdbc:postgresql://localhost:5432/currency_db");
        properties.put(Environment.DIALECT, "org.hibernate.dialect.PostgreSQLDialect");
        properties.put(Environment.USER, "myuser");
        properties.put(Environment.PASS, "mypassword");
       // properties.put(Environment.USER, System.getenv("DB_USER")); // Из переменных окружения
       // properties.put(Environment.PASS, System.getenv("DB_PASS")); // Из переменных окружения
        properties.put(Environment.SHOW_SQL, "true");
        properties.put(Environment.HBM2DDL_AUTO, "validate");

        sessionFactory = new Configuration()
                .setProperties(properties)
                .addAnnotatedClass(com.gross.currency_exchange.model.Currency.class)
                .addAnnotatedClass(com.gross.currency_exchange.model.ExchangeRate.class)
                .buildSessionFactory();
    }

    public static SessionFactory getSessionFactory() {
        return instance.sessionFactory;
    }

    public static void shutdown() {
        if (instance.sessionFactory != null) {
            instance.sessionFactory.close();
        }
    }
}
