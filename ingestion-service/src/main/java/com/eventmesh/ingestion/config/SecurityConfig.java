package com.eventmesh.ingestion.config;

import com.eventmesh.common.filter.CorrelationIdFilter;
import com.eventmesh.ingestion.auth.filter.ApiKeyAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final ApiKeyAuthFilter apiKeyAuthFilter;
    private final CorrelationIdFilter correlationIdFilter;

    /*Correlation Filter (runs First)*/
    @Bean
    public FilterRegistrationBean<CorrelationIdFilter> correlationFilter() {
        FilterRegistrationBean<CorrelationIdFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(correlationIdFilter);
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE); //First
        return registrationBean;
    }

    /*API Key Auth Filter (runs after correlation)*/
    @Bean
    public FilterRegistrationBean<ApiKeyAuthFilter> apiKeyFilter() {
        FilterRegistrationBean<ApiKeyAuthFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(apiKeyAuthFilter);
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE + 1);
        return registrationBean;
    }
}
