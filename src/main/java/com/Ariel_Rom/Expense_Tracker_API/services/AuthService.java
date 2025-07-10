package com.Ariel_Rom.Expense_Tracker_API.services;

import com.Ariel_Rom.Expense_Tracker_API.dtos.AuthResponseDTO;
import com.Ariel_Rom.Expense_Tracker_API.dtos.LoginRequestDTO;
import com.Ariel_Rom.Expense_Tracker_API.dtos.RegisterRequestDTO;
import com.Ariel_Rom.Expense_Tracker_API.models.UserEntity;
import com.Ariel_Rom.Expense_Tracker_API.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service // Marca esta clase como servicio de Spring (componente para lógica de negocio)
@RequiredArgsConstructor // Genera constructor con inyección automática para atributos finales
public class AuthService {

    private final UserRepository userRepository; // Repo para acceder a usuarios en la base
    private final JWTService jwtService; // Servicio para generar y validar tokens JWT
    private final AuthenticationManager authenticationManager; // Manager para autenticación de Spring Security
    private final PasswordEncoder passwordEncoder; // Encoder para hashear contraseñas (bcrypt)

    // Metodo para registrar un usuario nuevo
    public AuthResponseDTO register(RegisterRequestDTO requestDTO) {
        // Verifica que el username no esté en uso
        if(userRepository.findByUsername(requestDTO.getUsername()).isPresent()){
            throw new IllegalArgumentException("Username already taken");
        }

        // Crea un nuevo usuario con username y password hasheada
        UserEntity user = UserEntity.builder()
                .username(requestDTO.getUsername())
                .password(passwordEncoder.encode(requestDTO.getPassword()))
                .build();

        // Guarda el usuario en la base de datos
        userRepository.save(user);

        // Genera un token JWT para el usuario registrado y lo devuelve
        return AuthResponseDTO.builder()
                .token(jwtService.getToken(user))
                .build();
    }

    // Metodo para login de usuario ya registrado
    public AuthResponseDTO login(LoginRequestDTO requestDTO) {
        // Autentica al usuario con Spring Security (lanza excepción si falla)
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestDTO.getUsername(), requestDTO.getPassword())
        );

        // Busca los detalles del usuario para generar el token
        UserDetails user = userRepository.findByUsername(requestDTO.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // Genera el token JWT para el usuario autenticado
        String token = jwtService.getToken(user);

        // Devuelve el token en la respuesta
        return AuthResponseDTO.builder()
                .token(token)
                .build();
    }
}

