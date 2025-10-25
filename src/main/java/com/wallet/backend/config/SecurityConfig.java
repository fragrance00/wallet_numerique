package com.wallet.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // désactive la protection CSRF
                .headers(headers -> headers.frameOptions(frame -> frame.disable())) // permet l'affichage des frames H2
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**").permitAll() // autorise l’accès à la console H2
                        .anyRequest().permitAll() // autorise toutes les autres routes
                );

        return http.build();
    }
}
