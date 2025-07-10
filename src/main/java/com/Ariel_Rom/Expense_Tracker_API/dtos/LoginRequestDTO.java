package com.Ariel_Rom.Expense_Tracker_API.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO que representa los datos necesarios para el login de un usuario

@Data // Genera getters, setters, equals, hashCode y toString automáticamente
@AllArgsConstructor // Constructor con todos los campos
@NoArgsConstructor // Constructor vacío sin argumentos
@Builder // Permite crear instancias con patrón builder para mayor claridad
public class LoginRequestDTO {

    @NotBlank(message = "username is required") // El username no puede estar vacío ni ser nulo
    private String username;

    @NotBlank(message = "password is required") // El password no puede estar vacío ni ser nulo
    private String password;

}
