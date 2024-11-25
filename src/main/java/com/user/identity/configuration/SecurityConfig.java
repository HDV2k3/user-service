package com.user.identity.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final String[] PUBLIC_ENDPOINTS = {
        "/users/get-by-id/**",
            "/users/create",
            "/users/update/**",
            "/auth/login",
            "/auth/introspect",
            "/auth/logout",
            "/auth/refresh",
            "/swagger-ui/**",
            "/api-docs/**",
            "/users/**",
            "/auth/**",
            "/roles/**",
            "/permissions/**"

    };

    // Custom JWT decoder for handling token validation and extraction
    private final CustomJwtDecoder customJwtDecoder;

    // Constructor to inject the custom JWT decoder
    public SecurityConfig(CustomJwtDecoder customJwtDecoder) {
        this.customJwtDecoder = customJwtDecoder;
    }

    /**
     * Configures the security filter chain to define security policies such as:
     * - Defining public and protected endpoints.
     * - Enabling OAuth2 JWT authentication.
     * - Disabling CSRF protection (suitable for stateless services like APIs).
     * - Using stateless session management (no server-side session storage).
     *
     * @param httpSecurity HttpSecurity object to define security configurations.
     * @return SecurityFilterChain object containing the security filter settings.
     * @throws Exception in case of configuration errors.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // Configures authorization rules for different endpoints
                .authorizeHttpRequests(request -> request
                        // Permit GET, POST, and PUT requests to public endpoints
                        .requestMatchers(HttpMethod.GET, PUBLIC_ENDPOINTS).permitAll()
                        .requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINTS).permitAll()
                        .requestMatchers(HttpMethod.PUT, PUBLIC_ENDPOINTS).permitAll()
                        .requestMatchers(HttpMethod.DELETE, PUBLIC_ENDPOINTS).permitAll()
                        // Require authentication for all other requests
                        .anyRequest().authenticated())
                // Configures OAuth2 JWT-based authentication
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwtConfigurer -> jwtConfigurer
                                // Use custom JWT decoder
                                .decoder(customJwtDecoder)
                                // Set up the JWT authentication converter for role extraction
                                .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                        // Custom entry point for authentication exceptions (e.g., invalid token)
                        .authenticationEntryPoint(new JwtAuthenticationEntryPoint()))
                // Disables CSRF protection (suitable for stateless APIs)
                .csrf(AbstractHttpConfigurer::disable)
                // CORS configuration to allow cross-origin requests
                .cors(withDefaults())
                // Configures stateless session management (no sessions maintained)
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return httpSecurity.build();
    }

    /**
     * Configures the JwtAuthenticationConverter to extract roles/authorities from the JWT token without adding a prefix.
     * The JwtGrantedAuthoritiesConverter is responsible for mapping authorities in the JWT token to Spring Security's roles.
     *
     * @return JwtAuthenticationConverter for extracting authorities from the JWT token.
     */
    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        // Create a converter to extract granted authorities (roles) from the JWT
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        // Set an empty prefix for the authorities, as we do not require a specific prefix
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");

        // Create the JWT authentication converter and set the granted authorities converter
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }

    /**
     * Configures CORS settings to allow cross-origin requests from any origin, with any HTTP method and headers.
     * This is useful for enabling access to the API from different frontends or clients.
     *
     * @return CorsFilter object for managing CORS requests.
     */
    @Bean
    public CorsFilter corsFilter() {
        // Create and configure a new CORS configuration
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        // Allow requests from any origin
        corsConfiguration.addAllowedOrigin("*");
        // Allow all HTTP methods (GET, POST, PUT, etc.)
        corsConfiguration.addAllowedMethod("*");
        // Allow all headers (e.g., Authorization, Content-Type)
        corsConfiguration.addAllowedHeader("*");
    /// tt
        // Create a CORS configuration source and apply the configuration to all endpoints
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);

        // Return a new CorsFilter with the configuration source
        return new CorsFilter(urlBasedCorsConfigurationSource);
    }
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
}
