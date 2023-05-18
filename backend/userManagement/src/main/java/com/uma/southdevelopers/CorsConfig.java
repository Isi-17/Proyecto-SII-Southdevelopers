package com.uma.southdevelopers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Configura el mapeo para todas las rutas
                        .allowedOrigins("http://localhost:4200") // Permite solicitudes desde esta URL
                        .allowedMethods("GET", "POST", "PUT", "DELETE") // Permite los métodos HTTP permitidos
                        .allowCredentials(true); // Permite el envío de cookies
            }
        };
    }
}
