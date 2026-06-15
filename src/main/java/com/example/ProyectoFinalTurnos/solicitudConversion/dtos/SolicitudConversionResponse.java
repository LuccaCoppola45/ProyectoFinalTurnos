package com.example.ProyectoFinalTurnos.solicitudConversion.dtos;

import com.example.ProyectoFinalTurnos.model.enums.EstadoSolicitud;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudConversionResponse
{
    private Long id;
    private Long usuarioId;
    private String nombreComercioPropuesto;
    private String direccion;
    private String telefono;
    private String descripcion;
    private EstadoSolicitud estado;
    private LocalDateTime fechaSolicitud;

}
