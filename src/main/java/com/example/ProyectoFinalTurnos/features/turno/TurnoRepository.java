package com.example.ProyectoFinalTurnos.features.turno;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;

@Repository
public interface TurnoRepository extends JpaRepository<TurnoEntity,Long>
{

    boolean existsByUsuario_IdUsuario(Long idUsuario);
    boolean existsByUsuario_IdUsuarioAndFechaAndHoraInicio(Long usuarioId, LocalDate fecha, LocalTime horaInicio);

    // Validamos que haya una superposición.
// La lógica de superposición es: (NuevoInicio < FinExistente) Y (NuevoFin > InicioExistente)
// Además excluimos los turnos que hayan sido CANCELADOS.
    @Query("SELECT COUNT(t) > 0 FROM TurnoEntity t " +
            "WHERE t.servicio.idServicio = :idServicio " +
            "AND t.fecha = :fecha " +
            "AND t.estado != 'CANCELADO' " +
            "AND t.horaInicio < :horaFin " +
            "AND t.horaFin > :horaInicio")
    boolean existsSuperposicion(@Param("idServicio") Long idServicio,
                                @Param("fecha") LocalDate fecha,
                                @Param("horaInicio") LocalTime horaInicio,
                                @Param("horaFin") LocalTime horaFin);
}


