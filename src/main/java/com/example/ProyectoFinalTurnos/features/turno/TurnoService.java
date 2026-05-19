package com.example.ProyectoFinalTurnos.features.turno;

import com.example.ProyectoFinalTurnos.features.turno.Dtos.TurnoRequestDto;
import com.example.ProyectoFinalTurnos.features.turno.Dtos.TurnoResponceDto;

public interface TurnoService
{
    public TurnoResponceDto crearTurno(TurnoRequestDto dto);

}
