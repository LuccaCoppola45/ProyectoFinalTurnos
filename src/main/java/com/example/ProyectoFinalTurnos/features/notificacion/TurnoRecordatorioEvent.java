package com.example.ProyectoFinalTurnos.features.notificacion;

import java.time.LocalDate;
import java.time.LocalTime;

/** Se publica cuando falta ~1 día para el turno: el cliente recibe el mail "¿Vas a ir?". */
public record TurnoRecordatorioEvent(
        Long turnoId,
        String destinatario,
        String nombreUsuario,
        String nombreServicio,
        String nombreComercio,
        LocalDate fecha,
        LocalTime hora,
        /* código único del turno para responder desde el mail sin iniciar sesión */
        String tokenConfirmacion
) {
}
