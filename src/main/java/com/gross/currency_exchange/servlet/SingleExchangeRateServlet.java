package com.gross.currency_exchange.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gross.currency_exchange.dao.CurrencyDAOImpl;
import com.gross.currency_exchange.dao.ExchangeRateDAOImpl;
import com.gross.currency_exchange.dto.ExchangeRateDTO;
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

import java.io.IOException;
import java.math.BigDecimal;
import java.util.NoSuchElementException;

@WebServlet("/exchangerate/*")
public class SingleExchangeRateServlet extends HttpServlet {

    private final ExchangeRateService exchangeRateService;
    private final ObjectMapper objectMapper;

    public SingleExchangeRateServlet() {
        CurrencyService currencyService = new CurrencyServiceImpl(new CurrencyDAOImpl());
        exchangeRateService = new ExchangeRateServiceImpl(new ExchangeRateDAOImpl(), currencyService);
        objectMapper = new ObjectMapper();
    }

    private ExchangeRateDTO exchangeRateDTORequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = request.getPathInfo();
        if (path == null || path.length() < 7 || !path.matches("(?i)/[a-z]{6}")) {
            response.setStatus(400);
            response.getWriter().write("{\"error\": \"Invalid currency codes. Expected format: exchangerate/USDEUR\"}");
            return null;
        }
        try {
            String baseCurrencyCode = path.substring(1, 4).toUpperCase();
            String targetCurrencyCode = path.substring(4, 7).toUpperCase();
            ExchangeRateDTO exchangeRateDTO = exchangeRateService.getExchangeRate(baseCurrencyCode, targetCurrencyCode);
            return exchangeRateDTO;
        } catch (NoSuchElementException e) {
            response.setStatus(404);
            response.getWriter().write("{\" Exchange rate not found: " + e.getMessage() + "\"}");
        }catch (EntityNotFoundException e)
        {response.setStatus(404);
            response.getWriter().write(e.getMessage());}
       catch (Exception e) {
            response.setStatus(500);
            response.getWriter().write("{\"error\": \"Internal server error: " + e.getMessage() + "\"}");
        }
        return null;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ExchangeRateDTO exchangeRateDTO = exchangeRateDTORequest(request, response);
        if (exchangeRateDTO != null) {
            response.setStatus(200);
            response.getWriter().write(objectMapper.writeValueAsString(exchangeRateDTO));
        }
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Incoming request: " + req.getMethod() + " "
                + req.getRequestURI());
        if ("PATCH".equalsIgnoreCase(req.getMethod())) {
            doPatch(req, resp);
        } else {
            super.service(req, resp);
        }
    }

    @Override
    protected void doPatch(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("doPatch works");
        ExchangeRateDTO exchangeRateDTO = exchangeRateDTORequest(request, response);
        if (exchangeRateDTO == null)
            return;

        String rateString = extractTheRateParameterFromTheRequest(request);
        System.out.println(rateString);
        if (rateString == null || rateString.isEmpty()) {
            response.setStatus(400);
            response.getWriter().write("{\"error\": \"Invalid rate\"}");
            return;
        }

        try {
            BigDecimal rate = new BigDecimal(rateString);
            exchangeRateService.updateExchangeRate(exchangeRateDTO, rate);
            response.setStatus(200);
            objectMapper.writeValue(response.getWriter(), exchangeRateDTO);
        } catch (Exception e) {
            response.setStatus(500);
            response.getWriter().write("{\"error\": \"Internal Server Error:" + e.getMessage() + "\"}");
        }
    }

    private String extractTheRateParameterFromTheRequest(HttpServletRequest request) throws IOException {

        String body = new String(request.getInputStream().readAllBytes());
        String rateString = null;
        System.out.println(body);
        for (String param : body.split("&")) {
            String[] keyValue = param.split("=");
            if (keyValue.length == 2 && "rate".equals(keyValue[0])) {
                rateString = keyValue[1];
                break;
            }
        }
        return rateString;
    }
}
