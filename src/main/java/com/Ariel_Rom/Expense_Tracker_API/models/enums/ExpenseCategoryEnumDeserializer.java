package com.Ariel_Rom.Expense_Tracker_API.models.enums;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

// Deserializador personalizado para convertir un string JSON a un valor del enum ExpenseCategoryEnum
public class ExpenseCategoryEnumDeserializer extends JsonDeserializer<ExpenseCategoryEnum> {

    @Override
    public ExpenseCategoryEnum deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        // Obtiene el texto recibido en el JSON (ej: "Food", "Transport", etc.)
        String label = jsonParser.getText();

        // Recorre todos los valores posibles del enum ExpenseCategoryEnum
        for (ExpenseCategoryEnum categoryEnum : ExpenseCategoryEnum.values()) {
            // Compara el texto del JSON con el label del enum ignorando mayúsculas/minúsculas
            if (categoryEnum.getLabel().equalsIgnoreCase(label)) {
                // Retorna el enum correspondiente si encuentra coincidencia
                return categoryEnum;
            }
        }

        // Si no encuentra coincidencia, lanza una excepción indicando valor inválido
        throw new IllegalArgumentException("Invalid expense category: " + label);
    }
}
