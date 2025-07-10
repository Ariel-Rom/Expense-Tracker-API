package com.Ariel_Rom.Expense_Tracker_API.controllers;

import com.Ariel_Rom.Expense_Tracker_API.dtos.ExpenseRequestDTO;
import com.Ariel_Rom.Expense_Tracker_API.dtos.ExpenseResponseDTO;
import com.Ariel_Rom.Expense_Tracker_API.models.enums.ExpenseCategoryEnum;
import com.Ariel_Rom.Expense_Tracker_API.models.enums.FilteredPeriodEnum;
import com.Ariel_Rom.Expense_Tracker_API.services.ExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.util.List;

@RestController // Marca la clase como un controlador REST (responde con JSON, no vistas HTML)
@RequiredArgsConstructor // Crea un constructor con los atributos final (inyecta el servicio)
@RequestMapping("app/v1/expenses") // Ruta base para todos los endpoints de esta clase
public class ExpenseController {

    // Servicio que maneja la lógica de negocio para los gastos
    private final ExpenseService expenseService;

    // Endpoint POST para crear un nuevo gasto
    @PostMapping
    public ResponseEntity<ExpenseResponseDTO> createExpense(@RequestBody @Valid ExpenseRequestDTO createExpense){
        // Devuelve el gasto creado con estado HTTP 201 Created
        return ResponseEntity.status(HttpStatus.CREATED).body(expenseService.createExpense(createExpense));
    }

    // Endpoint GET para obtener un gasto por su ID
    @GetMapping("/{id}")
    public ResponseEntity<ExpenseResponseDTO> getExpenseByID(@PathVariable Long id) throws AccessDeniedException {
        // Devuelve el gasto correspondiente al ID (si pertenece al usuario autenticado)
        return ResponseEntity.ok(expenseService.getExpenseByID(id));
    }

    // Endpoint GET para obtener todos los gastos del usuario autenticado
    @GetMapping
    public ResponseEntity<List<ExpenseResponseDTO>> getAllExpenses(){
        return ResponseEntity.ok(expenseService.getExpenses());
    }

    // Endpoint GET para filtrar gastos por periodo, categoría y rango de fechas
    @GetMapping("/filter")
    public ResponseEntity<List<ExpenseResponseDTO>> filterExpenses(
            @RequestParam FilteredPeriodEnum period, // Ej: LAST_WEEK, LAST_MONTH, etc.
            @RequestParam(required = false) LocalDate start, // Fecha inicio personalizada (opcional)
            @RequestParam(required = false) LocalDate end, // Fecha fin personalizada (opcional)
            @RequestParam(required = false) ExpenseCategoryEnum category // Categoría específica (opcional)
    ) {
        // Aplica los filtros según los parámetros y devuelve la lista filtrada
        List<ExpenseResponseDTO> expenses = expenseService.getFilterExpenses(period, category, end, start);
        return ResponseEntity.ok(expenses);
    }

    // Endpoint PUT para actualizar un gasto por su ID
    @PutMapping("/{id}")
    public ResponseEntity<ExpenseResponseDTO> updateExpenseById(
            @RequestBody @Valid ExpenseRequestDTO newExpense, // Datos actualizados del gasto
            @PathVariable Long id // ID del gasto a actualizar
    ) throws AccessDeniedException {
        return ResponseEntity.ok(expenseService.updateExpense(newExpense, id));
    }

    // Endpoint DELETE para eliminar un gasto por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpenseById(@PathVariable Long id) throws AccessDeniedException {
        expenseService.deleteExpense(id);
        // Devuelve status 204 No Content si se eliminó correctamente
        return ResponseEntity.noContent().build();
    }
}

