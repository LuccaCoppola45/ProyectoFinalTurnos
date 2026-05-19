package com.example.ProyectoFinalTurnos.features.categoria.mapper;

import com.example.ProyectoFinalTurnos.features.categoria.CategoriaEntity;
import com.example.ProyectoFinalTurnos.features.categoria.dto.categoriaRequest;
import com.example.ProyectoFinalTurnos.features.categoria.dto.categoriaResponse;
import org.springframework.stereotype.Component;

@Component
public class CategoriaMapper {
    
    public CategoriaEntity requestDtoToEntity(categoriaRequest requestDto) {
        if (requestDto == null) {
            return null;
        }
        CategoriaEntity categoriaEntity = new CategoriaEntity();
        categoriaEntity.setNombreCategoria(requestDto.getNombreCategoria());
        return categoriaEntity;
    }
    
    public categoriaResponse entityToResponseDto(CategoriaEntity entity) {
        if (entity == null) {
            return null;
        }
        categoriaResponse responseDto = new categoriaResponse();
        responseDto.setUniqueId(entity.getUniqueId());
        responseDto.setNombreCategoria(entity.getNombreCategoria());
        return responseDto;
    }
}

