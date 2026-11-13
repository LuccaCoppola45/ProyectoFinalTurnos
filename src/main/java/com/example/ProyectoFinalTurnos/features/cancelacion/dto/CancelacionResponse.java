package com.example.ProyectoFinalTurnos.features.cancelacion.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CancelacionResponse {
    private Long idCancelacion;
    private LocalDateTime fechaCancelacion;
    // En minúscula: con "Motivo" el JSON salía como "Motivo" y el front (que espera "motivo") lo veía vacío
    private String motivo;
    private long idUsuario;
    /** Turno que se canceló (para que el admin vea de qué fecha y comercio era). */
    private Long idTurno;
}
