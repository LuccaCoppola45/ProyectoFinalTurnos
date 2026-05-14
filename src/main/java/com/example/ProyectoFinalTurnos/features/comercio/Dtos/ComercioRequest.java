package com.example.ProyectoFinalTurnos.features.comercio.Dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;

import java.util.List;

public class ComercioRequest {

    @NotBlank(message = "El nombre del comercio no puede estar vacío")
    private String nombre;

    @NotBlank(message = "La dirección del comercio no puede estar vacía")
    private String direccion;
    @Positive(message = "El teléfono debe ser un número positivo")
    private int telefono;
    @NotEmpty(message = "Debe seleccionar al menos una categoría para el comercio")
    private List<Long> idCategorias;

}
