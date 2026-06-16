package com.example.ProyectoFinalTurnos.exceptions.user;

public class UnauthorizedUserAccessException extends RuntimeException {
    public UnauthorizedUserAccessException(String message) {
        super(message);
    }
    public UnauthorizedUserAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
