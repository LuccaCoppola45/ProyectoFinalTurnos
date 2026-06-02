package com.example.ProyectoFinalTurnos.features.horarioAtencion;

import com.example.ProyectoFinalTurnos.model.enums.Diasemana;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HorarioAtencionRepository extends JpaRepository<HorarioAtencionEntity, Long> {
    List<HorarioAtencionEntity> findByComercioIdAndDiaSemana(Long comercioId, Diasemana diaSemana);
}
