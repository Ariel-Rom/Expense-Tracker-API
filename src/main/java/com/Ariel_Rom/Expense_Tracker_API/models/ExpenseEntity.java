package com.Ariel_Rom.Expense_Tracker_API.models;

import com.Ariel_Rom.Expense_Tracker_API.models.enums.ExpenseCategoryEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter // Genera getters para todos los campos
@Setter // Genera setters para todos los campos
@Entity // Marca esta clase como una entidad JPA que se mapea a una tabla en la base de datos
@NoArgsConstructor // Constructor vacío requerido por JPA
@AllArgsConstructor // Constructor con todos los campos
@Table(name = "expenses") // Nombre de la tabla en la base de datos
public class ExpenseEntity {

    @Id // Marca este campo como la clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Genera el ID automáticamente (auto-incremental)
    private Long id;

    @ManyToOne // Relación muchos a uno con UserEntity (varios gastos pueden pertenecer a un usuario)
    @JoinColumn(name = "user_id") // Columna en la tabla expenses que referencia al usuario
    @NotNull // No puede ser nulo (debe tener un usuario asignado)
    private UserEntity user;

    @Enumerated(EnumType.STRING) // Guarda el enum como texto en la base de datos
    @Column(nullable = false, length = 100) // No puede ser nulo y tiene longitud máxima
    private ExpenseCategoryEnum expenseCategory;

    @Positive // Valida que el monto sea positivo (mayor que cero)
    @Column(nullable = false) // No puede ser nulo en la DB
    private BigDecimal amount;

    @NotNull // No puede ser nulo
    @Column(nullable = false) // No puede ser nulo en la DB
    private LocalDate expenseDate;

    @Column(nullable = false) // No puede ser nulo
    private LocalDateTime createdAt;

    @Column(nullable = false) // No puede ser nulo
    private LocalDateTime updatedAt;

    // Método que se ejecuta justo antes de persistir (insertar) el registro en la DB
    // Asigna la fecha y hora actual a createdAt
    @PrePersist
    public void onCreate(){
        this.createdAt = LocalDateTime.now();
    }

    // Método que se ejecuta justo antes de actualizar el registro en la DB
    // Actualiza la fecha y hora de modificación en updatedAt
    @PreUpdate
    public void onUpdate(){
        this.updatedAt = LocalDateTime.now();
    }
}
