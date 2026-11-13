package com.example.ProyectoFinalTurnos.features.turno;

import com.example.ProyectoFinalTurnos.model.enums.EstadoTurno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface TurnoRepository extends JpaRepository<TurnoEntity, Long>
{
    boolean existsByUsuario_IdUsuarioAndFechaAndHoraInicio(
            Long usuarioId,
            LocalDate fecha,
            LocalTime horaInicio);

    List<TurnoEntity> findByServicio_IdServicioAndFecha(
            Long servicioId,
            LocalDate fecha);

    List<TurnoEntity> findByUsuario_IdUsuarioAndFecha(
            Long usuarioId,
            LocalDate fecha);

    boolean existsByUsuario_IdUsuario(Long idUsuario);

    boolean existsByServicio_IdServicio(long id);

    List<TurnoEntity> findByComercio_IdComercio(Long comercioId);
    List<TurnoEntity> findByComercio_IdComercioOrderByFechaDesc(Long comercioId);

    /**
     * Pasa a FINALIZADO los turnos activos cuyo horario ya terminó
     * (fecha anterior a hoy, o de hoy con hora de fin ya pasada).
     * Devuelve cuántos turnos actualizó.
     */
    @Modifying
    @Transactional
    @Query("""
            UPDATE TurnoEntity t SET t.estado = :finalizado
            WHERE t.estado IN :activos
              AND (t.fecha < :hoy OR (t.fecha = :hoy AND t.horaFin <= :ahora))
            """)
    int finalizarVencidos(@Param("finalizado") EstadoTurno finalizado,
                          @Param("activos") List<EstadoTurno> activos,
                          @Param("hoy") LocalDate hoy,
                          @Param("ahora") LocalTime ahora);
}

