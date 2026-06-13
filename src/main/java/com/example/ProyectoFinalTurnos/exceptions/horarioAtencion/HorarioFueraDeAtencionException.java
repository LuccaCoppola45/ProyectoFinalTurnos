package com.example.ProyectoFinalTurnos.exceptions.horarioAtencion;

public class HorarioFueraDeAtencionException extends RuntimeException
{
    public HorarioFueraDeAtencionException(String message) {
        super(message);
    }
}
