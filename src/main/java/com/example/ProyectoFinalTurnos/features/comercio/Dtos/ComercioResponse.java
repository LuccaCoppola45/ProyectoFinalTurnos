package com.example.ProyectoFinalTurnos.features.comercio.Dtos;

import lombok.Data;

import java.util.List;


@Data

public class ComercioResponse {
    private long id;
    private String nombre;
    private String direccion;
    private int telefono;
    private List<String> categorias;



}
