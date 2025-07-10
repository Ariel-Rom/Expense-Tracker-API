package com.Ariel_Rom.Expense_Tracker_API.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO que representa la respuesta de autenticación
// Contiene el token JWT que el cliente usará para autorizar solicitudes futuras

@Data // Genera getters, setters, equals, hashCode y toString automáticamente
@AllArgsConstructor // Genera constructor con todos los campos
@NoArgsConstructor // Genera constructor vacío (sin argumentos)
@Builder // Permite construir objetos con patrón builder para más claridad al crear instancias
public class AuthResponseDTO {

    private String token; // Token JWT devuelto después de login o registro

}

