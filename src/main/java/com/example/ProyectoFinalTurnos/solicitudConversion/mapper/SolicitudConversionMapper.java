package com.example.ProyectoFinalTurnos.solicitudConversion.mapper;

import com.example.ProyectoFinalTurnos.solicitudConversion.SolicitudConversionEntity;
import com.example.ProyectoFinalTurnos.solicitudConversion.dtos.SolicitudConversionRequest;
import com.example.ProyectoFinalTurnos.solicitudConversion.dtos.SolicitudConversionResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SolicitudConversionMapper
{
    private final ModelMapper modelMapper;

    public SolicitudConversionEntity toEntity(SolicitudConversionRequest request) {
        SolicitudConversionEntity entity = new SolicitudConversionEntity();
        entity.setNombreComercioPropuesto(request.getNombreComercioPropuesto());
        entity.setDireccion(request.getDireccion());
        entity.setTelefono(request.getTelefono());
        entity.setDescripcion(request.getDescripcion());
        // NO asignamos usuario ni estado; el servicio los asigna manualmente
        return entity;
    }

    public SolicitudConversionResponse toDto(SolicitudConversionEntity entity) {
        SolicitudConversionResponse dto = new SolicitudConversionResponse();
        dto.setId(entity.getId());

        if (entity.getUsuario() != null) {
            dto.setUsuarioId(entity.getUsuario().getIdUsuario());
        }

        dto.setNombreComercioPropuesto(entity.getNombreComercioPropuesto());
        dto.setDireccion(entity.getDireccion());
        dto.setTelefono(entity.getTelefono());
        dto.setDescripcion(entity.getDescripcion());
        dto.setEstado(entity.getEstado());
        dto.setFechaSolicitud(entity.getFechaSolicitud());
        return dto;
    }

}
