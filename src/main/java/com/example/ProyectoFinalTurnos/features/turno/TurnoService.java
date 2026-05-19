package com.example.ProyectoFinalTurnos.features.turno;

import com.example.ProyectoFinalTurnos.features.turno.Dtos.TurnoRequestDto;
import com.example.ProyectoFinalTurnos.features.turno.Dtos.TurnoResponceDto;

import java.util.List;

public interface TurnoService
{
    public TurnoResponceDto crearTurno(TurnoRequestDto dto);
    public List<TurnoResponceDto> obtenerTodos ();
    public TurnoResponceDto obtenerPorId (Long id);

}
