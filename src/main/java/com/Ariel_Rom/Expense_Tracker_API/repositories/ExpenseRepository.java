package com.Ariel_Rom.Expense_Tracker_API.repositories;

import com.Ariel_Rom.Expense_Tracker_API.models.ExpenseEntity;
import com.Ariel_Rom.Expense_Tracker_API.models.UserEntity;
import com.Ariel_Rom.Expense_Tracker_API.models.enums.ExpenseCategoryEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository // Marca esta interfaz como componente Spring para acceder a la base de datos
public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Long> {

    // Query personalizada para filtrar gastos por usuario, categoría (opcional) y rango de fechas
    @Query("SELECT e FROM ExpenseEntity e " +
            "WHERE e.user = :user " +  // Filtra solo los gastos del usuario dado
            "AND (:category IS NULL OR e.expenseCategory = :category) " + // Filtra por categoría si no es null
            "AND e.expenseDate BETWEEN :startDate AND :endDate") // Filtra gastos entre fechas startDate y endDate
    List<ExpenseEntity> findFilteredExpenses(
            @Param("user") UserEntity user,
            @Param("category") ExpenseCategoryEnum category,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    // Metodo que obtiene todos los gastos de un usuario específico
    List<ExpenseEntity> findAllByUser(UserEntity user);
}
