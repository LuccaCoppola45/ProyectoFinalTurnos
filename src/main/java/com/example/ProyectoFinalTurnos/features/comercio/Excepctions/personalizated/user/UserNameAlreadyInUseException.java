package com.example.ProyectoFinalTurnos.features.comercio.Excepctions.personalizated.user;

public class UserNameAlreadyInUseException extends RuntimeException {
    public UserNameAlreadyInUseException(String message) {
        super(message);
    }
}
