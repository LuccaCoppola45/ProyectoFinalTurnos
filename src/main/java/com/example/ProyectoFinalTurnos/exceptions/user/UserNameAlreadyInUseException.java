package com.example.ProyectoFinalTurnos.exceptions.user;

public class UserNameAlreadyInUseException extends RuntimeException {
    public UserNameAlreadyInUseException(String message) {
        super(message);
    }
}
