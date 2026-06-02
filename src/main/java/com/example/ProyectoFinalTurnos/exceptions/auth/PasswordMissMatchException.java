package com.example.ProyectoFinalTurnos.exceptions.auth;

public class PasswordMissMatchException extends RuntimeException {
    public PasswordMissMatchException(String message) {
        super(message);
    }
}
