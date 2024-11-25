package com.gross.currency_exchange;

import com.gross.currency_exchange.dao.CurrencyDAOImpl;
import com.gross.currency_exchange.dto.CurrencyDTO;
import com.gross.currency_exchange.model.Currency;
import com.gross.currency_exchange.service.CurrencyServiceImpl;
import com.gross.currency_exchange.util.HibernateSessionFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;


public class Main {
    public static void main(String[] args) {
        try (SessionFactory factory = HibernateSessionFactory.getSessionFactory(); Session session = factory.openSession()) {
            CurrencyServiceImpl currencyService=new CurrencyServiceImpl(new CurrencyDAOImpl());

            session.beginTransaction();
          //  Currency currency=session.get(Currency.class,1);
            CurrencyDTO currency=currencyService.getCurrencyByCode("RUB");
            System.out.println(currency.toString());
            session.getTransaction().commit();

        }


    }
}
