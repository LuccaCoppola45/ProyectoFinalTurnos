package com.example.ProyectoFinalTurnos.features.comercio.Excepctions.personalizated.catalog;

public class ComercioAlreadyExistsException extends RuntimeException {
    public ComercioAlreadyExistsException(String message) {
        super(message);
    }
}
