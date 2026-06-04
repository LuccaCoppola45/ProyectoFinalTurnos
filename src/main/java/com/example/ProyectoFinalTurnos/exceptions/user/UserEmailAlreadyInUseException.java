package com.example.ProyectoFinalTurnos.exceptions.user;

public class UserEmailAlreadyInUseException extends RuntimeException {
    public UserEmailAlreadyInUseException(String message) {
        super(message);
    }
}
