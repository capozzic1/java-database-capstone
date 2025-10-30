package com.project.back_end.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull; 

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        // Allow CORS for all endpoints but only from the Angular dev server
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:4200, http://localhost:4000")  // restrict wildcard when using credentials
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // Specify allowed methods
                .allowedHeaders("*")  // You can restrict headers if needed
                .allowCredentials(true); // required when frontend uses withCredentials = true
    }
}
