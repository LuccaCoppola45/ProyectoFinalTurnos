package com.example.ProyectoFinalTurnos.features.comercio.Mapper;

import com.example.ProyectoFinalTurnos.features.categoria.CategoriaEntity;
import com.example.ProyectoFinalTurnos.features.comercio.ComercioEntity;
import com.example.ProyectoFinalTurnos.features.comercio.Dtos.ComercioRequest;
import com.example.ProyectoFinalTurnos.features.comercio.Dtos.ComercioResponse;
import com.example.ProyectoFinalTurnos.features.horarioAtencion.HorarioAtencionEntity;
import com.example.ProyectoFinalTurnos.features.horarioAtencion.Mapper.HorarioAtencionMapper;
import com.example.ProyectoFinalTurnos.features.servicio.Mapper.ServicioMapper;
import com.example.ProyectoFinalTurnos.features.servicio.ServicioEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ComercioMapper {

    private final ModelMapper modelMapper;
    private final ServicioMapper servicioMapper;      // ← AGREGAR
    private final HorarioAtencionMapper horarioMapper; // ← AGREGAR

    public ComercioMapper(ModelMapper modelMapper,
                          ServicioMapper servicioMapper,      // ← AGREGAR
                          HorarioAtencionMapper horarioMapper) { // ← AGREGAR
        this.modelMapper = modelMapper;
        this.servicioMapper = servicioMapper;         // ← AGREGAR
        this.horarioMapper = horarioMapper;           // ← AGREGAR
    }

    public ComercioResponse toResponse(ComercioEntity entity)
    {
        if (entity == null)
        {
            return null;
        }

        ComercioResponse response =
                modelMapper.map(entity, ComercioResponse.class);

        if (entity.getCategorias() != null)
        {
            response.setCategorias(
                    entity.getCategorias()
                            .stream()
                            .map(CategoriaEntity::getNombreCategoria)
                            .toList()
            );
        }

        if (entity.getServicios() != null)
        {
            response.setServicios(
                    entity.getServicios()
                            .stream()
                            .map(servicioMapper::toDto)
                            .toList()
            );
        }

        if (entity.getHorarios() != null)
        {
            response.setHorarios(
                    entity.getHorarios()
                            .stream()
                            .map(horarioMapper::toDto)
                            .toList()
            );
        }

        if (entity.getUsuarioPropietario() != null) {
            response.setUsuarioPropietarioId(entity.getUsuarioPropietario().getIdUsuario());
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
