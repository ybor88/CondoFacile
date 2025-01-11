package com.condofacile.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disabilita CSRF solo se necessario
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/residenti/**").permitAll() // Endpoint pubblici
                        .anyRequest().authenticated() // Tutti gli altri richiedono autenticazione
                )
                .httpBasic(Customizer.withDefaults()); // Abilita autenticazione HTTP Basic

        return http.build();
    }
}