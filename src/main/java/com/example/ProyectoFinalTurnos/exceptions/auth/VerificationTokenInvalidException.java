package com.example.ProyectoFinalTurnos.exceptions.auth;

public class VerificationTokenInvalidException extends RuntimeException {
    public VerificationTokenInvalidException(String message) {
        super(message);
    }
}
