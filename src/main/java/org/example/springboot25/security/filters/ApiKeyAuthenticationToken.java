package org.example.springboot25.security.filters;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;

public class ApiKeyAuthenticationToken extends AbstractAuthenticationToken {
    private final String apiKey;

    public ApiKeyAuthenticationToken(String apiKey) {
        super(Collections.emptyList());
        this.apiKey = apiKey;
        super.setAuthenticated(false);
    }

    // Add a constructor for successful authentication with authorities
    public ApiKeyAuthenticationToken(String apiKey, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.apiKey = apiKey;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return new ApiKeyPrincipal(apiKey);
    }

    private static class ApiKeyPrincipal {
        private final String apiKey;

        public ApiKeyPrincipal(String apiKey) {
            this.apiKey = apiKey;
        }

        @Override
        public String toString() {
            return "API Client: " + apiKey.substring(0, 4) + "...";
        }
    }
}

