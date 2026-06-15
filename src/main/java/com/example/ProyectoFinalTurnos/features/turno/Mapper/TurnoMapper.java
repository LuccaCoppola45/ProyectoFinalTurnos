package com.example.ProyectoFinalTurnos.features.turno.Mapper;

import com.example.ProyectoFinalTurnos.features.turno.TurnoEntity;
import com.example.ProyectoFinalTurnos.features.turno.Dtos.TurnoRequestDto;
import com.example.ProyectoFinalTurnos.features.turno.Dtos.TurnoResponceDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TurnoMapper {

    private final ModelMapper modelMapper;

    /**     * Mapea DTO a entidad.     * NOTA: usuarioId ya NO viene en el DTO, así que solo mapeamos fecha, horaInicio, servicioId.     * El servicio asigna usuario y servicio manualmente.     */
    public TurnoEntity toEntity(TurnoRequestDto dto) {
        TurnoEntity entity = new TurnoEntity();
        entity.setFecha(dto.getFecha());
        entity.setHoraInicio(dto.getHoraInicio());
        // NO mapeamos usuarioId porque ya no existe en el DTO
        // NO mapeamos servicioId porque el servicio lo asigna manualmente
        // NO mapeamos estado porque se asigna en el servicio (PENDIENTE)
        return entity;
    }

    /**     * Mapea entidad a DTO.     * Extrae los IDs de las relaciones (usuario, servicio, cancelacion).     */
    public TurnoResponceDto toDto(TurnoEntity entity) {
        TurnoResponceDto dto = modelMapper.map(entity, TurnoResponceDto.class);

        dto.setIdTurno(entity.getIdTurno());

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
