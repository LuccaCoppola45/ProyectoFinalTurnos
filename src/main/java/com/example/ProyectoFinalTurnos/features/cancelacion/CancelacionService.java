package com.example.ProyectoFinalTurnos.features.cancelacion;

import com.example.ProyectoFinalTurnos.features.cancelacion.dto.CancelacionRequest;
import com.example.ProyectoFinalTurnos.features.cancelacion.dto.CancelacionResponse;

import java.util.List;

public interface CancelacionService {
    //POST
    CancelacionResponse cancelarTurno(CancelacionRequest cancelacionRequest);
    //GET
    List<CancelacionResponse> listarCancelaciones();

}
