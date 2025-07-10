package com.Ariel_Rom.Expense_Tracker_API.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO que representa los datos necesarios para registrar un nuevo usuario

@Data // Genera getters, setters, equals, hashCode y toString automáticamente
@NoArgsConstructor // Constructor vacío sin argumentos
@AllArgsConstructor // Constructor con todos los campos
@Builder // Permite construir instancias con patrón builder para claridad
public class RegisterRequestDTO {

    @NotBlank(message = "username is required") // El username no puede ser vacío ni nulo
    private String username;

    @NotBlank(message = "password is required") // El password no puede ser vacío ni nulo
    private String password;

}

