package com.example.ProyectoFinalTurnos.features.turno.Mapper;

import com.example.ProyectoFinalTurnos.features.servicio.ServicioEntity;
import com.example.ProyectoFinalTurnos.features.turno.TurnoEntity;
import com.example.ProyectoFinalTurnos.features.turno.Dtos.TurnoRequestDto;
import com.example.ProyectoFinalTurnos.features.turno.Dtos.TurnoResponceDto;
import com.example.ProyectoFinalTurnos.features.usuario.UsuarioEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TurnoMapper
{
    private final ModelMapper modelMapper;

    public TurnoEntity toEntity(TurnoRequestDto dto)
    {
        return modelMapper.map(dto,TurnoEntity.class);
    }

//    public TurnoResponceDto toDto(TurnoEntity entity)
//    {
//
//        return modelMapper.map(entity,TurnoResponceDto.class);
//    }
    public TurnoResponceDto toDto(TurnoEntity entity) {
        // 1. Dejamos que ModelMapper mapee lo que pueda (fecha, hora, estado)
        TurnoResponceDto dto = modelMapper.map(entity, TurnoResponceDto.class);

        // 2. Mapeamos manualmente los IDs que fallan (ajustá los "getters" según tu Entidad)
        dto.setIdTurno(entity.getIdTurno());

        if (entity.getUsuario() != null) {
            dto.setUsuarioId(entity.getUsuario().getIdUsuario()); // O getIdUsuario() según cómo lo tengas
        }

        if (entity.getServicio() != null) {
            dto.setServicioId(entity.getServicio().getIdServicio()); // O getIdServicio()
        }

        if (entity.getCancelacion() != null) {
            dto.setCancelacionId(entity.getCancelacion().getId());
        }


        return dto;
    }








}
