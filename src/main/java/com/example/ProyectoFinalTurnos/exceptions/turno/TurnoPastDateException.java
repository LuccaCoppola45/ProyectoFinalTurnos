package com.example.ProyectoFinalTurnos.exceptions.turno;

public class TurnoPastDateException extends RuntimeException{
    public TurnoPastDateException(String message){
        super(message);
    }
}
