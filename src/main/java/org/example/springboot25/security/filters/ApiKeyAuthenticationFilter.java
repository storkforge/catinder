package org.example.springboot25.security.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String apiKey = request.getParameter("apiKey");

        // Only process if API key is provided
        if (apiKey != null && !apiKey.isEmpty()) {
            if (isValidApiKey(apiKey)) {
                Authentication auth = new ApiKeyAuthenticationToken(apiKey);
                SecurityContextHolder.getContext().setAuthentication(auth);
                logger.debug("API key authentication successful");
            } else {
                // Log failed authentication attempt
                logger.warn("Invalid API key provided");
            }
            filterChain.doFilter(request, response);
        }
    }

    private boolean isValidApiKey(String apiKey) {
        return "apiKey".equals(apiKey);
    }
}
