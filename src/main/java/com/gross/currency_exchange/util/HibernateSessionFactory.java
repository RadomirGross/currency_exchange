package com.gross.currency_exchange.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.Properties;

public class HibernateSessionFactory {

    private static final HibernateSessionFactory instance = new HibernateSessionFactory();
    private final SessionFactory sessionFactory;

    private HibernateSessionFactory() {
        // Настраиваем HikariCP
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/currency_db");
        config.setUsername("myuser");
        config.setPassword("mypassword");
        config.setDriverClassName("org.postgresql.Driver");
        config.setMaximumPoolSize(10);

        HikariDataSource dataSource = new HikariDataSource(config);

        Properties properties = new Properties();

        properties.put(Environment.HBM2DDL_AUTO, "validate");
        properties.put(Environment.SHOW_SQL, "true");

        Configuration configuration = new Configuration();
        configuration.setProperties(properties);
        configuration.addAnnotatedClass(com.gross.currency_exchange.model.Currency.class);
        configuration.addAnnotatedClass(com.gross.currency_exchange.model.ExchangeRate.class);

// Передача DataSource в Hibernate
        configuration.getProperties().put(Environment.DATASOURCE, dataSource);

        sessionFactory = configuration.buildSessionFactory();


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
