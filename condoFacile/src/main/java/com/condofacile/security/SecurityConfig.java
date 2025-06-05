package com.condofacile.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Configuration
public class SecurityConfig {

    private static final String AUTH_TOKEN = "Bearer eyJzdGF0aWMiOiAiY29uZG9mYWNpbGVfYXBwIiwgInJvbGUiOiAiYWRtaW4iLCAiZXhwaXJlcyI6ICIyMDI3LTEyLTMxIn0=";

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/condofacile/api/**").authenticated()  // SOLO queste protette
                        .anyRequest().permitAll()                                // tutto il resto libero
                )
                .addFilterBefore(new BearerTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    static class BearerTokenFilter extends OncePerRequestFilter {
        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                        FilterChain filterChain) throws ServletException, IOException {

            String path = request.getRequestURI();
            String authHeader = request.getHeader("Authorization");

            // Applica filtro SOLO se il path è una API
            if (path.startsWith("/condofacile/api")) {
                if (authHeader != null && authHeader.equals(AUTH_TOKEN)) {
                    var auth = new UsernamePasswordAuthenticationToken("api-user", null, Collections.emptyList());
                    SecurityContextHolder.getContext().setAuthentication(auth);
                } else {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Unauthorized: token mancante o non valido.");
                    return;
                }
            }

            filterChain.doFilter(request, response);
        }
    }
}