package com.eventmesh.ingestion.security;

import com.eventmesh.common.dto.ApiKeyRole;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Custom Authentication Token for API Key based authentication.
 */
public class ApiKeyAuthenticationToken extends AbstractAuthenticationToken {

    private final String apiKey;
    private final String clientName;
    private final String role;

    public ApiKeyAuthenticationToken(String apiKey, String clientName, String role, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.apiKey = apiKey;
        this.clientName = clientName;
        this.role = role;
    }

    @Override
    public Object getCredentials() {
        return apiKey;
    }

    @Override
    public Object getPrincipal() {
        return apiKey;
    }

    public String getClientName() {
        return clientName;
    }

    public String getRole() {
        return role;
    }
}

