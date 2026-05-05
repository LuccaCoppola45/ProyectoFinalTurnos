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
    private String mensaje;
}
