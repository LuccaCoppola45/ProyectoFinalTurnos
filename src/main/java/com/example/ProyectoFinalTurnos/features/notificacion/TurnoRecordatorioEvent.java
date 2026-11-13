package com.example.ProyectoFinalTurnos.features.notificacion;

import java.time.LocalDate;
import java.time.LocalTime;

public record TurnoRecordatorioEvent(
        Long turnoId,
        String destinatario,
        String nombreUsuario,
        String nombreServicio,
        String nombreComercio,
        LocalDate fecha,
        LocalTime hora,
        String tokenConfirmacion
) {
}
