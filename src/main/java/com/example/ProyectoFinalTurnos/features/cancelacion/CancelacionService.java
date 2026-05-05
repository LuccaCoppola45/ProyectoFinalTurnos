package com.example.ProyectoFinalTurnos.features.cancelacion;

import com.example.ProyectoFinalTurnos.features.cancelacion.dto.CancelacionRequest;
import com.example.ProyectoFinalTurnos.features.cancelacion.dto.CancelacionResponse;

public interface CancelacionService {
    CancelacionResponse cancelarTurno(CancelacionRequest cancelacionRequest);
}
