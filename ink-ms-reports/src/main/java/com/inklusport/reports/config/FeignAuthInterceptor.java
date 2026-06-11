package com.inklusport.reports.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class FeignAuthInterceptor implements RequestInterceptor {

    private static final Logger log = LoggerFactory.getLogger(FeignAuthInterceptor.class);

    @Override
    public void apply(RequestTemplate template) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.getCredentials() != null) {
            String token = authentication.getCredentials().toString();
            template.header("Authorization", "Bearer " + token);
            log.debug("Token agregado a petición Feign: {}", template.url());
        } else {
            log.warn("No hay token disponible para petición Feign: {}", template.url());
        }
    }
}