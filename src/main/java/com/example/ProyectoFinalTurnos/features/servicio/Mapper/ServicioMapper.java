package com.example.ProyectoFinalTurnos.features.servicio.Mapper;

import com.example.ProyectoFinalTurnos.features.servicio.Dtos.ServicioRequest;
import com.example.ProyectoFinalTurnos.features.servicio.Dtos.ServicioResponse;
import com.example.ProyectoFinalTurnos.features.servicio.ServicioEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ServicioMapper {
    private final ModelMapper modelMapper;

    public ServicioMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ServicioEntity toEntity(ServicioRequest request) {
        return modelMapper.map(request, ServicioEntity.class);
    }

    public ServicioResponse toDto(ServicioEntity entity) {
        ServicioResponse response = modelMapper.map(entity, ServicioResponse.class);

        // Mapeo manual de IDs para asegurar que el Response sea limpio
        if (entity.getComercio() != null) {
            response.setIdComercio(entity.getComercio().getId());
        }
        if (entity.getHorario() != null) {
            response.setIdHorario(entity.getHorario().getId());
        }
        return response;
    }
}
