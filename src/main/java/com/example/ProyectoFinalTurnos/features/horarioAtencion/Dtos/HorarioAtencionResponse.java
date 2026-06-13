package com.example.ProyectoFinalTurnos.features.horarioAtencion.Dtos;

import com.example.ProyectoFinalTurnos.model.enums.Diasemana;
import lombok.Data;

import java.time.LocalTime;

@Data
public class HorarioAtencionResponse {
    private Long idHorarioAtencion;
    private Long comercioId; // ID del comercio asociado
    private Long servicioId; //ID del servicio asociado (será null si es el horario general del local)
    private Diasemana diaSemana;
    private LocalTime horaApertura;
    private LocalTime horaCierre;
}
