package com.example.ProyectoFinalTurnos.features.horarioAtencion.Dtos;

import com.example.ProyectoFinalTurnos.model.enums.Diasemana;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalTime;

@Data
public class HorarioAtencionRequest {


    @NotNull(message = "El ID del comercio es obligatorio")
    private Long comercioIdRequest;

    // Opcional: Si viene null, estás definiendo el horario de apertura/cierre del local.
    // Si viene con un ID, es el horario de atención de ese peluquero.
    private Long servicioIdRequest;

    @NotNull(message = "El dia  es obligatorio")
    private Diasemana diaSemana;

    @NotNull(message = "La hora de apertura es obligatoria")
    private LocalTime horaApertura;

    @NotNull(message = "La hora de cierre es obligatoria")
    private LocalTime horaCierre;



}
