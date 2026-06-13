package com.example.ProyectoFinalTurnos.features.servicio.Dtos;

import lombok.Data;

import java.util.List;

@Data
public class ServicioResponse
{
    private Long idServicio;
    private String nombreServicio;
    private Integer duracionDelServicio;
    private double precio;
    private Long idComercio;
    private List<Long> horariosAtencionIds;
}
