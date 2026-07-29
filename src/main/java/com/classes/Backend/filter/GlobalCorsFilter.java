package com.classes.Backend.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalCorsFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                      HttpServletResponse response,
                                      FilterChain filterChain) throws ServletException, IOException {
        String origin = request.getHeader("Origin");
        if (origin != null && isAllowedOrigin(origin)) {
            response.setHeader("Access-Control-Allow-Origin", origin);
        }
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, PATCH, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Max-Age", "3600");

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean isAllowedOrigin(String origin) {
        return origin.equals("http://localhost:3000")
                || origin.equals("http://localhost:3001")
                || origin.equals("http://localhost:3002")
                || origin.equals("https://console.myclasses.co.in")
                || origin.equals("https://admin.myclasses.co.in")
                || origin.equals("https://myclasses.co.in")
                || origin.equals("https://www.myclasses.co.in");
    }
}
