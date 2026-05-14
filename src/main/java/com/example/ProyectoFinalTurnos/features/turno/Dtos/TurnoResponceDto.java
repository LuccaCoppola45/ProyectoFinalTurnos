package com.example.ProyectoFinalTurnos.features.turno.Dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TurnoResponceDto
{
    private Long idTurno;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private String estado;
    private Long usuarioId;
    private Long servicioId;
    private Long cancelacionId;
}
