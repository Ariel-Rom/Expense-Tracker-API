package com.Ariel_Rom.Expense_Tracker_API.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

// Clase que representa la estructura estándar para respuestas de error en la API

@Data // Genera getters, setters, equals, hashCode y toString automáticamente
@Builder // Permite construir instancias con patrón builder para claridad
@AllArgsConstructor // Constructor con todos los campos
public class ApiErrorResponse {

    private LocalDateTime timestamp; // Fecha y hora en que ocurrió el error

    private int status; // Código HTTP del error (ej: 400, 404, 500)

    private String error; // Tipo o nombre del error (ej: "Bad Request")

    private String message; // Mensaje descriptivo del error

    private Map<String, String> errors;
    // Mapa de errores específicos, usado principalmente para errores de validación
    // (null si no aplica)
}
