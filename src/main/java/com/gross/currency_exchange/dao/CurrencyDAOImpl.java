package com.gross.currency_exchange.dao;

import com.gross.currency_exchange.model.Currency;
import com.gross.currency_exchange.util.HibernateSessionFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;


import java.util.List;

public class CurrencyDAOImpl implements CurrencyDAO {
    SessionFactory sessionFactory;


    public CurrencyDAOImpl() {
        sessionFactory = HibernateSessionFactory.getSessionFactory();
    }

    @Override
    public List<Currency> getAllCurrencies() {
        try (Session session = sessionFactory.openSession()) {
            List<Currency> currencies = session.createQuery("from Currency", Currency.class).list();
            return currencies;
        } catch (Exception e) {
            throw new RuntimeException("Error fetching currencies :" + e.getMessage());
        }
    }

    @Override
    public Currency getCurrencyByCode(String code) {
        try (Session session = sessionFactory.openSession()) {
            String sql = "SELECT * FROM currencies WHERE code = :code";
            NativeQuery<Currency> query = session.createNativeQuery(sql, Currency.class);
            query.setParameter("code", code);
            return query.uniqueResult();
        }
    }

    @Override
    public Currency addCurrency(Currency currency) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(currency);
            session.getTransaction().commit();
            return currency;
        }
    }
}
