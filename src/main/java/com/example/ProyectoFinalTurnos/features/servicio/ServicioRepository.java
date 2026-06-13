package com.example.ProyectoFinalTurnos.features.servicio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicioRepository  extends JpaRepository<ServicioEntity,Long> {

    //Devuelve true si existe un servicio con el nombre dado
    boolean existsByNombreServicio(String nombreServicio,Long comercioId);
}
