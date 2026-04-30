package com.example.ProyectoFinalTurnos.features.comercio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComercioRepository extends JpaRepository<ComercioEntity, Long> {
}
