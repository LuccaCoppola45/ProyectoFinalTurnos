package com.example.ProyectoFinalTurnos.exceptions.categoria;

public class CategoriaInUseException extends RuntimeException{
    public CategoriaInUseException(String msg){
        super(msg);
    }
}
