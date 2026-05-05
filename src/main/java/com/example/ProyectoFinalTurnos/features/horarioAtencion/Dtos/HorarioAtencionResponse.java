package com.example.ProyectoFinalTurnos.features.horarioAtencion.Dtos;

import lombok.Data;

import java.time.LocalTime;

@Data
public class HorarioAtencionResponse {
    private long id;
    private Integer DiaSemana;
    private LocalTime fechaApertura;
    private LocalTime fechaCierre;
    private Long idComercio;
}
