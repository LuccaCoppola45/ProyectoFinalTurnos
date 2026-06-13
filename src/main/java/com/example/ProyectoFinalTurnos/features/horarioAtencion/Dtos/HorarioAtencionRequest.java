package com.example.ProyectoFinalTurnos.features.horarioAtencion.Dtos;

import com.example.ProyectoFinalTurnos.model.enums.Diasemana;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalTime;

@Data
public class HorarioAtencionRequest
{
    @NotNull(message = "El ID del comercio es obligatorio")
    @Positive(message = "El ID del comercio debe ser mayor a cero")
    private Long comercioId;

    @Positive(message = "El ID del servicio debe ser mayor a cero")
    private Long servicioId;

    @NotNull(message = "El día es obligatorio")
    private Diasemana diaSemana;

    @NotNull(message = "La hora de apertura es obligatoria")
    private LocalTime horaApertura;

    @NotNull(message = "La hora de cierre es obligatoria")
    private LocalTime horaCierre;

}