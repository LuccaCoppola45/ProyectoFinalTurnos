package com.example.ProyectoFinalTurnos.features.turno;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;

@Repository
public interface TurnoRepository extends JpaRepository<TurnoEntity,Long>
{

    boolean existsByUsuario_IdUsuario(Long idUsuario);
    boolean existsByUsuario_IdUsuarioAndFechaAndHoraInicio(
            Long usuarioId,
            LocalDate fecha,
            LocalTime horaInicio
    );


}
