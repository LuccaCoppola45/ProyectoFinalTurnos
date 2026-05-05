package com.example.ProyectoFinalTurnos.features.cancelacion.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CancelacionRequest {

    @NotNull(message = "El ID del turno es obligatorio")
    private Long idTurno;

    @NotNull(message = "El ID del usuario es obligatorio")
    private Long idUsuario;

    @NotBlank(message = "El motivo de la cancelacion no puede estar vacio")
    @Size(max = 500, message = "El motivo no puede superar los 500 caracteres")
    private String motivo;

}
