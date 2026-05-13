package com.example.ProyectoFinalTurnos.features.servicio.Dtos;

import lombok.Data;

@Data
public class ServicioResponse {
    private Long idServicio;
    private String nombreServicio;
    private Double precio;
    private Long idComercio;
    private Long idHorario;
}
