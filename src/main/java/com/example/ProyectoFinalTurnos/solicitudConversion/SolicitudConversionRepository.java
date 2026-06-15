package com.example.ProyectoFinalTurnos.solicitudConversion;

import com.example.ProyectoFinalTurnos.model.enums.EstadoSolicitud;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SolicitudConversionRepository extends JpaRepository<SolicitudConversionEntity,Long>
{
    List<SolicitudConversionEntity> findByEstado(EstadoSolicitud estado);
}
