package com.Ariel_Rom.Expense_Tracker_API.security;

import com.Ariel_Rom.Expense_Tracker_API.services.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component // Marca esta clase como un componente Spring (inyectable)
@RequiredArgsConstructor // Genera constructor con todos los atributos finales (inyección automática)
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    // Filtro que se ejecuta una vez por cada request para validar el JWT y autenticar al usuario

    private final JWTService jwtService; // Servicio para manejar la lógica del token JWT
    private final UserDetailsService userDetailsService; // Servicio para cargar detalles de usuario

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Extrae el token JWT del header Authorization
        final String token = getTokenFromRequest(request);

        // Si no hay token, continúa sin autenticar (pasa el filtro)
        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // Obtiene el username que está codificado dentro del token
        final String username = jwtService.getUsernameFromToken(token);

        // Si username existe y no hay una autenticación previa configurada en el contexto
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Carga los detalles del usuario por username
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Verifica si el token es válido para ese usuario
            if (jwtService.isTokenValid(token, userDetails)) {
                // Crea un objeto de autenticación con los detalles y roles del usuario
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());

                // Añade detalles del request para usar en la autenticación
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Guarda la autenticación en el contexto de seguridad de Spring (usuario queda autenticado)
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        // Continúa con la cadena de filtros
        filterChain.doFilter(request, response);
    }

    // Metodo auxiliar para extraer el token JWT del header Authorization
    private String getTokenFromRequest(HttpServletRequest request) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        // Verifica que el header tenga texto y comience con "Bearer "
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            // Retorna solo el token sin el prefijo "Bearer "
            return authHeader.substring(7);
        }
        return null;
    }
}

