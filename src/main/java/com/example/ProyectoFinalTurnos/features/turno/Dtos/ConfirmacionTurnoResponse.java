package com.example.ProyectoFinalTurnos.features.turno.Dtos;

import com.example.ProyectoFinalTurnos.model.enums.EstadoTurno;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Lo que ve el cliente en la página "¿Vas a ir?" (se abre desde el mail, sin iniciar sesión).
 * Solo datos del propio turno: nada de ids ni datos de otros usuarios.
 */
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
