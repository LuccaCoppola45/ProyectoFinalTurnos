package com.example.ProyectoFinalTurnos.exceptions.categoria;

public class CategoriaNoEncontradaException extends RuntimeException{
    public CategoriaNoEncontradaException(String mensaje){
        super(mensaje);
    }
}
