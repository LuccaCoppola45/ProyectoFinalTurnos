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

    public TurnoResponceDto toDto(TurnoEntity entity)
    {
        return modelMapper.map(entity,TurnoResponceDto.class);
    }








}
