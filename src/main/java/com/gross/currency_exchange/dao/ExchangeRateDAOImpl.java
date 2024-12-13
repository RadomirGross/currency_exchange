package com.gross.currency_exchange.dao;

import com.gross.currency_exchange.model.Currency;
import com.gross.currency_exchange.model.ExchangeRate;
import com.gross.currency_exchange.util.HibernateSessionFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;


import java.math.BigDecimal;
import java.util.List;

public class ExchangeRateDAOImpl implements ExchangeRateDAO {
    SessionFactory sessionFactory;

    public ExchangeRateDAOImpl() {
        sessionFactory = HibernateSessionFactory.getSessionFactory();
    }


    @Override
    public List<ExchangeRate> getAllExchangeRates() {
        try (Session session = sessionFactory.openSession()) {
            List<ExchangeRate> exchangeRates = session.createQuery("from ExchangeRate", ExchangeRate.class).list();
            return exchangeRates;
        } catch (Exception e) {
            throw new RuntimeException("Error fetching exchangeRates :" + e.getMessage());
        }
    }

    @Override
    public ExchangeRate getExchangeRate(Currency baseCurrency, Currency targetCurrency) {
        try (Session session = sessionFactory.openSession()) {
            String sql = "SELECT * FROM \"ExchangeRates\" where " +
                    "\"BaseCurrencyId\" = :baseCurrencyId and \"TargetCurrencyId\" = :targetCurrencyId";

            ExchangeRate exchangeRate = session.createNativeQuery(sql, ExchangeRate.class)
                    .setParameter("baseCurrencyId", baseCurrency.getId())
                    .setParameter("targetCurrencyId", targetCurrency.getId())
                    .uniqueResult();

            return exchangeRate;
        }
    }

    @Override
    public ExchangeRate addExchangeRate(ExchangeRate exchangeRate) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(exchangeRate);
            session.getTransaction().commit();
            return exchangeRate;
        }
    }

    @Override
    public void updateExchangeRate(ExchangeRate exchangeRate) {

        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.merge(exchangeRate);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                throw new PersistenceException("Database constraint violation: " + e.getMessage(), e);
            }
        }
    }
}