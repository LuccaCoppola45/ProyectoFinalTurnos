package com.example.ProyectoFinalTurnos.features.comercio.Dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ComercioRequest {

    @NotBlank(message = "El nombre del comercio no puede estar vacío")
    private String nombre;

    @NotBlank(message = "La dirección del comercio no puede estar vacía")
    private String direccion;
    @NotNull(message = "El teléfono no puede ser nulo") // <-- Ahora podés usar esta validación
    @Positive(message = "El teléfono debe ser un número positivo")
    private Integer telefono;
    @NotEmpty(message = "Debe seleccionar al menos una categoría para el comercio")
    private List<Long> idCategorias;

}
