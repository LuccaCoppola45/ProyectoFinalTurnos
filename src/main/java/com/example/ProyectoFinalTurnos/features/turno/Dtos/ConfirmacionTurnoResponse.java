package com.example.ProyectoFinalTurnos.features.turno.Dtos;

import com.example.ProyectoFinalTurnos.model.enums.EstadoTurno;

import java.time.LocalDate;
import java.time.LocalTime;

public record ConfirmacionTurnoResponse(
        String nombreCliente,
        String nombreComercio,
        String direccionComercio,
        String nombreServicio,
        LocalDate fecha,
        LocalTime horaInicio,
        EstadoTurno estado
) {
}
