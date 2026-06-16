package com.example.ProyectoFinalTurnos.features.turno;

import com.example.ProyectoFinalTurnos.exceptions.horarioAtencion.HorarioFueraDeAtencionException;
import com.example.ProyectoFinalTurnos.exceptions.servicio.ServicioNotFoundException;
import com.example.ProyectoFinalTurnos.exceptions.turno.*;
import com.example.ProyectoFinalTurnos.exceptions.user.UserProfileNotFoundException;
import com.example.ProyectoFinalTurnos.features.comercio.ComercioEntity;
import com.example.ProyectoFinalTurnos.features.horarioAtencion.HorarioAtencionEntity;
import com.example.ProyectoFinalTurnos.features.servicio.ServicioEntity;
import com.example.ProyectoFinalTurnos.features.servicio.ServicioRepository;
import com.example.ProyectoFinalTurnos.features.turno.Dtos.TurnoRequestDto;
import com.example.ProyectoFinalTurnos.features.turno.Dtos.TurnoResponceDto;
import com.example.ProyectoFinalTurnos.features.turno.Mapper.TurnoMapper;
import com.example.ProyectoFinalTurnos.features.turno.TurnoEntity;
import com.example.ProyectoFinalTurnos.features.usuario.UsuarioEntity;
import com.example.ProyectoFinalTurnos.features.usuario.UsuarioRepository;
import com.example.ProyectoFinalTurnos.model.enums.Diasemana;
import com.example.ProyectoFinalTurnos.model.enums.EstadoTurno;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TurnoServiceImp implements TurnoService {

    private final NotificacionService  notificacionService;
    private final TurnoRepository turnoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ServicioRepository servicioRepository;
    private final TurnoMapper turnoMapper;

    @Override
    public TurnoResponceDto crearTurno(TurnoRequestDto requestDto) {
        validarFechaYHora(requestDto);

        // Obtener usuario autenticado (ya no viene en el request)
        String emailUsuario = obtenerEmailUsuarioAutenticado();
        UsuarioEntity usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new UserProfileNotFoundException("Usuario autenticado no encontrado"));

        ServicioEntity servicio = servicioRepository.findById(requestDto.getServicioId())
                .orElseThrow(() -> new ServicioNotFoundException("Servicio no encontrado"));

        LocalTime horaInicio = requestDto.getHoraInicio();
        LocalTime horaFinCalculada = horaInicio.plusMinutes(servicio.getDuracionDelServicio());

        // Validar horario en base al comercio del servicio
        validarHorarioAtencion(servicio.getComercio(), requestDto.getFecha(), horaInicio, horaFinCalculada);

        // Validaciones de superposición usan el servicio y el usuario autenticado
        validarSuperposicionServicio(servicio.getIdServicio(), requestDto.getFecha(), horaInicio, horaFinCalculada);
        validarSuperposicionUsuario(usuario.getIdUsuario(), requestDto.getFecha(), horaInicio, horaFinCalculada);

        TurnoEntity turno = turnoMapper.toEntity(requestDto);
        turno.setUsuario(usuario);
        turno.setServicio(servicio);
        turno.setHoraFin(horaFinCalculada);
        turno.setComercio(servicio.getComercio());
        turno.setEstado(EstadoTurno.PENDIENTE);

        TurnoEntity guardado = turnoRepository.save(turno);

        String urlGestion = "http://localhost:8080/turnos/detalles/" + guardado.getIdTurno();
        Context context = new Context();
        context.setVariable("nombre", guardado.getUsuario().getNombre());
        context.setVariable("fecha", guardado.getFecha().toString());
        context.setVariable("hora", guardado.getHoraInicio().toString());
        context.setVariable("urlGestion", urlGestion);
        // 3. Llamar al servicio de notificación (async)
        // El tercer parámetro es el nombre del archivo: "email-turno.html" -> "email-turno"
        try {
            notificacionService.enviarEmailConPlantilla(
                    guardado.getUsuario().getEmail(),
                    "Confirmación de tu Turno",
                    "email-turno",
                    context
            );
        } catch (Exception e) {
            // Esto evita que si falla el mail, el usuario crea que el turno no se guardó
            System.err.println("El turno se guardó, pero hubo un error al enviar el mail: " + e.getMessage());
        }
        return turnoMapper.toDto(guardado);
    }

    @Override
    public List<TurnoResponceDto> obtenerTodos() {
        return turnoRepository.findAll()
                .stream()
                .map(turnoMapper::toDto)
                .toList();
    }

    @Override
    public TurnoResponceDto obtenerPorId(Long id) {
        TurnoEntity turno = turnoRepository.findById(id)
                .orElseThrow(() -> new TurnoNotFoundException("Turno no encontrado"));
        return turnoMapper.toDto(turno);
    }

    @Override
    public TurnoResponceDto actualizarTurno(Long id, TurnoRequestDto dto) {
        TurnoEntity turno = turnoRepository.findById(id)
                .orElseThrow(() -> new TurnoNotFoundException("Turno no encontrado"));

        if (turno.getEstado() == EstadoTurno.CANCELADO) {
            throw new TurnoAlreadyCancelledException("No se puede modificar un turno cancelado");
        }

        validarFechaYHora(dto);

        // Obtener usuario autenticado
        String emailUsuario = obtenerEmailUsuarioAutenticado();
        UsuarioEntity usuarioAutenticado = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new UserProfileNotFoundException("Usuario autenticado no encontrado"));

        // Verificar permisos: solo el propietario o ADMIN pueden modificar
        boolean esAdmin = usuarioAutenticado.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (!turno.getUsuario().getIdUsuario().equals(usuarioAutenticado.getIdUsuario()) && !esAdmin) {
            throw new AccessDeniedException("No tienes permisos para modificar este turno");
        }

        ServicioEntity servicio = servicioRepository.findById(dto.getServicioId())
                .orElseThrow(() -> new ServicioNotFoundException("Servicio no encontrado"));

        LocalTime horaInicio = dto.getHoraInicio();
        LocalTime horaFinCalculada = horaInicio.plusMinutes(servicio.getDuracionDelServicio());

        validarHorarioAtencion(servicio.getComercio(), dto.getFecha(), horaInicio, horaFinCalculada);

        validarSuperposicionServicioExcluyendoActual(turno.getIdTurno(), servicio.getIdServicio(), dto.getFecha(), horaInicio, horaFinCalculada);
        validarSuperposicionUsuarioExcluyendoActual(turno.getIdTurno(), usuarioAutenticado.getIdUsuario(), dto.getFecha(), horaInicio, horaFinCalculada);

        turno.setFecha(dto.getFecha());
        turno.setHoraInicio(horaInicio);
        turno.setHoraFin(horaFinCalculada);
        turno.setServicio(servicio);

        TurnoEntity actualizado = turnoRepository.save(turno);
        return turnoMapper.toDto(actualizado);
    }

    @Override
    public void eliminarUnTurno(Long id) {
        TurnoEntity turno = turnoRepository.findById(id)
                .orElseThrow(() -> new TurnoNotFoundException("Turno no encontrado"));

        if (turno.getEstado() == EstadoTurno.FINALIZADO) {
            throw new TurnFinishedException("No se puede eliminar un turno finalizado");
        }

        String emailUsuario = obtenerEmailUsuarioAutenticado();
        UsuarioEntity usuarioAutenticado = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new UserProfileNotFoundException("Usuario autenticado no encontrado"));

        // Verificar permisos: solo el propietario o ADMIN pueden eliminar
        boolean esAdmin = usuarioAutenticado.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (!turno.getUsuario().getIdUsuario().equals(usuarioAutenticado.getIdUsuario()) && !esAdmin) {
            throw new AccessDeniedException("No tienes permisos para eliminar este turno");
        }

        turnoRepository.delete(turno);
    }

    private void validarFechaYHora(TurnoRequestDto dto) {
        if (dto.getFecha().isBefore(LocalDate.now())) {
            throw new TurnoPastDateException("No se puede crear un turno en una fecha pasada");
        }

        if (dto.getFecha().equals(LocalDate.now())
                && dto.getHoraInicio().isBefore(LocalTime.now())) {
            throw new TurnoPastDateException("No se puede crear un turno en una hora pasada");
        }
    }

    private void validarSuperposicionServicio(Long servicioId, java.time.LocalDate fecha, LocalTime horaInicio, LocalTime horaFin) {
        boolean existeSuperposicion = turnoRepository.findByServicio_IdServicioAndFecha(servicioId, fecha)
                .stream()
                .filter(t -> t.getEstado() != EstadoTurno.CANCELADO)
                .anyMatch(t -> haySuperposicion(t, horaInicio, horaFin));

        if (existeSuperposicion) {
            throw new DuplicateTurnException("El horario seleccionado ya está ocupado");
        }
    }

    private void validarSuperposicionUsuario(Long usuarioId, java.time.LocalDate fecha, LocalTime horaInicio, LocalTime horaFin) {
        boolean existeSuperposicion = turnoRepository.findByUsuario_IdUsuarioAndFecha(usuarioId, fecha)
                .stream()
                .filter(t -> t.getEstado() != EstadoTurno.CANCELADO)
                .anyMatch(t -> haySuperposicion(t, horaInicio, horaFin));

        if (existeSuperposicion) {
            throw new DuplicateTurnException("El usuario ya posee un turno en ese horario");
        }
    }

    private void validarSuperposicionServicioExcluyendoActual(Long idTurno, Long servicioId, java.time.LocalDate fecha, LocalTime horaInicio, LocalTime horaFin) {
        boolean existeSuperposicion = turnoRepository.findByServicio_IdServicioAndFecha(servicioId, fecha)
                .stream()
                .filter(t -> !t.getIdTurno().equals(idTurno))
                .filter(t -> t.getEstado() != EstadoTurno.CANCELADO)
                .anyMatch(t -> haySuperposicion(t, horaInicio, horaFin));

        if (existeSuperposicion) {
            throw new DuplicateTurnException("El horario seleccionado ya está ocupado");
        }
    }

    private void validarSuperposicionUsuarioExcluyendoActual(Long idTurno, Long usuarioId, java.time.LocalDate fecha, LocalTime horaInicio, LocalTime horaFin) {
        boolean existeSuperposicion = turnoRepository.findByUsuario_IdUsuarioAndFecha(usuarioId, fecha)
                .stream()
                .filter(t -> !t.getIdTurno().equals(idTurno))
                .filter(t -> t.getEstado() != EstadoTurno.CANCELADO)
                .anyMatch(t -> haySuperposicion(t, horaInicio, horaFin));

        if (existeSuperposicion) {
            throw new DuplicateTurnException("El usuario ya posee un turno en ese horario");
        }
    }

    private boolean haySuperposicion(TurnoEntity turno, LocalTime horaInicio, LocalTime horaFin) {
        LocalTime inicioExistente = turno.getHoraInicio();
        LocalTime finExistente = inicioExistente.plusMinutes(turno.getServicio().getDuracionDelServicio());
        return horaInicio.isBefore(finExistente) && horaFin.isAfter(inicioExistente);
    }

    private void validarHorarioAtencion(ComercioEntity comercio, java.time.LocalDate fecha, LocalTime horaInicio, LocalTime horaFin) {
        Diasemana diaTurno = mapearDiaSemana(fecha.getDayOfWeek());
        HorarioAtencionEntity horario = comercio.getHorarios().stream()
                .filter(h -> h.getDiaSemana() == diaTurno)
                .findFirst()
                .orElseThrow(() -> new HorarioFueraDeAtencionException("El comercio no atiende los días " + diaTurno));

        if (horaInicio.isBefore(horario.getHoraApertura())) {
            throw new HorarioFueraDeAtencionException("El horario seleccionado es anterior a la apertura del comercio");
        }

        if (horaFin.isAfter(horario.getHoraCierre())) {
            throw new HorarioFueraDeAtencionException("El horario seleccionado excede el horario de cierre del comercio");
        }
    }

    private Diasemana mapearDiaSemana(java.time.DayOfWeek dayOfWeek) {
        return switch (dayOfWeek) {
            case MONDAY -> Diasemana.LUNES;
            case TUESDAY -> Diasemana.MARTES;
            case WEDNESDAY -> Diasemana.MIERCOLES;
            case THURSDAY -> Diasemana.JUEVES;
            case FRIDAY -> Diasemana.VIERNES;
            case SATURDAY -> Diasemana.SABADO;
            case SUNDAY -> Diasemana.DOMINGO;
        };
    }

    private String obtenerEmailUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new UserProfileNotFoundException("No hay usuario autenticado");
        }
        return authentication.getName();
    }

    // Clase de excepción local que puedes usar o reemplazar por la que ya tengas
    private static class AccessDeniedException extends RuntimeException {
        public AccessDeniedException(String message) {
            super(message);
        }
    }
}

