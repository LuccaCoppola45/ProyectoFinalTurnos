package com.example.ProyectoFinalTurnos.exceptions.turno;

public class TurnoAlreadyCancelledException extends RuntimeException {
    public TurnoAlreadyCancelledException(String message) {
        super(message);
    }
}
