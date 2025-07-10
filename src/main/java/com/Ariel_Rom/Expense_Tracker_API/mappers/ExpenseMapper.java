package com.Ariel_Rom.Expense_Tracker_API.mappers;

import com.Ariel_Rom.Expense_Tracker_API.dtos.ExpenseRequestDTO;
import com.Ariel_Rom.Expense_Tracker_API.dtos.ExpenseResponseDTO;
import com.Ariel_Rom.Expense_Tracker_API.models.ExpenseEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

// Mapper para convertir entre la entidad ExpenseEntity y los DTOs relacionados
// Usa MapStruct para generar código automático de mapeo
@Mapper(componentModel = "spring") // Inyectable como bean de Spring
public interface ExpenseMapper {

    // Actualiza una entidad ExpenseEntity existente con los datos del DTO
    // Ignora el id y createdAt para no modificarlos en la actualización
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateExpense(@MappingTarget ExpenseEntity oldExpense, ExpenseRequestDTO dto);

    // Convierte una entidad ExpenseEntity a ExpenseResponseDTO
    // Mapea la propiedad 'expenseCategory.label' de la entidad a 'category' del DTO
    @Mapping(source = "expenseCategory.label", target = "category")
    ExpenseResponseDTO toDTO(ExpenseEntity expenseEntity);

    // Convierte un ExpenseRequestDTO a ExpenseEntity para crear una nueva entidad
    // Ignora campos como id, user, createdAt y updatedAt porque se generan automáticamente
    // Mapea explícitamente el campo 'expenseCategory'
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(source = "expenseCategory", target = "expenseCategory")
    ExpenseEntity fromDTO(ExpenseRequestDTO dto);
}
