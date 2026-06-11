package com.example.ProyectoFinalTurnos.exceptions.turno;

public class TurnoOutsideBusinessException extends RuntimeException
{
    public TurnoOutsideBusinessException(String m)
    {
        super (m);
    }

}
