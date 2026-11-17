package com.example.ProyectoFinalTurnos.features.notificacion;

public record RecuperarPasswordEvent(
        String destinatario,
        String nombreUsuario,
        String token,
        int minutosDeValidez
) {
}
