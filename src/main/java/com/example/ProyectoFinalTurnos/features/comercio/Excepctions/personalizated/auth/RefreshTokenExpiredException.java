package com.example.ProyectoFinalTurnos.features.comercio.Excepctions.personalizated.auth;

public class RefreshTokenExpiredException extends RuntimeException {
    public RefreshTokenExpiredException(String message) {
        super(message);
    }
}
