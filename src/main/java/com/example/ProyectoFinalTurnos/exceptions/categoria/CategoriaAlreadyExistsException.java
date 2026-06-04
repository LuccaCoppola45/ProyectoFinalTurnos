package com.example.ProyectoFinalTurnos.exceptions.categoria;

public class CategoriaAlreadyExistsException extends RuntimeException {
    public CategoriaAlreadyExistsException(String message) {
        super(message);
    }
}
