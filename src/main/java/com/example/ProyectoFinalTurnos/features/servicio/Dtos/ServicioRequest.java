package com.example.ProyectoFinalTurnos.features.servicio.Dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ServicioRequest {

    @NotBlank(message = "El nombre del servicio no puede estar vacio")
    private String nombreServicio;

    private Long idComercio;
    private Long idHorario;
}
