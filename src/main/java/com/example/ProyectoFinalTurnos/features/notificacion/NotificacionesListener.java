package com.example.ProyectoFinalTurnos.features.notificacion;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.thymeleaf.context.Context;

@Component
@RequiredArgsConstructor
public class NotificacionesListener {

    private final CanalDeNotificacion canal;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void alCrearTurno(TurnoCreadoEvent evento) {
        Context contexto = new Context();
        contexto.setVariable("turnoId", evento.turnoId());
        contexto.setVariable("nombreUsuario", evento.nombreUsuario());
        contexto.setVariable("nombreServicio", evento.nombreServicio());
        contexto.setVariable("nombreComercio", evento.nombreComercio());
        contexto.setVariable("fecha", evento.fecha());
        contexto.setVariable("hora", evento.hora());
        canal.enviar(evento.destinatario(), "Confirmación de turno", "email/turno-creado", contexto);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void alCancelarTurno(TurnoCanceladoEvent evento) {
        Context contexto = new Context();
        contexto.setVariable("turnoId", evento.turnoId());
        contexto.setVariable("nombreUsuario", evento.nombreUsuario());
        contexto.setVariable("nombreServicio", evento.nombreServicio());
        contexto.setVariable("nombreComercio", evento.nombreComercio());
        contexto.setVariable("fecha", evento.fecha());
        contexto.setVariable("hora", evento.hora());
        contexto.setVariable("motivo", evento.motivo());
        canal.enviar(evento.destinatario(), "Cancelación de turno", "email/turno-cancelado", contexto);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void alResolverSolicitud(SolicitudResueltaEvent evento) {
        Context contexto = new Context();
        contexto.setVariable("solicitudId", evento.solicitudId());
        contexto.setVariable("nombreUsuario", evento.nombreUsuario());
        contexto.setVariable("nombreComercio", evento.nombreComercio());
        contexto.setVariable("estado", evento.estado());
        contexto.setVariable("motivo", evento.motivo());
        canal.enviar(
                evento.destinatario(),
                "Resolución de solicitud de conversión",
                "email/solicitud-resuelta",
                contexto
        );
    }
}
