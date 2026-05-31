package com.example.ProyectoFinalTurnos.features.comercio.Excepctions.personalizated.user;

public class UserProfileNotFoundException extends RuntimeException {
    public UserProfileNotFoundException(String message) {
        super(message);
    }
}
