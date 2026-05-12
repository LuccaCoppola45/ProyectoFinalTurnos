package com.example.ProyectoFinalTurnos.features.horarioAtencion.Mapper;

import com.example.ProyectoFinalTurnos.features.horarioAtencion.Dtos.HorarioAtencionRequest;
import com.example.ProyectoFinalTurnos.features.horarioAtencion.Dtos.HorarioAtencionResponse;
import com.example.ProyectoFinalTurnos.features.horarioAtencion.HorarioAtencionEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component

public class HorarioAtencionMapper {

    private  final ModelMapper modelMapper;

    public HorarioAtencionMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public HorarioAtencionEntity toEntity(HorarioAtencionRequest dto){
        return modelMapper.map(dto, HorarioAtencionEntity.class);
    }

    public HorarioAtencionResponse toDto(HorarioAtencionEntity dto){
        HorarioAtencionResponse entity = modelMapper.map(dto, HorarioAtencionResponse.class);

        if(dto.getComercio() != null){
            entity.setIdComercio(dto.getComercio().getId());
        }
        return entity;
    }
}
