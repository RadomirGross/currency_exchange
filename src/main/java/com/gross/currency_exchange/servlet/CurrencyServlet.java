package com.gross.currency_exchange.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gross.currency_exchange.dao.CurrencyDAOImpl;
import com.gross.currency_exchange.dto.CurrencyDTO;
import com.gross.currency_exchange.service.CurrencyService;
import com.gross.currency_exchange.service.CurrencyServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.exception.ConstraintViolationException;

import java.io.IOException;

import java.util.List;


@WebServlet("/currencies")
public class CurrencyServlet extends HttpServlet {
    private final CurrencyService currencyService;
    private final ObjectMapper objectMapper;

    public CurrencyServlet() {
        this.objectMapper = new ObjectMapper();
        this.currencyService = new CurrencyServiceImpl(new CurrencyDAOImpl());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            List<CurrencyDTO> currencies = currencyService.getAllCurrencies();
            String jsonResponse = objectMapper.writeValueAsString(currencies);
            response.setStatus(200);
            response.getWriter().write(jsonResponse);
        } catch (Exception e) {
            response.setStatus(500);
            response.getWriter().write("{\"error\": \"Internal server error: " + e.getMessage() + "\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String code = request.getParameter("code");
        String fullName = request.getParameter("fullName");
        String sign = request.getParameter("sign");

        try {
            CurrencyDTO savedCurrency = currencyService.addCurrency(code,fullName,sign);
            response.setStatus(201);
            response.getWriter().write(objectMapper.writeValueAsString(savedCurrency));
        } catch (ConstraintViolationException e) {
            response.setStatus(409);
            response.getWriter().write("{\"error\":" + e.getMessage() + "\"}");
        } catch (Exception e) {
            response.setStatus(500);
            response.getWriter().write("{\"error\": \"Internal server error: " + e.getMessage() + "\"}");
        }
    }
}
