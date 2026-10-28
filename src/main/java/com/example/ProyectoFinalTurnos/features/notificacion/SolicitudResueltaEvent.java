package com.example.ProyectoFinalTurnos.features.notificacion;

public record SolicitudResueltaEvent(
        Long solicitudId,
        String destinatario,
        String nombreUsuario,
        String nombreComercio,
        String estado,
        String motivo
) {
}
