package com.example.ProyectoFinalTurnos.features.comercio.Excepctions.personalizated.auth;

public class VerificationTokenInvalidException extends RuntimeException {
    public VerificationTokenInvalidException(String message) {
        super(message);
    }
}
