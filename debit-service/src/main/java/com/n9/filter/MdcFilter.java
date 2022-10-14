package com.n9.filter;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

//@WebFilter
@Component
@Order(1)
public class MdcFilter extends HttpFilter {

    private static final String CORRELATION_ID = "CorrelationId";

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        try {
            String correlationIdVal = getCorrelationId(request);
            MDC.put(CORRELATION_ID, correlationIdVal);
            filterChain.doFilter(request, response);

            response.addHeader(CORRELATION_ID, correlationIdVal);

        } finally {
            MDC.remove(CORRELATION_ID);
        }
    }

    private String getCorrelationId(HttpServletRequest request) {

        final String token;

        if (!StringUtils.isEmpty(request.getHeader(CORRELATION_ID))) {
            token = request.getHeader(CORRELATION_ID);
        } else {
            token = UUID.randomUUID().toString();
        }
        return token;
    }
}