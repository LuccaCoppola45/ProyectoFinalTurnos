package com.example.ProyectoFinalTurnos.features.turno.Mapper;

import com.example.ProyectoFinalTurnos.features.servicio.ServicioEntity;
import com.example.ProyectoFinalTurnos.features.turno.TurnoEntity;
import com.example.ProyectoFinalTurnos.features.turno.Dtos.TurnoRequestDto;
import com.example.ProyectoFinalTurnos.features.turno.Dtos.TurnoResponceDto;
import com.example.ProyectoFinalTurnos.features.usuario.UsuarioEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class TurnoMapper
{
    private final ModelMapper modelMapper;

    public TurnoMapper(ModelMapper modelMapper)
    {
        this.modelMapper = modelMapper;
    }

    /// -DTO -> Entity- ///

    public TurnoEntity toEntity(TurnoRequestDto dto, UsuarioEntity usuario, ServicioEntity servicio)
    {
        TurnoEntity turno = modelMapper.map(dto, TurnoEntity.class);
        turno.setUsuario(usuario);
        turno.setServicio(servicio);
        return turno;
    }

    /// -Entity -> DTO- ///

    public TurnoResponceDto toDto(TurnoEntity entity)
    {
        TurnoResponceDto dto = modelMapper.map(entity, TurnoResponceDto.class);

        if (entity.getUsuario() != null) {
            dto.setUsuarioId(entity.getUsuario().getIdUsuario());
        }
        if (entity.getServicio() != null) {
            dto.setServicioId(entity.getServicio().getIdServicio());
        }
        if (entity.getCancelacion() != null) {
            dto.setCancelacionId(entity.getCancelacion().getId());
        }

        return dto;
    }

}
