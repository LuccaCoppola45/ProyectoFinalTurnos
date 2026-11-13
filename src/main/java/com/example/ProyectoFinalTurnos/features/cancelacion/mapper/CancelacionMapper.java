package com.example.ProyectoFinalTurnos.features.cancelacion.mapper;

import com.example.ProyectoFinalTurnos.features.cancelacion.CancelacionEntity;
import com.example.ProyectoFinalTurnos.features.cancelacion.dto.CancelacionRequest;
import com.example.ProyectoFinalTurnos.features.cancelacion.dto.CancelacionResponse;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class CancelacionMapper {

    private final ModelMapper modelMapper = new ModelMapper();

    public CancelacionEntity requestDtoToEntity(CancelacionRequest requestDto) {
        if (requestDto == null) return null;
        CancelacionEntity entity = new CancelacionEntity();
        entity.setMotivo(requestDto.getMotivo());

        return entity;
    }

    public CancelacionResponse entityToResponseDto(CancelacionEntity entity) {
        if (entity == null) return null;
        CancelacionResponse dto = modelMapper.map(entity, CancelacionResponse.class);
        // Se cargan a mano para no depender de que ModelMapper adivine los nombres
        dto.setIdCancelacion(entity.getId());
        if (entity.getTurno() != null) {
            dto.setIdTurno(entity.getTurno().getIdTurno());
        }
        return dto;
    }
}


