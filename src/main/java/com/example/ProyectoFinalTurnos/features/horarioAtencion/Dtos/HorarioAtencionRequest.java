package com.example.ProyectoFinalTurnos.features.horarioAtencion.Dtos;

import com.example.ProyectoFinalTurnos.model.enums.Diasemana;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
@Data
public class HorarioAtencionRequest {

    @NotNull(message = "El dia  es obligatorio")
    private Diasemana dia;

    @NotNull(message = "La hora de apertura es obligatoria")
    private LocalTime apertura;

    @NotNull(message = "La hora de cierre es obligatoria")
    private LocalTime cierre;

    @NotNull(message = "El id del comercio es obligatorio")
    private Long comercioIdRequest;



}
