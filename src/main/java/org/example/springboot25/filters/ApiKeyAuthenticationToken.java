package org.example.springboot25.filters;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;

public class ApiKeyAuthenticationToken extends AbstractAuthenticationToken {
    private final String apiKey;

    public ApiKeyAuthenticationToken(String apiKey) {
        super(null); //No authorities initially
        this.apiKey = apiKey;
        setAuthenticated(true);
    }

    public String getApiKey() {
            return apiKey;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    //Method that fetches user data like username, user role, etc.
    //Set with getContext inside ApiKeyAuthenticationFilter
    @Override
    public Object getPrincipal() {
        return apiKey;
    }
}
