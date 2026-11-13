package com.example.ProyectoFinalTurnos.features.turno.Mapper;

import com.example.ProyectoFinalTurnos.features.turno.TurnoEntity;
import com.example.ProyectoFinalTurnos.features.turno.Dtos.TurnoRequestDto;
import com.example.ProyectoFinalTurnos.features.turno.Dtos.TurnoResponceDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TurnoMapper {

    private final ModelMapper modelMapper;

    public TurnoEntity toEntity(TurnoRequestDto dto) {
        TurnoEntity entity = new TurnoEntity();
        entity.setFecha(dto.getFecha());
        entity.setHoraInicio(dto.getHoraInicio());
        return entity;
    }

    public TurnoResponceDto toDto(TurnoEntity entity) {
        TurnoResponceDto dto = modelMapper.map(entity, TurnoResponceDto.class);

        dto.setIdTurno(entity.getIdTurno());

        if (entity.getUsuario() != null) {
            dto.setUsuarioId(entity.getUsuario().getIdUsuario());
        }

        if (entity.getServicio() != null) {
            dto.setServicioId(entity.getServicio().getIdServicio());
        }

        if (entity.getCancelacion() != null) {
            dto.setCancelacionId(entity.getCancelacion().getId());
        }

        if (entity.getComercio() != null) {
            dto.setIdComercio(entity.getComercio().getIdComercio());
        }

        return dto;
    }
}
