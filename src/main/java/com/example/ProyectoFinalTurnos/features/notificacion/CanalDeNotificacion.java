package com.example.ProyectoFinalTurnos.features.notificacion;

import org.thymeleaf.context.Context;

public interface CanalDeNotificacion {
    void enviar(String destinatario, String asunto, String nombreTemplate, Context contexto);
}
