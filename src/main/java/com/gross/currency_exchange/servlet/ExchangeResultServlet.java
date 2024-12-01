package com.gross.currency_exchange.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gross.currency_exchange.dto.ExchangeResult;
import com.gross.currency_exchange.service.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.NoSuchElementException;


@WebServlet("/exchange")
public class ExchangeResultServlet extends HttpServlet {
    private final ExchangeResultService exchangeResultService = new ExchangeResultServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws  IOException {

        String baseCurrencyCode = request.getParameter("from");
        String targetCurrencyCode = request.getParameter("to");
        BigDecimal amount = new BigDecimal(request.getParameter("amount"));
        ExchangeResult exchangeResult;
        try {
            exchangeResult = exchangeResultService.
                    getExchangeResult(baseCurrencyCode, targetCurrencyCode, amount);

        ObjectMapper objectMapper = new ObjectMapper();
        if (exchangeResult != null)
        response.getWriter().write(objectMapper.writeValueAsString(exchangeResult));
        else {
            response.setStatus(400);
            response.getWriter().write("Exchange is not possible: no valid exchange rate found for the specified currencies.");
        }
        }catch (NoSuchElementException e)
        {
            response.setStatus(404);
            response.getWriter().write(e.getMessage());
        }
    }


}

