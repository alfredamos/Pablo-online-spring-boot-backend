package com.alfredamos.springbootbackend.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {

                registry.addMapping("/**") // Apply CORS to all paths
                        .allowedOrigins("http://localhost:4200", "http://localhost:5173", "http://localhost:5174") // Specify allowed origins
                        .allowedMethods("GET", "POST", "PUT", "PATCH","DELETE", "OPTIONS") // Allowed HTTP methods
                        .allowedHeaders("*") // Allowed headers (e.g., Content-Type, Authorization)
                        .allowCredentials(true) // Crucial for sending cookies/credentials
                        .maxAge(3600); // Max age of pre-flight OPTIONS request in seconds
            }
        };
    }
}