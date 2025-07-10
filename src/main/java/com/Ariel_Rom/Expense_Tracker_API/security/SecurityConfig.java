package com.Ariel_Rom.Expense_Tracker_API.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration // Marca esta clase como configuración de Spring
@EnableWebSecurity // Habilita la seguridad web de Spring Security
@RequiredArgsConstructor // Constructor con inyección automática de atributos finales
public class SecurityConfig {

    private final AuthenticationProvider authenticationProvider; // Proveedor de autenticación configurado (por ejemplo DaoAuthenticationProvider)
    private final JWTAuthenticationFilter jwTauthenticationFilter; // Filtro personalizado para validar tokens JWT

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                // Deshabilita CSRF porque estamos usando JWT (sin sesiones)
                .csrf(AbstractHttpConfigurer::disable)

                // Configura las reglas de autorización
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/app/v1/auth/**").permitAll(); // Permite acceso libre a rutas de autenticación (login, registro)
                    auth.anyRequest().authenticated(); // El resto de rutas requieren estar autenticado
                })

                // Configura la política de sesión como stateless (sin sesión, porque usamos tokens)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Define el proveedor de autenticación que usará Spring Security
                .authenticationProvider(authenticationProvider)

                // Agrega el filtro JWT antes del filtro de autenticación por usuario/contraseña tradicional
                .addFilterBefore(jwTauthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                // Construye la cadena de filtros y devuelve el objeto SecurityFilterChain
                .build();
    }
}

