package com.example.ProyectoFinalTurnos.features.comercio.Excepctions.personalizated.auth;

public class WrongCredentialsException extends RuntimeException {
    public WrongCredentialsException(String message) {
        super(message);
    }
}
