package de.webtech.backend.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Konfigurationsklasse für die Sicherheitseinstellungen der Webanwendung.
 * Verwendet Spring Security, um verschiedene Sicherheitsaspekte wie Authentifizierung, Autorisierung,
 * CORS, CSRF-Schutz und HTTP-Headers zu konfigurieren.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends AbstractHttpConfigurer<SecurityConfig, HttpSecurity> {

    /**
     * Erstellt eine Bean für den Passwortencoder, der zur Verschlüsselung der Passwörter verwendet wird.
     * Hier wird BCryptPasswordEncoder verwendet.
     *
     * @return Ein Passwortencoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Definiert die Sicherheitsfilterkette zur Konfiguration der Sicherheitseinstellungen.
     * Konfiguriert die Authentifizierungs- und Autorisierungsregeln für verschiedene Endpunkte.
     *
     * @param http HttpSecurity-Konfigurationsobjekt zur Definition von Sicherheitseinstellungen.
     * @return Die konfigurierte SecurityFilterChain.
     * @throws Exception bei Konfigurationsfehlern.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll() // Zugriff auf H2-Console erlauben
                                .requestMatchers(new AntPathRequestMatcher("/api/users/**")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/api/skins/**")).permitAll()
                                .anyRequest().authenticated()
                )
                .formLogin(AbstractAuthenticationFilterConfigurer::permitAll
                )
                .rememberMe(Customizer.withDefaults()
                )
                .cors(cors -> {
                })
                .csrf(AbstractHttpConfigurer::disable) // Deaktiviert CSRF-Schutz
                .headers(AbstractHttpConfigurer::disable // Deaktiviert alle Header, einschließlich derjenigen, die Frame-Optionen setzen
                );

        return http.build();
    }

    /**
     * Konfiguriert globale Authentifizierungseinstellungen.
     * Hier wird eine einfache In-Memory-Authentifizierung mit einem einzigen Benutzer definiert.
     *
     * @param auth AuthenticationManagerBuilder zur Konfiguration der Authentifizierung.
     * @throws Exception bei Konfigurationsfehlern.
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser("user").password("{noop}password").roles("USER");
    }

}


