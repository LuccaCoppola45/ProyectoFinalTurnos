package com.example.ProyectoFinalTurnos.features.horarioAtencion.Dtos;

import com.example.ProyectoFinalTurnos.model.enums.Diasemana;
import lombok.Data;

import java.time.LocalTime;

@Data
public class HorarioAtencionResponse {
    private Long id;
    private Diasemana diaSemana;
    private LocalTime horaApertura;
    private LocalTime horaCierre;
    private Long comercioIdResponse;
}
