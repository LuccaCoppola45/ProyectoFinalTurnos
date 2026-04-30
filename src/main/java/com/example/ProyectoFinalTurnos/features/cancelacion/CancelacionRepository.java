package com.example.ProyectoFinalTurnos.features.cancelacion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CancelacionRepository extends JpaRepository<CancelacionEntity, Long> {
}
