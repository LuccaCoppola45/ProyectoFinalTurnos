package com.example.ProyectoFinalTurnos.features.servicio.Mapper;

import com.example.ProyectoFinalTurnos.features.servicio.Dtos.ServicioRequest;
import com.example.ProyectoFinalTurnos.features.servicio.Dtos.ServicioResponse;
import com.example.ProyectoFinalTurnos.features.servicio.ServicioEntity;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

@Component
public class ServicioMapper {
    private final ModelMapper modelMapper;

    public ServicioMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        this.modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }


    public ServicioEntity toEntity(ServicioRequest request) {
        return modelMapper.map(request, ServicioEntity.class);
    }

    public ServicioResponse toDto(ServicioEntity entity) {
        ServicioResponse response = new ServicioResponse();

        // Mapeo de propiedades básicas
        response.setIdServicio(entity.getIdServicio());
        response.setNombreServicio(entity.getNombre());
        response.setPrecio(entity.getPrecio());

        // Mapeo de comercio ID
        if (entity.getComercio() != null) {
            response.setIdComercio(entity.getComercio().getId());
        }

        // Mapeo de horarios IDs
        if (entity.getHorarios() != null && !entity.getHorarios().isEmpty()) {
            response.setHorariosAtencionIds(
                entity.getHorarios().stream()
                    .map(h -> h.getId())
                    .toList()
            );
        }

        return response;
    }
}
