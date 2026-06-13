package com.example.ProyectoFinalTurnos.features.servicio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicioRepository  extends JpaRepository<ServicioEntity,Long> {

    //Devuelve true si existe un servicio con el nombre dado
    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END " +
            "FROM ServicioEntity s " +
            "WHERE s.nombre = :nombre " +
            "AND s.comercio.id = :idComercio")
    boolean existsByNombreServicioAndIdComercio(@Param("nombre") String nombre,
                                                @Param("idComercio") Long idComercio);
}