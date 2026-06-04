package com.example.ProyectoFinalTurnos.exceptions.turno;

public class TurnoNotFoundException extends RuntimeException{
    public TurnoNotFoundException(String message){
        super(message);
    }
}
