package com.example.ProyectoFinalTurnos.features.comercio.Mapper;

import com.example.ProyectoFinalTurnos.features.comercio.ComercioEntity;
import com.example.ProyectoFinalTurnos.features.comercio.Dtos.ComercioRequest;
import com.example.ProyectoFinalTurnos.features.comercio.Dtos.ComercioResponse;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ComercioMapper {

    private final ModelMapper modelMapper;

    public ComercioMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ComercioResponse toResponse(ComercioEntity entity) {
        if (entity == null) {
            return null;
        }

        ComercioResponse response = modelMapper.map(entity, ComercioResponse.class);

        if (entity.getCategorias() != null) {
            response.setCategorias(
                    entity.getCategorias().stream()
                            .map(cat -> cat.getNombre())
                            .collect(Collectors.toList())
            );
        }

        return response;
    }

    public ComercioEntity toEntity(ComercioRequest request) {
        if (request == null) {
            return null;
        }

        return modelMapper.map(request, ComercioEntity.class);
    }

    public void updateEntity(ComercioRequest request, ComercioEntity entity) {
        if (request == null || entity == null) {
            return;
        }

        modelMapper.map(request, entity);
    }
}
