package com.eventmesh.ingestion.auth.filter;

import com.eventmesh.common.security.AuthEntryPoint;
import com.eventmesh.ingestion.auth.entity.ApiKeyEntity;
import com.eventmesh.ingestion.auth.exception.CustomAuthException;
import com.eventmesh.ingestion.auth.service.ApiKeyService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class ApiKeyAuthFilter extends OncePerRequestFilter {
    private final ApiKeyService apiKeyService;

    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();

        //Allow public endpoints
        if(isPublicEndpoint(path)){
            filterChain.doFilter(request, response);
            return;
        }

        String apiKey = request.getHeader("x-api-key");

        if(apiKey == null || apiKey.isBlank()){
            log.warn("Missing API Key for request: {}", path);
            AuthEntryPoint.handleUnauthorized(request, response, "Missing API Key");
            return;
        }
        try{
            ApiKeyEntity entity = apiKeyService.validateApiKeyHeader(apiKey);

            //Inject role into request for downstream usage
            request.setAttribute("role", entity.getRole());
            request.setAttribute("clientName", entity.getClientName());

            filterChain.doFilter(request, response);
        } catch (CustomAuthException ex){
            log.warn("Unauthorized request: {}", ex.getMessage());
            AuthEntryPoint.handleUnauthorized(request, response, ex.getMessage());
        }
    }

    /*Public endpoints that don't require authentication*/
    private boolean isPublicEndpoint(String path){
        return  path.contains("/actuator") || path.contains("/health");
    }
}
