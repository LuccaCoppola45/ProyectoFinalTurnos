package com.example.ProyectoFinalTurnos.features.cancelacion;

import com.example.ProyectoFinalTurnos.exceptions.cancelacion.CancelacionNotFoundException;
import com.example.ProyectoFinalTurnos.exceptions.turno.TurnFinishedException;
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
import com.example.ProyectoFinalTurnos.model.enums.Rol;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
        // Quien cancela es el usuario logueado (sale del token), no el id que manda el front:
        // así nadie puede cancelar "en nombre de" otro usuario cambiando el body.
        UsuarioEntity usuario = usuarioAutenticado();

        TurnoEntity turno = turnoRepository.findById(requestDTO.getIdTurno())
                .orElseThrow(() -> new TurnoNotFoundException("Turno no encontrado con ID: " + requestDTO.getIdTurno()));

        // 1. ¿Puede cancelarlo? (RF-14 el cliente del turno, RF-19 el dueño del comercio, o un admin)
        boolean esElCliente = turno.getUsuario().getIdUsuario().equals(usuario.getIdUsuario());
        boolean esElComercio = esDuenioDelComercio(usuario, turno);
        boolean esAdmin = usuario.getRol() == Rol.ADMIN;
        if (!esElCliente && !esElComercio && !esAdmin) {
            throw new UnauthorizedTurnoAccessException("No tienes permiso para cancelar este turno.");
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
        if (turno.getEstado() == EstadoTurno.FINALIZADO) {
            throw new TurnFinishedException("No se puede cancelar un turno finalizado.");
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

        // El mail siempre le llega al CLIENTE del turno (aunque lo haya cancelado el comercio)
        UsuarioEntity cliente = turno.getUsuario();
        eventPublisher.publishEvent(new TurnoCanceladoEvent(
                turno.getIdTurno(),
                cliente.getEmail(),
                cliente.getNombre(),
                turno.getServicio().getNombre(),
                turno.getComercio().getNombre(),
                turno.getFecha(),
                turno.getHoraInicio(),
                guardada.getMotivo(),
                !esElCliente // true = lo canceló el comercio (o un admin)
        ));

        // 5. USO DEL MAPPER (Entidad -> ResponseDTO)
        String mensajeExito = "El turno ID: " + turno.getIdTurno() + " se canceló correctamente.";
        return cancelacionMapper.entityToResponseDto(guardada);
    }

    private UsuarioEntity usuarioAutenticado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) {
            throw new UnauthorizedTurnoAccessException("Tenés que iniciar sesión para cancelar un turno.");
        }
        return usuarioRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new UserProfileNotFoundException("Usuario no encontrado: " + auth.getName()));
    }

    private boolean esDuenioDelComercio(UsuarioEntity usuario, TurnoEntity turno) {
        UsuarioEntity duenio = turno.getComercio() != null ? turno.getComercio().getUsuarioPropietario() : null;
        return duenio != null && duenio.getIdUsuario().equals(usuario.getIdUsuario());
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
