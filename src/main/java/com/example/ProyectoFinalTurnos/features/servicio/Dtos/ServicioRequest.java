package com.example.ProyectoFinalTurnos.features.servicio.Dtos;

import lombok.Data;

@Data
public class ServicioRequest {
    private String nombreServicio;
    private Double precio;
    private Long idComercio;
    private Long idHorario;
}
