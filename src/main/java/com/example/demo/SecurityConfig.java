package com.example.demo;

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

    // Define el algoritmo de hash (BCrypt) para las contraseñas
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Define las reglas de acceso (el Administrador)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        
        // 1. DESACTIVAR CSRF para la consola H2 (Necesario para permitir el POST)
        http
            .csrf((csrf) -> csrf
                .ignoringRequestMatchers("/h2-console/**")
            );

        http
            .authorizeHttpRequests((requests) -> requests
                // 2. PERMITIR ACCESO TOTAL A LA CONSOLA H2
                .requestMatchers("/h2-console/**").permitAll() 
                
                // Regla crítica: Solo ADMIN puede acceder a /admin/**
                .requestMatchers("/admin/**").hasAuthority("ADMIN")
                
                // Permite acceso libre al login, CSS, JS, etc.
                .requestMatchers("/", "/login", "/css/**", "/js/**").permitAll()
                
                // Todas las demás URLs requieren autenticación
                .anyRequest().authenticated()
            )
            .formLogin((form) -> form
                .loginPage("/login")
                .defaultSuccessUrl("/admin/dashboard", true)
                .permitAll()
            )
            .logout((logout) -> logout.permitAll());

        // 3. PERMITIR MOSTRAR EL FRAME (para evitar que el navegador bloquee la consola)
        http.headers((headers) -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin())); 

        return http.build();
    }
}