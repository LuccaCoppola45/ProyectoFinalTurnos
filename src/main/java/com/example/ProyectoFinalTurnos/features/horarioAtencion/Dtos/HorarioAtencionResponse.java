package com.example.ProyectoFinalTurnos.features.horarioAtencion.Dtos;

import com.example.ProyectoFinalTurnos.model.enums.Diasemana;
import lombok.Data;

import java.time.LocalTime;

@Data
public class HorarioAtencionResponse {
    private long id;
    private Diasemana DiaSemana;
    private LocalTime fechaApertura;
    private LocalTime fechaCierre;
    private Long comercioIdResponse;
}
