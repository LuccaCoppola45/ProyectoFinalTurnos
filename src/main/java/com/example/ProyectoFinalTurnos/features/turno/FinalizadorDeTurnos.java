package com.example.ProyectoFinalTurnos.features.turno;

import com.example.ProyectoFinalTurnos.model.enums.EstadoTurno;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Tarea automática: cada minuto pasa a FINALIZADO los turnos cuyo horario ya terminó.
 * Así un turno que ya pasó no queda "pendiente" para siempre, y no se puede
 * cancelar ni reprogramar (esas operaciones ya rechazan los turnos finalizados).
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class FinalizadorDeTurnos {

    /** Estados de un turno que todavía "está por ocurrir". */
    private static final List<EstadoTurno> ACTIVOS =
            List.of(EstadoTurno.PENDIENTE, EstadoTurno.CONFIRMADO, EstadoTurno.RESERVADO);

    private final TurnoRepository turnoRepository;

    // initialDelay: también corre a los 5 segundos de arrancar, por si el backend estuvo apagado
    @Scheduled(initialDelay = 5_000, fixedRate = 60_000)
    public void finalizarTurnosVencidos() {
        int finalizados = turnoRepository.finalizarVencidos(
                EstadoTurno.FINALIZADO, ACTIVOS, LocalDate.now(), LocalTime.now());
        if (finalizados > 0) {
            log.info("Se marcaron {} turno(s) como FINALIZADO porque ya pasó su horario", finalizados);
        }
    }
}
