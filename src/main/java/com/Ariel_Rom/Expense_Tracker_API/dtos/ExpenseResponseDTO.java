package com.Ariel_Rom.Expense_Tracker_API.dtos;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

// DTO que representa la respuesta con los datos de un gasto
// Se usa para enviar la info del gasto al cliente (sin exponer entidades internas)

@Setter // Genera setters
@Getter // Genera getters
public class ExpenseResponseDTO {

    private String category; // Categoría del gasto como texto (string)

    private BigDecimal amount; // Monto del gasto

    private LocalDate expenseDate; // Fecha del gasto
}

