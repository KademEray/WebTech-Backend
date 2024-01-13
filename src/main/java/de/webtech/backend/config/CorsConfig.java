package de.webtech.backend.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;


/**
 * Konfigurationsklasse zur Definition der CORS (Cross-Origin Resource Sharing) Richtlinien.
 * Diese Klasse implementiert das WebMvcConfigurer-Interface, um benutzerdefinierte CORS-Richtlinien festzulegen.
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    /**
     * Konfiguriert CORS-Mappings für die gesamte Anwendung.
     *
     * Diese Methode definiert die CORS-Richtlinien, die auf alle Endpunkte der Anwendung angewendet werden.
     * Erlaubte Ursprünge, Methoden, Kopfzeilen und Exposition von Kopfzeilen können hier definiert werden.
     *
     * @param registry Das CORS-Registrierungsobjekt, mit dem die CORS-Richtlinien konfiguriert werden.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8081")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("Content-Type", "Authorization")
                .exposedHeaders("Custom-Header1", "Custom-Header2")
                .allowCredentials(true);
    }
}





