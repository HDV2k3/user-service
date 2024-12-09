package com.user.identity.configuration;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * AuthenticationRequestInterceptor is a Feign RequestInterceptor that retrieves
 * the current request's Authorization header and passes it along in the Feign request.
 * This ensures the token-based authentication is propagated in downstream requests.
 */
@Slf4j
public class AuthenticationRequestInterceptor implements RequestInterceptor {

    /**
     * Intercepts the Feign request to inject the Authorization header from the current HTTP request.
     *
     * @param requestTemplate The Feign request template to which the Authorization header will be added.
     */
    @Override
    public void apply(RequestTemplate requestTemplate) {
        // Retrieve current request attributes
        ServletRequestAttributes servletRequestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        // Ensure the servletRequestAttributes is not null
        if (servletRequestAttributes != null) {
            String authHeader = servletRequestAttributes.getRequest().getHeader("Authorization");

            // Log the extracted header for debugging purposes
            log.info("Authorization Header: {}", authHeader);

            if (StringUtils.hasText(authHeader)) {
                // Strip 'Bearer ' prefix if it exists
                if (authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                    log.info("Authorization Header final: {}", authHeader);

                }

                // Add 'Bearer ' prefix to the token before sending it in the Feign request
                requestTemplate.header("Authorization", "Bearer " + authHeader);
            }
        }
    }
}
