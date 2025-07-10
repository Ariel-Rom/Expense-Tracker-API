package com.Ariel_Rom.Expense_Tracker_API.security;

import com.Ariel_Rom.Expense_Tracker_API.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration // Marca esta clase como configuración de Spring
@RequiredArgsConstructor // Genera constructor con todos los atributos finales (inyección automática)
public class AppConfig {

    private final UserRepository userRepository; // Repositorio para buscar usuarios en la base

    // Bean para el AuthenticationManager que Spring Security usa para autenticar usuarios
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // Bean que define el AuthenticationProvider, que maneja la lógica de autenticación
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        // Configura cómo obtener los detalles del usuario (UserDetailsService)
        authenticationProvider.setUserDetailsService(userDetailsService());
        // Configura el encoder para comparar passwords (bcrypt)
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    // Bean que define el encoder para encriptar las contraseñas usando BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Bean que define cómo buscar usuarios por username para autenticar
    @Bean
    public UserDetailsService userDetailsService() {
        // Busca en la base por username, lanza excepción si no encuentra usuario
        return username -> userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}

