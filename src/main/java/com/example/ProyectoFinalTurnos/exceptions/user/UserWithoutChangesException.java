package com.example.ProyectoFinalTurnos.exceptions.user;

public class UserWithoutChangesException extends RuntimeException {
    public UserWithoutChangesException(String message) {
        super(message);
    }
}
