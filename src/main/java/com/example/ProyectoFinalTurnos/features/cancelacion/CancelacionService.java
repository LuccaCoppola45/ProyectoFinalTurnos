package com.example.ProyectoFinalTurnos.features.cancelacion;

import com.example.ProyectoFinalTurnos.features.cancelacion.dto.CancelacionRequest;
import com.example.ProyectoFinalTurnos.features.cancelacion.dto.CancelacionResponse;

import java.util.List;

public interface CancelacionService {
    CancelacionResponse cancelarTurno(CancelacionRequest cancelacionRequest);
    List<CancelacionResponse> listarCancelaciones();
    CancelacionResponse obtenerCancelacionPorId(Long id);

}
