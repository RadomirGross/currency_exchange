package com.gross.currency_exchange.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gross.currency_exchange.dao.CurrencyDAOImpl;
import com.gross.currency_exchange.dao.ExchangeRateDAOImpl;
import com.gross.currency_exchange.dto.ExchangeRateDTO;
import com.gross.currency_exchange.service.CurrencyService;
import com.gross.currency_exchange.service.CurrencyServiceImpl;
import com.gross.currency_exchange.service.ExchangeRateService;
import com.gross.currency_exchange.service.ExchangeRateServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/exchangerate/*")
public class SingleExchangeRateServlet extends HttpServlet {

    private final ExchangeRateService exchangeRateService;
    private final ObjectMapper objectMapper;

    public SingleExchangeRateServlet() {
        CurrencyService currencyService = new CurrencyServiceImpl(new CurrencyDAOImpl());
        exchangeRateService = new ExchangeRateServiceImpl(new ExchangeRateDAOImpl(), currencyService);
        objectMapper = new ObjectMapper();
    }


    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();
        if (path.length() < 7 || !path.matches("/[A-Z]{6}")) {
            response.setStatus(400);
            response.getWriter().write("{\"error\": \"Invalid currency codes. Expected format: exchangerate/USDEUR\"}");
            return;
        }
        try {
            String baseCurrencyCode = path.substring(1, 4);
            String targetCurrencyCode = path.substring(4, 7);
            ExchangeRateDTO exchangeRateDTO = exchangeRateService.getExchangeRate(baseCurrencyCode, targetCurrencyCode);
            response.getWriter().write(objectMapper.writeValueAsString(exchangeRateDTO));
        } catch (IllegalArgumentException e) {
            response.setStatus(404);
            response.getWriter().write("{\" Exchange rate not found: " + e.getMessage() + "\"}");
        } catch (Exception e) {
            response.setStatus(500);
            response.getWriter().write("{\"error\": \"Internal server error: " + e.getMessage() + "\"}");
        }


    }

    @Override
    protected void doPatch(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();
        if (path.length() < 7 || !path.matches("/[A-Z]{6}")) {
            response.setStatus(400);
            response.getWriter().write("{\"error\": \"Invalid currency codes. Expected format: exchangerate/USDEUR\"}");
            return;
        }
    }
}
