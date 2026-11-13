package com.example.ProyectoFinalTurnos.features.turno.Dtos;

import jakarta.validation.constraints.NotNull;

/** Respuesta del cliente al recordatorio: true = va a ir, false = no puede ir (se cancela). */
public record RespuestaAsistenciaRequest(
        @NotNull(message = "Indicá si vas a ir o no")
        Boolean asiste
) {
}
