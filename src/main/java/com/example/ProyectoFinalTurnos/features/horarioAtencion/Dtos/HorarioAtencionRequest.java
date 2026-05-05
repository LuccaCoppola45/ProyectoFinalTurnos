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
    @Min(value = 1, message = "El dia debe ser entre 1(lunes) y 7 (domingo)")
    @Max(value = 7, message = "El dia debe ser entre 1(lunes) y 7 (domingo)")
    private Diasemana dia;

    @NotNull(message = "La hora de apertura es obligatoria")
    private LocalTime apertura;

    @NotNull(message = "La hora de cierre es obligatoria")
    private LocalTime cierre;

    @NotNull(message = "El id del comercio es obligatorio")
    private Long idComercio;



}
