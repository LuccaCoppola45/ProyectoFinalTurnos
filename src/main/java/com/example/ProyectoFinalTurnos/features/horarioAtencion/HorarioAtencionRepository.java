package com.example.ProyectoFinalTurnos.features.horarioAtencion;

import com.example.ProyectoFinalTurnos.features.horarioAtencion.Dtos.HorarioAtencionResponse;
import com.example.ProyectoFinalTurnos.model.enums.Diasemana;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface HorarioAtencionRepository extends JpaRepository<HorarioAtencionEntity, Long> {
    List<HorarioAtencionEntity> findByComercio_IdComercioAndDiaSemana(Long comercioId, Diasemana diaSemana);
//    HorarioAtencionResponse findByIdHorarioAtencion(Long id);
//    List<HorarioAtencionEntity> findByComercio_IdComercio(Long comercioId);
//    List<HorarioAtencionEntity> findByServicio_IdServicio(Long servicioId);
//    boolean existsByComercio_IdComercioAndDiaSemanaAndHoraAperturaAndHoraCierre(Long comercioId, Diasemana diaSemana, LocalTime horaApertura, LocalTime horaCierre);


}
