package com.Ariel_Rom.Expense_Tracker_API.controllers;

import com.Ariel_Rom.Expense_Tracker_API.dtos.AuthResponseDTO;
import com.Ariel_Rom.Expense_Tracker_API.dtos.LoginRequestDTO;
import com.Ariel_Rom.Expense_Tracker_API.dtos.RegisterRequestDTO;
import com.Ariel_Rom.Expense_Tracker_API.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Marca la clase como un controlador REST (devuelve JSON en lugar de vistas HTML)
@RestController
// Inyecta automáticamente el constructor con los atributos marcados como final (en este caso, authService)
@RequiredArgsConstructor
// Ruta base para todos los endpoints de esta clase
@RequestMapping("app/v1/auth")
public class AuthController {

    // Servicio que maneja la lógica de autenticación y registro
    private final AuthService authService;

    // Endpoint POST para registrar un nuevo usuario
    // @Valid asegura que los campos del DTO cumplan con las validaciones definidas
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@RequestBody @Valid RegisterRequestDTO requestRegister){
        // Llama al metodo register del servicio y devuelve la respuesta con status 200 OK
        return ResponseEntity.ok(authService.register(requestRegister));
    }

    // Endpoint POST para iniciar sesión
    // También valida el DTO de login
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid LoginRequestDTO requestLogin){
        // Llama al metodo login del servicio y devuelve la respuesta con status 200 OK
        return ResponseEntity.ok(authService.login(requestLogin));
    }
}
