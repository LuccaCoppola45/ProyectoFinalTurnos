package com.example.ProyectoFinalTurnos.features.servicio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServicioRepository  extends JpaRepository<ServicioEntity,Long> {

    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END " +
            "FROM ServicioEntity s " +
            "WHERE s.nombre = :nombre " +
            "AND s.comercio.id = :idComercio")
    boolean existsByNombreServicioAndIdComercio(@Param("nombre") String nombre,
                                                @Param("idComercio") Long idComercio);

    List<ServicioEntity> findByComercio_IdComercio(Long comercioId);

    List<ServicioEntity> findByNombreContainingIgnoreCase(String nombre);

    @Query("SELECT s FROM ServicioEntity s WHERE s.comercio.id = :comercioId AND LOWER(s.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<ServicioEntity> findByComercioAndNombre(@Param("comercioId") Long comercioId, @Param("nombre") String nombre);
}