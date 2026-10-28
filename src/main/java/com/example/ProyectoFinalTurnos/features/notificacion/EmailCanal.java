package com.example.ProyectoFinalTurnos.features.notificacion;

import com.example.ProyectoFinalTurnos.features.turno.NotificacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;

@Component
@RequiredArgsConstructor
public class EmailCanal implements CanalDeNotificacion {

    private final NotificacionService notificacionService;

    @Override
    public void enviar(String destinatario, String asunto, String nombreTemplate, Context contexto) {
        notificacionService.enviarEmailConPlantilla(destinatario, asunto, nombreTemplate, contexto);
    }
}
