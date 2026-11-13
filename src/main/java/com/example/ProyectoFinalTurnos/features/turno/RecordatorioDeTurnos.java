package com.example.ProyectoFinalTurnos.features.turno;

import com.example.ProyectoFinalTurnos.features.notificacion.TurnoRecordatorioEvent;
import com.example.ProyectoFinalTurnos.model.enums.EstadoTurno;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class RecordatorioDeTurnos {

    private static final List<EstadoTurno> SIN_CONFIRMAR = List.of(EstadoTurno.PENDIENTE, EstadoTurno.RESERVADO);

    private final TurnoRepository turnoRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Scheduled(initialDelay = 10_000, fixedRate = 600_000)
    @Transactional
    public void enviarRecordatorios() {
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime desde = ahora.plusHours(1);
        LocalDateTime hasta = ahora.plusHours(24);

        List<TurnoEntity> candidatos = turnoRepository.findByEstadoInAndFechaBetween(
                SIN_CONFIRMAR, LocalDate.now(), hasta.toLocalDate());

        int enviados = 0;
        for (TurnoEntity turno : candidatos) {
            LocalDateTime inicio = LocalDateTime.of(turno.getFecha(), turno.getHoraInicio());
            boolean yaSeMando = Boolean.TRUE.equals(turno.getRecordatorioEnviado());
            if (yaSeMando || inicio.isBefore(desde) || inicio.isAfter(hasta)) {
                continue;
            }

            turno.setTokenConfirmacion(UUID.randomUUID().toString());
            turno.setRecordatorioEnviado(true);
            turnoRepository.save(turno);

            eventPublisher.publishEvent(new TurnoRecordatorioEvent(
                    turno.getIdTurno(),
                    turno.getUsuario().getEmail(),
                    turno.getUsuario().getNombre(),
                    turno.getServicio().getNombre(),
                    turno.getComercio().getNombre(),
                    turno.getFecha(),
                    turno.getHoraInicio(),
                    turno.getTokenConfirmacion()
            ));
            enviados++;
        }
        if (enviados > 0) {
            log.info("Se mandaron {} recordatorio(s) de turno para confirmar asistencia", enviados);
        }
    }
}
