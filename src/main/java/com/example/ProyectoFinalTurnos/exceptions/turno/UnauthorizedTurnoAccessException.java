package com.example.ProyectoFinalTurnos.exceptions.turno;

public class UnauthorizedTurnoAccessException extends RuntimeException {
    public UnauthorizedTurnoAccessException(String message) {
        super(message);
    }
}
