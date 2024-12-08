package com.gross.currency_exchange.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gross.currency_exchange.dao.CurrencyDAOImpl;
import com.gross.currency_exchange.dao.ExchangeRateDAOImpl;
import com.gross.currency_exchange.dto.ExchangeRateDTO;
import com.gross.currency_exchange.model.ExchangeRate;
import com.gross.currency_exchange.service.CurrencyService;
import com.gross.currency_exchange.service.CurrencyServiceImpl;
import com.gross.currency_exchange.service.ExchangeRateService;
import com.gross.currency_exchange.service.ExchangeRateServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.exception.ConstraintViolationException;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

@WebServlet("/exchangeRates")
public class ExchangeRateServlet extends HttpServlet {
    private final ExchangeRateService exchangeRateService;
    private final ObjectMapper mapper;
    private final CurrencyService currencyService;

    public ExchangeRateServlet() {
        currencyService = new CurrencyServiceImpl(new CurrencyDAOImpl());
        exchangeRateService = new ExchangeRateServiceImpl(new ExchangeRateDAOImpl(), currencyService);
        mapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            List<ExchangeRateDTO> exchangeRateDTOList = exchangeRateService.getAllExchangeRates();
            String json = mapper.writeValueAsString(exchangeRateDTOList);
            response.setStatus(200);
            response.getWriter().write(json);
        } catch (EntityNotFoundException e) {
            e.getMessage();
        } catch (Exception e) {
            response.setStatus(500);
            response.getWriter().write("{\"error\": " + e.getMessage() + "\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String baseCurrencyCode = request.getParameter("baseCurrencyCode");
        String targetCurrencyCode = request.getParameter("targetCurrencyCode");
        String rateStr = request.getParameter("rate");
        if (baseCurrencyCode == null || targetCurrencyCode == null || rateStr == null) {
            response.setStatus(400);
            response.getWriter().write("{\"error\": \"Missing required parameters: baseCurrencyCode, targetCurrencyCode, rate.\"}");
            return;
        }
        if (rateStr.trim().isEmpty()) {
            response.setStatus(400);
            response.getWriter().write("{\"error\": \"Rate is missing or empty.\"}");
            return;
        }

        BigDecimal rate = new BigDecimal(rateStr);
        try {
            ExchangeRateDTO savedExchangeRate = exchangeRateService.addExchangeRate(baseCurrencyCode, targetCurrencyCode, rate);
            response.setStatus(201);
            response.getWriter().write(mapper.writeValueAsString(savedExchangeRate));
        } catch (IllegalArgumentException | ConstraintViolationException e) {
            response.setStatus(409);
            response.getWriter().write("{\"error\":" + e.getMessage() + "\"}");
        } catch (NoSuchElementException e) {
            response.setStatus(404);
            response.getWriter().write("{\"error\":" + e.getMessage() + "\"}");
        } catch (Exception e) {
            response.setStatus(500);
            response.getWriter().write("{\"error\": \"Internal server error: " + e.getMessage() + "\"}");
        }


    }


}
