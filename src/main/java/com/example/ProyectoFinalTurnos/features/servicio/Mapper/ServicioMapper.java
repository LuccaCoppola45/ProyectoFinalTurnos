package com.example.ProyectoFinalTurnos.features.servicio.Mapper;

import com.example.ProyectoFinalTurnos.features.horarioAtencion.HorarioAtencionEntity;
import com.example.ProyectoFinalTurnos.features.servicio.Dtos.ServicioRequest;
import com.example.ProyectoFinalTurnos.features.servicio.Dtos.ServicioResponse;
import com.example.ProyectoFinalTurnos.features.servicio.ServicioEntity;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

@Component
public class ServicioMapper {

    public ServicioMapper(ModelMapper modelMapper) {
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

    }

    public ServicioEntity toEntity(ServicioRequest request) {

        if (request == null) {
            return null;
        }

        ServicioEntity entity = new ServicioEntity();

        entity.setNombre(request.getNombreServicio());
        entity.setPrecio(request.getPrecio());
        entity.setDuracionDelServicio(request.getDuracionDelServicio());

        return entity;
    }

    public ServicioResponse toDto(ServicioEntity entity) {
        if (entity == null) {
            return null;
        }

        ServicioResponse response = new ServicioResponse();

        response.setIdServicio(entity.getIdServicio());
        response.setNombreServicio(entity.getNombre());
        response.setPrecio(entity.getPrecio());
        response.setDuracionDelServicio(entity.getDuracionDelServicio());

        if (entity.getComercio() != null) {
            response.setIdComercio(entity.getComercio().getIdComercio());
        }

        return response;
    }
}
