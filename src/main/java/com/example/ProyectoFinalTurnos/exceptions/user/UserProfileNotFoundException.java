package com.example.ProyectoFinalTurnos.exceptions.user;

public class UserProfileNotFoundException extends RuntimeException {
    public UserProfileNotFoundException(String message) {
        super(message);
    }
}
