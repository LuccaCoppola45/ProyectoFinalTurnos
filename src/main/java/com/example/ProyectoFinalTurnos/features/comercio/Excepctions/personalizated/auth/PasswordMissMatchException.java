package com.example.ProyectoFinalTurnos.features.comercio.Excepctions.personalizated.auth;

public class PasswordMissMatchException extends RuntimeException {
    public PasswordMissMatchException(String message) {
        super(message);
    }
}
