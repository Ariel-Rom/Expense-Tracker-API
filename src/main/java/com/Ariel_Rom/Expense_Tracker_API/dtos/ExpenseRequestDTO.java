package com.Ariel_Rom.Expense_Tracker_API.dtos;

import com.Ariel_Rom.Expense_Tracker_API.models.enums.ExpenseCategoryEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;


// DTO que representa la información necesaria para crear o actualizar un gasto

@Setter // Genera setters para todos los campos
@Getter // Genera getters para todos los campos
@AllArgsConstructor // Constructor con todos los campos
@NoArgsConstructor // Constructor vacío sin argumentos
public class ExpenseRequestDTO {

    @NotNull // No puede ser nulo (requerido)
    @JsonProperty("category") // Mapea el campo JSON "category" a esta propiedad
    private ExpenseCategoryEnum expenseCategory; // Categoría del gasto (enum)

    @NotNull // Requerido
    @Positive // Debe ser un número positivo (> 0)
    private BigDecimal amount; // Monto del gasto

    @NotNull // Requerido
    @Column(nullable = false) // En la base de datos esta columna no puede ser nula (más para entidad, igual no molesta acá)
    private LocalDate expenseDate; // Fecha del gasto
}

