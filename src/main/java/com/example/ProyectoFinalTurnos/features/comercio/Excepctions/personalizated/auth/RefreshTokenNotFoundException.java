package com.example.ProyectoFinalTurnos.features.comercio.Excepctions.personalizated.auth;

public class RefreshTokenNotFoundException extends RuntimeException {
    public RefreshTokenNotFoundException(String message) {
        super(message);
    }
}
