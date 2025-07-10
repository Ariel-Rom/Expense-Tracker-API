package com.Ariel_Rom.Expense_Tracker_API.models.enums;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

// Enum que representa las categorías posibles para un gasto

@Getter // Genera getter para el campo label
@RequiredArgsConstructor // Genera constructor con todos los campos finales (label en este caso)
@JsonDeserialize(using = ExpenseCategoryEnumDeserializer.class)
// Indica que para deserializar JSON a este enum se use una clase personalizada (ExpenseCategoryEnumDeserializer)
public enum ExpenseCategoryEnum {

    HEALTH("Health"),
    ELECTRONICS("Electronics"),
    LEISURE("Leisure"),
    UTILITIES("Utilities"),
    GROCERIES("Groceries"),
    CLOTHING("Clothing"),
    SERVICES("Services"),
    OTHERS("Others");

    private final String label; // Etiqueta legible para cada categoría

}
