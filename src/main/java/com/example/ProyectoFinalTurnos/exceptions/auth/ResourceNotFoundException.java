package com.example.ProyectoFinalTurnos.exceptions.auth;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
