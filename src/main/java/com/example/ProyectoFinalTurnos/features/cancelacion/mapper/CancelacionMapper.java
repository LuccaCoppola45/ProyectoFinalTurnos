package com.example.ProyectoFinalTurnos.features.cancelacion.mapper;

import com.example.ProyectoFinalTurnos.features.cancelacion.CancelacionEntity;
import com.example.ProyectoFinalTurnos.features.cancelacion.dto.CancelacionRequest;
import com.example.ProyectoFinalTurnos.features.cancelacion.dto.CancelacionResponse;
import org.springframework.stereotype.Component;

@Component
public class CancelacionMapper {
    public CancelacionEntity requestDtotoEntity(CancelacionRequest requestDto) {
        if (requestDto == null) {
            return null;
        }
        CancelacionEntity cancelacionEntity = new CancelacionEntity();
        cancelacionEntity.setMotivo(requestDto.getMotivo());

        return cancelacionEntity;
    }

    public CancelacionResponse entitytoResponseDto(CancelacionEntity entityCancelacion, String mensajePersonalizado) {
        if (entityCancelacion == null) {
            return null;
        }
        CancelacionResponse responseDTO = new CancelacionResponse();
        responseDTO.setIdCancelacion(entityCancelacion.getId());
        responseDTO.setFechaCancelacion(entityCancelacion.getFechaCancelacion());
        responseDTO.setMensaje(mensajePersonalizado);

        return responseDTO;
    }

}
