package com.example.ProyectoFinalTurnos.features.turno;


import com.example.ProyectoFinalTurnos.exceptions.auth.ResourceNotFoundException;
import com.example.ProyectoFinalTurnos.exceptions.turno.*;
import com.example.ProyectoFinalTurnos.exceptions.user.UserProfileNotFoundException;
import com.example.ProyectoFinalTurnos.features.servicio.ServicioEntity;
import com.example.ProyectoFinalTurnos.features.servicio.ServicioRepository;
import com.example.ProyectoFinalTurnos.features.turno.TurnoEntity;
import com.example.ProyectoFinalTurnos.features.turno.Dtos.TurnoRequestDto;
import com.example.ProyectoFinalTurnos.features.turno.Dtos.TurnoResponceDto;
import com.example.ProyectoFinalTurnos.features.turno.Mapper.TurnoMapper;
import com.example.ProyectoFinalTurnos.features.usuario.UsuarioEntity;
import com.example.ProyectoFinalTurnos.features.usuario.UsuarioRepository;
import com.example.ProyectoFinalTurnos.model.enums.Diasemana;
import com.example.ProyectoFinalTurnos.model.enums.EstadoTurno;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TurnoServiceImp implements TurnoService
{
    private final TurnoRepository turnoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ServicioRepository servicioRepository;
    private final TurnoMapper turnoMapper;


    @Override
    public TurnoResponceDto crearTurno(TurnoRequestDto requestDto)
    {
        validarFechaYHora(requestDto);

        UsuarioEntity usuario = usuarioRepository.findById(requestDto.getUsuarioId())
                .orElseThrow(() ->
                        new UserProfileNotFoundException(
                                "Usuario no encontrado"));

        ServicioEntity servicio = servicioRepository.findById(requestDto.getServicioId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Servicio no encontrado"));

        LocalTime horaInicio = requestDto.getHoraInicio();

        LocalTime horaFinCalculada =
                horaInicio.plusMinutes(
                        servicio.getDuracionDelServicio());

        validarSuperposicionServicio(
                servicio.getIdServicio(),
                requestDto.getFecha(),
                horaInicio,
                horaFinCalculada);

        validarSuperposicionUsuario(
                usuario.getIdUsuario(),
                requestDto.getFecha(),
                horaInicio,
                horaFinCalculada);

        TurnoEntity turno = turnoMapper.toEntity(requestDto);

        turno.setUsuario(usuario);
        turno.setServicio(servicio);
        turno.setEstado(EstadoTurno.PENDIENTE);

        TurnoEntity guardado = turnoRepository.save(turno);

        return turnoMapper.toDto(guardado);
    }

    @Override
    public List<TurnoResponceDto> obtenerTodos()
    {
        return turnoRepository.findAll()
                .stream()
                .map(turnoMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public TurnoResponceDto obtenerPorId(Long id)
    {
        TurnoEntity turno = turnoRepository.findById(id)
                .orElseThrow(() ->
                        new TurnoNotFoundException(
                                "Turno no encontrado"));

        return turnoMapper.toDto(turno);
    }

    @Override
    public TurnoResponceDto actualizarTurno(Long id, TurnoRequestDto dto)
    {
        TurnoEntity turno = turnoRepository.findById(id)
                .orElseThrow(() ->
                        new TurnoNotFoundException(
                                "Turno no encontrado"));

        if (turno.getEstado() == EstadoTurno.CANCELADO)
        {
            throw new TurnoAlreadyCancelledException(
                    "No se puede modificar un turno cancelado");
        }

        validarFechaYHora(dto);

        UsuarioEntity usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() ->
                        new UserProfileNotFoundException(
                                "Usuario no encontrado"));

        ServicioEntity servicio = servicioRepository.findById(dto.getServicioId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Servicio no encontrado"));

        LocalTime horaFinCalculada =
                dto.getHoraInicio()
                        .plusMinutes(
                                servicio.getDuracionDelServicio());

        validarSuperposicionServicioExcluyendoActual(
                id,
                servicio.getIdServicio(),
                dto.getFecha(),
                dto.getHoraInicio(),
                horaFinCalculada);

        validarSuperposicionUsuarioExcluyendoActual(
                id,
                usuario.getIdUsuario(),
                dto.getFecha(),
                dto.getHoraInicio(),
                horaFinCalculada);

        turno.setFecha(dto.getFecha());
        turno.setHoraInicio(dto.getHoraInicio());
        turno.setUsuario(usuario);
        turno.setServicio(servicio);

        TurnoEntity actualizado = turnoRepository.save(turno);

        return turnoMapper.toDto(actualizado);
    }

    @Override
    public void eliminarUnTurno(Long id)
    {
        TurnoEntity turno = turnoRepository.findById(id)
                .orElseThrow(() ->
                        new TurnoNotFoundException(
                                "Turno no encontrado"));

        if (turno.getEstado() == EstadoTurno.FINALIZADO)
        {
            throw new TurnFinishedException(
                    "No se puede eliminar un turno finalizado");
        }

        turnoRepository.delete(turno);
    }

    private void validarFechaYHora(TurnoRequestDto dto)
    {
        if (dto.getFecha().isBefore(LocalDate.now()))
        {
            throw new TurnoPastDateException(
                    "No se puede crear un turno en una fecha pasada");
        }

        if (dto.getFecha().equals(LocalDate.now())
                && dto.getHoraInicio().isBefore(LocalTime.now()))
        {
            throw new TurnoPastDateException(
                    "No se puede crear un turno en una hora pasada");
        }
    }

    private void validarSuperposicionServicio(
            Long servicioId,
            LocalDate fecha,
            LocalTime horaInicio,
            LocalTime horaFin)
    {
        List<TurnoEntity> turnos =
                turnoRepository.findByServicio_IdServicioAndFecha(
                        servicioId,
                        fecha);

        for (TurnoEntity turno : turnos)
        {
            if (turno.getEstado() == EstadoTurno.CANCELADO)
            {
                continue;
            }

            LocalTime inicioExistente = turno.getHoraInicio();

            LocalTime finExistente =
                    inicioExistente.plusMinutes(
                            turno.getServicio()
                                    .getDuracionDelServicio());

            if (horaInicio.isBefore(finExistente)
                    && horaFin.isAfter(inicioExistente))
            {
                throw new DuplicateTurnException(
                        "El horario seleccionado ya está ocupado");
            }
        }
    }

    private void validarSuperposicionUsuario(
            Long usuarioId,
            LocalDate fecha,
            LocalTime horaInicio,
            LocalTime horaFin)
    {
        List<TurnoEntity> turnos =
                turnoRepository.findByUsuario_IdUsuarioAndFecha(
                        usuarioId,
                        fecha);

        for (TurnoEntity turno : turnos)
        {
            if (turno.getEstado() == EstadoTurno.CANCELADO)
            {
                continue;
            }

            LocalTime inicioExistente = turno.getHoraInicio();

            LocalTime finExistente =
                    inicioExistente.plusMinutes(
                            turno.getServicio()
                                    .getDuracionDelServicio());

            if (horaInicio.isBefore(finExistente)
                    && horaFin.isAfter(inicioExistente))
            {
                throw new DuplicateTurnException(
                        "El usuario ya posee un turno en ese horario");
            }
        }
    }

    private void validarSuperposicionServicioExcluyendoActual(
            Long idTurno,
            Long servicioId,
            LocalDate fecha,
            LocalTime horaInicio,
            LocalTime horaFin)
    {
        List<TurnoEntity> turnos =
                turnoRepository.findByServicio_IdServicioAndFecha(
                        servicioId,
                        fecha);

        for (TurnoEntity turno : turnos)
        {
            if (turno.getIdTurno().equals(idTurno))
            {
                continue;
            }

            if (turno.getEstado() == EstadoTurno.CANCELADO)
            {
                continue;
            }

            LocalTime inicioExistente = turno.getHoraInicio();

            LocalTime finExistente =
                    inicioExistente.plusMinutes(
                            turno.getServicio()
                                    .getDuracionDelServicio());

            if (horaInicio.isBefore(finExistente)
                    && horaFin.isAfter(inicioExistente))
            {
                throw new DuplicateTurnException(
                        "El horario seleccionado ya está ocupado");
            }
        }
    }

    private void validarSuperposicionUsuarioExcluyendoActual(
            Long idTurno,
            Long usuarioId,
            LocalDate fecha,
            LocalTime horaInicio,
            LocalTime horaFin)
    {
        List<TurnoEntity> turnos =
                turnoRepository.findByUsuario_IdUsuarioAndFecha(
                        usuarioId,
                        fecha);

        for (TurnoEntity turno : turnos)
        {
            if (turno.getIdTurno().equals(idTurno))
            {
                continue;
            }

            if (turno.getEstado() == EstadoTurno.CANCELADO)
            {
                continue;
            }

            LocalTime inicioExistente = turno.getHoraInicio();

            LocalTime finExistente =
                    inicioExistente.plusMinutes(
                            turno.getServicio()
                                    .getDuracionDelServicio());

            if (horaInicio.isBefore(finExistente)
                    && horaFin.isAfter(inicioExistente))
            {
                throw new DuplicateTurnException(
                        "El usuario ya posee un turno en ese horario");
            }
        }
    }
}
