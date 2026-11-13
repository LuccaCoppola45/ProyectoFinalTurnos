package com.example.ProyectoFinalTurnos.features.turno;

import com.example.ProyectoFinalTurnos.exceptions.turno.TurnFinishedException;
import com.example.ProyectoFinalTurnos.exceptions.turno.TurnoAlreadyCancelledException;
import com.example.ProyectoFinalTurnos.exceptions.turno.TurnoNotFoundException;
import com.example.ProyectoFinalTurnos.exceptions.turno.TurnoPastDateException;
import com.example.ProyectoFinalTurnos.features.cancelacion.CancelacionEntity;
import com.example.ProyectoFinalTurnos.features.cancelacion.CancelacionRepository;
import com.example.ProyectoFinalTurnos.features.notificacion.TurnoCanceladoEvent;
import com.example.ProyectoFinalTurnos.features.turno.Dtos.ConfirmacionTurnoResponse;
import com.example.ProyectoFinalTurnos.model.enums.EstadoTurno;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * El cliente responde al recordatorio de 24 h desde el link del mail (sin iniciar sesión).
 * El "código" (token) del link es lo que identifica al turno: es un UUID imposible de adivinar.
 */
@Service
@RequiredArgsConstructor
public class ConfirmacionTurnoService {

    static final String MOTIVO_NO_ASISTE = "El cliente avisó desde el recordatorio que no puede asistir.";

    private final TurnoRepository turnoRepository;
    private final CancelacionRepository cancelacionRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional(readOnly = true)
    public ConfirmacionTurnoResponse obtener(String token) {
        return aRespuesta(buscar(token));
    }

    @Transactional
    public ConfirmacionTurnoResponse responder(String token, boolean asiste) {
        TurnoEntity turno = buscar(token);

        if (turno.getEstado() == EstadoTurno.CANCELADO) {
            throw new TurnoAlreadyCancelledException("Este turno ya está cancelado.");
        }
        if (turno.getEstado() == EstadoTurno.FINALIZADO) {
            throw new TurnFinishedException("Este turno ya finalizó.");
        }
        if (LocalDateTime.of(turno.getFecha(), turno.getHoraInicio()).isBefore(LocalDateTime.now())) {
            throw new TurnoPastDateException("El horario de este turno ya pasó.");
        }

        if (asiste) {
            turno.setEstado(EstadoTurno.CONFIRMADO);
            turnoRepository.save(turno);
        } else {
            cancelar(turno);
        }
        return aRespuesta(turno);
    }

    /** Igual que una cancelación hecha desde la app: queda registrada y le llega el mail al cliente. */
    private void cancelar(TurnoEntity turno) {
        CancelacionEntity cancelacion = new CancelacionEntity();
        cancelacion.setUsuario(turno.getUsuario());
        cancelacion.setTurno(turno);
        cancelacion.setMotivo(MOTIVO_NO_ASISTE);
        cancelacion.setFechaCancelacion(LocalDateTime.now());
        cancelacionRepository.save(cancelacion);

        turno.setEstado(EstadoTurno.CANCELADO);
        turnoRepository.save(turno);

        eventPublisher.publishEvent(new TurnoCanceladoEvent(
                turno.getIdTurno(),
                turno.getUsuario().getEmail(),
                turno.getUsuario().getNombre(),
                turno.getServicio().getNombre(),
                turno.getComercio().getNombre(),
                turno.getFecha(),
                turno.getHoraInicio(),
                MOTIVO_NO_ASISTE,
                false
        ));
    }

    private TurnoEntity buscar(String token) {
        return turnoRepository.findByTokenConfirmacion(token)
                .orElseThrow(() -> new TurnoNotFoundException("El link no es válido o el turno ya no existe."));
    }

    private ConfirmacionTurnoResponse aRespuesta(TurnoEntity turno) {
        return new ConfirmacionTurnoResponse(
                turno.getUsuario().getNombre(),
                turno.getComercio().getNombre(),
                turno.getComercio().getDireccion(),
                turno.getServicio().getNombre(),
                turno.getFecha(),
                turno.getHoraInicio(),
                turno.getEstado()
        );
    }
}
