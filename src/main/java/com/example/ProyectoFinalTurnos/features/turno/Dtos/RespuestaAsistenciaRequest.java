package com.example.ProyectoFinalTurnos.features.turno.Dtos;

import jakarta.validation.constraints.NotNull;

public record RespuestaAsistenciaRequest(
        @NotNull(message = "Indicá si vas a ir o no")
        Boolean asiste
) {
}
