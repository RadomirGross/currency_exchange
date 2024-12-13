package com.gross.currency_exchange.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;


import java.util.Properties;

public class HibernateSessionFactory {

    private static final HibernateSessionFactory instance = new HibernateSessionFactory();
    private final SessionFactory sessionFactory;

    private HibernateSessionFactory() {
        // Настраиваем HikariCP
        try {
            HikariConfig config = new HikariConfig();
           config.setJdbcUrl("jdbc:postgresql://db:5432/currency_db");
     //       config.setJdbcUrl("jdbc:postgresql://localhost:5432/currency_db");
            config.setUsername("myuser");
            config.setPassword("mypassword");
            config.setDriverClassName("org.postgresql.Driver");
            config.setMaximumPoolSize(10);

            HikariDataSource dataSource = new HikariDataSource(config);

            Properties properties = new Properties();

            properties.put(Environment.HBM2DDL_AUTO, "update");
            properties.put(Environment.SHOW_SQL, "true");


            Configuration configuration = new Configuration();
            configuration.setProperties(properties);
            configuration.addAnnotatedClass(com.gross.currency_exchange.model.Currency.class);
            configuration.addAnnotatedClass(com.gross.currency_exchange.model.ExchangeRate.class);
            configuration.getProperties().put(Environment.DATASOURCE, dataSource);
// Передача DataSource в Hibernate


            sessionFactory = configuration.buildSessionFactory();
            System.out.println("Hibernate SessionFactory initialized successfully.");
        } catch (HibernateException e) {
            System.err.println("Error initializing Hibernate SessionFactory: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Hibernate initialization failed", e);
        }
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