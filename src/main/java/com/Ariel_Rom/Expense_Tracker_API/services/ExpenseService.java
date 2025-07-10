package com.Ariel_Rom.Expense_Tracker_API.services;

// Importaciones necesarias del proyecto y librerías
import com.Ariel_Rom.Expense_Tracker_API.Utils.AuthUtils;
import com.Ariel_Rom.Expense_Tracker_API.dtos.ExpenseRequestDTO;
import com.Ariel_Rom.Expense_Tracker_API.dtos.ExpenseResponseDTO;
import com.Ariel_Rom.Expense_Tracker_API.mappers.ExpenseMapper;
import com.Ariel_Rom.Expense_Tracker_API.models.ExpenseEntity;
import com.Ariel_Rom.Expense_Tracker_API.models.UserEntity;
import com.Ariel_Rom.Expense_Tracker_API.models.enums.ExpenseCategoryEnum;
import com.Ariel_Rom.Expense_Tracker_API.models.enums.FilteredPeriodEnum;
import com.Ariel_Rom.Expense_Tracker_API.repositories.ExpenseRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service // Clase que contiene la lógica de negocio para manejar gastos
@RequiredArgsConstructor // Constructor automático para inyectar dependencias finales
public class ExpenseService {

    private final ExpenseRepository expenseRepository; // Repo para operaciones con gastos en la DB
    private final ExpenseMapper expenseMapper;         // Mapper para convertir entre DTO y entidad
    private final AuthUtils authUtils;                 // Utilidad para obtener el usuario actual

    // Crea un nuevo gasto y lo vincula al usuario logueado
    public ExpenseResponseDTO createExpense(ExpenseRequestDTO newExpense){
        ExpenseEntity expenseEntity = expenseMapper.fromDTO(newExpense); // DTO -> Entidad
        expenseEntity.setUser(authUtils.getCurrentUser());               // Asocia el usuario actual
        ExpenseEntity savedExpense = expenseRepository.save(expenseEntity); // Guarda en DB
        return expenseMapper.toDTO(savedExpense);                        // Entidad -> DTO para respuesta
    }

    // Obtiene un gasto por ID, validando que pertenezca al usuario logueado
    public ExpenseResponseDTO getExpenseByID(Long id) throws AccessDeniedException {
        return expenseMapper.toDTO(getUserExpenseOrThrow(id)); // Usa método privado que verifica acceso
    }

    // Devuelve todos los gastos del usuario autenticado
    public List<ExpenseResponseDTO> getExpenses(){
        UserEntity currentUser = authUtils.getCurrentUser();
        return expenseRepository.findAllByUser(currentUser) // Busca gastos del usuario
                .stream()
                .map(expenseMapper::toDTO) // Convierte a DTOs
                .collect(Collectors.toList());
    }

    // Obtiene gastos filtrados por periodo y categoría, para el usuario actual
    public List<ExpenseResponseDTO> getFilterExpenses(FilteredPeriodEnum period, ExpenseCategoryEnum category, LocalDate start, LocalDate end){
        UserEntity currentUser = authUtils.getCurrentUser();

        LocalDate startDate;
        LocalDate endDate = LocalDate.now();

        // Define fechas según periodo solicitado
        switch (period) {
            case LAST_WEEK:
                startDate = endDate.minusWeeks(1);
                break;
            case LAST_MONTH:
                startDate = endDate.minusMonths(1);
                break;
            case LAST_3_MONTHS:
                startDate = endDate.minusMonths(3);
                break;
            case CUSTOM:
                if (start == null || end == null){
                    throw new IllegalArgumentException("Custom filter requires both start and end dates");
                }
                startDate = start;
                endDate = end;
                break;
            default:
                throw new IllegalArgumentException("Invalid filter period");
        }

        return expenseRepository.findFilteredExpenses(currentUser, category, startDate, endDate)
                .stream()
                .map(expenseMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Actualiza un gasto si pertenece al usuario autenticado
    public ExpenseResponseDTO updateExpense(ExpenseRequestDTO newExpense, Long id) throws AccessDeniedException {
        ExpenseEntity updatingExpense = getUserExpenseOrThrow(id);   // Verifica que el gasto sea del usuario
        expenseMapper.updateExpense(updatingExpense, newExpense);    // Aplica cambios del DTO
        ExpenseEntity savedExpense = expenseRepository.save(updatingExpense); // Guarda en DB
        return expenseMapper.toDTO(savedExpense);                    // Devuelve DTO actualizado
    }

    // Elimina un gasto si pertenece al usuario autenticado
    public void deleteExpense(Long id) throws AccessDeniedException {
        ExpenseEntity deletingExpense = getUserExpenseOrThrow(id); // Verifica propiedad del gasto
        expenseRepository.delete(deletingExpense);                 // Elimina de la DB
    }

    // Método privado que valida que el gasto exista y sea del usuario actual
    private ExpenseEntity getUserExpenseOrThrow(Long id) throws AccessDeniedException {
        ExpenseEntity expense = expenseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Expense with ID: " + id + " not found"));

        UserEntity currentUser = authUtils.getCurrentUser();

        // Si el gasto no pertenece al usuario autenticado, lanza excepción
        if (!expense.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You can't access this resource");
        }

        return expense;
    }
}
