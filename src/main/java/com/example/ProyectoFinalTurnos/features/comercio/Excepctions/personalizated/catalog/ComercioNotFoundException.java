package com.example.ProyectoFinalTurnos.features.comercio.Excepctions.personalizated.catalog;

public class ComercioNotFoundException extends RuntimeException {
    public ComercioNotFoundException(String message) {
        super(message);
    }
}
