package de.webtech.backend.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers(PathRequest.toH2Console()).permitAll() // Zugriff auf H2-Console erlauben
                                .anyRequest().permitAll()
                )
                .csrf(csrf -> csrf.disable()) // Deaktiviert CSRF-Schutz
                .headers(headers -> headers
                        .disable() // Deaktiviert alle Header, einschließlich derjenigen, die Frame-Optionen setzen
                );

        return http.build();
    }





}
