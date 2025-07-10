package com.Ariel_Rom.Expense_Tracker_API.models.enums;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

// Enum que representa los períodos de tiempo para filtrar gastos

@Getter // Genera getters para los campos (aunque acá no tiene campos adicionales)
@RequiredArgsConstructor // Genera constructor con campos finales (no aplica en este caso porque no hay campos)
@JsonDeserialize // Indica que Jackson puede deserializar este enum automáticamente desde JSON
public enum FilteredPeriodEnum {

    LAST_WEEK,      // Última semana
    LAST_MONTH,     // Último mes
    LAST_3_MONTHS,  // Últimos 3 meses
    CUSTOM;         // Período personalizado definido por fechas específicas

}

