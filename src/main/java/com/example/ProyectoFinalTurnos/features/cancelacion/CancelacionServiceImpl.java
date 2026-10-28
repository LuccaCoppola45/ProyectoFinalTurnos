package com.example.ProyectoFinalTurnos.features.cancelacion;

import com.example.ProyectoFinalTurnos.exceptions.cancelacion.CancelacionNotFoundException;
import com.example.ProyectoFinalTurnos.exceptions.turno.TurnoAlreadyCancelledException;
import com.example.ProyectoFinalTurnos.exceptions.turno.TurnoNotFoundException;
import com.example.ProyectoFinalTurnos.exceptions.turno.TurnoPastDateException;
import com.example.ProyectoFinalTurnos.exceptions.turno.UnauthorizedTurnoAccessException;
import com.example.ProyectoFinalTurnos.exceptions.user.UserProfileNotFoundException;
import com.example.ProyectoFinalTurnos.features.cancelacion.dto.CancelacionRequest;
import com.example.ProyectoFinalTurnos.features.cancelacion.dto.CancelacionResponse;
import com.example.ProyectoFinalTurnos.features.cancelacion.mapper.CancelacionMapper;
import com.example.ProyectoFinalTurnos.features.notificacion.TurnoCanceladoEvent;
import com.example.ProyectoFinalTurnos.features.turno.TurnoEntity;
import com.example.ProyectoFinalTurnos.features.turno.TurnoRepository;
import com.example.ProyectoFinalTurnos.features.usuario.UsuarioEntity;
import com.example.ProyectoFinalTurnos.features.usuario.UsuarioRepository;
import com.example.ProyectoFinalTurnos.model.enums.EstadoTurno;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CancelacionServiceImpl implements CancelacionService {
    private final CancelacionRepository cancelacionRepository;
    private final UsuarioRepository usuarioRepository;
    private final TurnoRepository turnoRepository;

    private final CancelacionMapper cancelacionMapper;
    private final ApplicationEventPublisher eventPublisher;

    //crear una cancelacion
    @Override
    @Transactional
    public CancelacionResponse cancelarTurno(CancelacionRequest requestDTO) {
        UsuarioEntity usuario = usuarioRepository.findById(requestDTO.getIdUsuario()).
                orElseThrow(() -> new UserProfileNotFoundException("Usuario no encontrado: " + requestDTO.getIdUsuario()));

        TurnoEntity turno = turnoRepository.findById(requestDTO.getIdTurno())
                .orElseThrow(() -> new TurnoNotFoundException("Turno no encontrado con ID: " + requestDTO.getIdTurno()));

        // 1. ¿El turno le pertenece al usuario que lo quiere cancelar?
        if (!turno.getUsuario().getIdUsuario().equals(usuario.getIdUsuario())) {
            throw new UnauthorizedTurnoAccessException("No tienes permiso para cancelar un turno que no te pertenece.");
        }

        // 2. Verifica que el turno no sea en el pasado
        LocalDateTime fechaHoraDelTurno = LocalDateTime.of(turno.getFecha(), turno.getHoraInicio());

        if (fechaHoraDelTurno.isBefore(LocalDateTime.now())) {
            throw new TurnoPastDateException("No se puede cancelar un turno cuya fecha y hora ya pasaron.");
        }

        // 3. Verifica estado actual del turno usando tu Enum
        if (turno.getEstado() == EstadoTurno.CANCELADO) {
            throw new TurnoAlreadyCancelledException("Este turno ya se encuentra cancelado.");
        }

        // 2. USO DEL MAPPER (DTO -> Entidad)
        CancelacionEntity cancelacionEntity = cancelacionMapper.requestDtoToEntity(requestDTO);


        cancelacionEntity.setUsuario(usuario);
        cancelacionEntity.setTurno(turno);
        cancelacionEntity.setFechaCancelacion(LocalDateTime.now());

        turno.setEstado(EstadoTurno.CANCELADO); // Acá le pasamos la constante del Enum
        turnoRepository.save(turno);
        // 4. PERSISTENCIA
        CancelacionEntity guardada = cancelacionRepository.save(cancelacionEntity);
        eventPublisher.publishEvent(new TurnoCanceladoEvent(
                turno.getIdTurno(),
                usuario.getEmail(),
                usuario.getNombre(),
                turno.getServicio().getNombre(),
                turno.getComercio().getNombre(),
                turno.getFecha(),
                turno.getHoraInicio(),
                guardada.getMotivo()
        ));

        // 5. USO DEL MAPPER (Entidad -> ResponseDTO)
        String mensajeExito = "El turno ID: " + turno.getIdTurno() + " se canceló correctamente.";
        return cancelacionMapper.entityToResponseDto(guardada);
    }

    @Override
    public List<CancelacionResponse> listarCancelaciones (){
        List <CancelacionEntity> cancelaciones = cancelacionRepository.findAll();

        return cancelaciones.stream()
                .map(cancelacion -> cancelacionMapper.entityToResponseDto(cancelacion))
                .toList();
    }

    @Override
    public CancelacionResponse obtenerCancelacionPorId(Long id){
        CancelacionEntity cancelacion = cancelacionRepository.findById(id).
                orElseThrow(()->new CancelacionNotFoundException("Cancelacion no encontrada con ID: " + id));
        return cancelacionMapper.entityToResponseDto(cancelacion);
    }

}
