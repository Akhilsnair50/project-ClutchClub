package org.akhil.authorizationserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class BeansConfig {
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration cors = new CorsConfiguration();
        cors.addAllowedHeader("*");
        cors.addAllowedMethod("*");
        cors.addAllowedOrigin("http://localhost:4200");
        cors.addAllowedOrigin("http://127.0.0.1:8080");
        cors.addAllowedOrigin("http://localhost:8080");
        cors.addAllowedOrigin("http://127.0.0.1:4200");
        cors.setAllowCredentials(true);
        source.registerCorsConfiguration("/**",cors);
        return source;
    }
}
