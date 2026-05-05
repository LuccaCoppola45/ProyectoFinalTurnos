package com.example.ProyectoFinalTurnos.features.usuario.mapper;


import com.example.ProyectoFinalTurnos.features.servicio.ServicioEntity;
import com.example.ProyectoFinalTurnos.features.usuario.UsuarioEntity;
import com.example.ProyectoFinalTurnos.features.usuario.dto.UsuarioRequest;
import com.example.ProyectoFinalTurnos.features.usuario.dto.UsuarioResponce;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


@Component
public class UsuarioMapper
{
    private final ModelMapper modelMapper;

    public UsuarioMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public UsuarioEntity toEntity(UsuarioRequest dto, List<ServicioEntity>servicios)
    {
        UsuarioEntity usuario = modelMapper.map(dto, UsuarioEntity.class);
        usuario.setServiciosVinculados(servicios);
        return usuario;
    }

    public UsuarioResponce toResponce(UsuarioEntity usuario)
    {
        UsuarioResponce dto = modelMapper.map(usuario, UsuarioResponce.class);
        dto.setServiciosIds(
                usuario.getServiciosVinculados()
                        .stream()
                        .map(ServicioEntity::getIdServicio)
                        .collect(Collectors.toList())
        );
        dto.setTurnosIds(
                usuario.getTurnosVinculados()
                        .stream()
                        .map(t -> t.getIdTurno())
                        .collect(Collectors.toList())
        );
        dto.setCancelacionesIds(
                usuario.getCancelacionesEntitiesVinculados()
                        .stream()
                        .map(c -> c.getIdCancelaciones())
                        .collect(Collectors.toList())
        );
        return dto;
    }


}
