package com.example.ProyectoFinalTurnos.features.cancelacion.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CancelacionRequest {

    @NotNull(message = "El ID del turno es obligatorio")
    @Positive(message = "El ID del turno debe ser un número positivo mayor a 0")
    private Long idTurno;

    @NotNull(message = "El ID del usuario es obligatorio")
    @Positive(message = "El ID del usuario debe ser un número positivo mayor a 0")
    private Long idUsuario;

    @NotBlank(message = "El motivo de la cancelacion no puede estar vacio")
    @Size(min = 10, max = 500, message = "El motivo debe tener entre 10 y 500 caracteres")
    private String motivo;

}
