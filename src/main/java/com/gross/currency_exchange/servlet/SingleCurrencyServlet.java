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

import java.io.IOException;
import java.util.NoSuchElementException;

@WebServlet("/currency/*")
public class SingleCurrencyServlet extends HttpServlet {
    private final CurrencyService currencyService;
    private final ObjectMapper objectMapper;

    public SingleCurrencyServlet() {
        this.currencyService = new CurrencyServiceImpl(new CurrencyDAOImpl());
        this.objectMapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String code = null;
        try {
            String pathInfo = request.getPathInfo();
            if (pathInfo == null || !pathInfo.matches("/[A-Z]{3}")) {
                response.setStatus(400);
                response.getWriter().write("{\"error\": \"Invalid currency code. Expected format: /currency/USD\"}");
                return;
            }
            code = pathInfo.substring(1).toUpperCase();
            CurrencyDTO currencyDTO = currencyService.getCurrencyByCode(code);
            ObjectMapper objectMapper = new ObjectMapper();
            response.setStatus(200);
            objectMapper.writeValue(response.getWriter(), currencyDTO);
        } catch (NoSuchElementException e) {
            response.setStatus(404);
            response.getWriter().write("{\"error\": \""+e.getMessage() + "\"}");
        } catch (Exception e) {
            response.setStatus(500);
            response.getWriter().write("{\"error\": \"Internal server error: " + e.getMessage() + "\"}");
        }
    }
}
