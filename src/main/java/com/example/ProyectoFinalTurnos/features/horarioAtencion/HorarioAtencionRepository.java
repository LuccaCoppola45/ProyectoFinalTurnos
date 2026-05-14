package com.example.ProyectoFinalTurnos.features.horarioAtencion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HorarioAtencionRepository extends JpaRepository<HorarioAtencionEntity, Long> {
}
