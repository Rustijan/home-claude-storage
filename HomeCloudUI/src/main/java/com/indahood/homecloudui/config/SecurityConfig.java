package com.indahood.homecloudui.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.StaticHeadersWriter;


@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Keep disabled for now since UI acts as proxy
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .headers(headers -> headers
                        .contentSecurityPolicy(csp -> csp
                                .policyDirectives("default-src 'self'; " +
                                        "script-src 'self' 'unsafe-inline'; " +
                                        "style-src 'self' 'unsafe-inline'; " +
                                        "img-src 'self' data:; " +
                                        "font-src 'self' https://assets.ngrok.com; " + // front error
                                        "connect-src 'self' https://*.ngrok-free.app https://*.ngrok-free.dev;")
                        )
                );
        return http.build();
    }


//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .headers(headers -> headers
//                        // Disable all default security headers
//                        .defaultsDisabled()
//                        // Allow everything (temporary fix)
//                        .addHeaderWriter(new StaticHeadersWriter(
//                                "Content-Security-Policy",
//                                "default-src * 'unsafe-eval' 'unsafe-inline' data: blob:; " +
//                                        "img-src * data: blob:; " +
//                                        "font-src * data:; " +
//                                        "style-src * 'unsafe-inline'; " +
//                                        "script-src * 'unsafe-eval' 'unsafe-inline';"
//                        ))
//                )
//                // Allow all requests (since you don't have auth yet)
//                .authorizeHttpRequests(auth -> auth
//                        .anyRequest().permitAll()
//                )
//                // Disable CSRF for testing
//                .csrf(csrf -> csrf.disable());
//
//        return http.build();
//    }

}
