package com.example.ProyectoFinalTurnos.features.turno;


import com.example.ProyectoFinalTurnos.exceptions.auth.ResourceNotFoundException;
import com.example.ProyectoFinalTurnos.exceptions.horarioAtencion.HorarioFueraDeAtencionException;
import com.example.ProyectoFinalTurnos.exceptions.servicio.ServicioNotFoundException;
import com.example.ProyectoFinalTurnos.exceptions.turno.*;
import com.example.ProyectoFinalTurnos.exceptions.user.UserProfileNotFoundException;
import com.example.ProyectoFinalTurnos.features.horarioAtencion.HorarioAtencionEntity;
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
    public class TurnoServiceImp implements TurnoService {

        private final TurnoRepository turnoRepository;
        private final UsuarioRepository usuarioRepository;
        private final ServicioRepository servicioRepository;
        private final TurnoMapper turnoMapper;

        @Override
        public TurnoResponceDto crearTurno(TurnoRequestDto requestDto) {
            validarFechaYHora(requestDto);

            UsuarioEntity usuario = usuarioRepository
                    .findById(requestDto.getUsuarioId())
                    .orElseThrow(() ->
                            new UserProfileNotFoundException(
                                    "Usuario no encontrado"));

            ServicioEntity servicio = servicioRepository
                    .findById(requestDto.getServicioId())
                    .orElseThrow(() ->
                            new ServicioNotFoundException(
                                    "Servicio no encontrado"));

            LocalTime horaInicio = requestDto.getHoraInicio();

            LocalTime horaFinCalculada =
                    horaInicio.plusMinutes(
                            servicio.getDuracionDelServicio());

            // CAMBIO: Obtener horarios del comercio del servicio, no del servicio
            validarHorarioAtencion(
                    servicio.getComercio(),
                    requestDto.getFecha(),
                    horaInicio,
                    horaFinCalculada);

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
            turno.setHoraFin(horaFinCalculada);
            turno.setEstado(EstadoTurno.PENDIENTE);

            TurnoEntity guardado = turnoRepository.save(turno);

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

            UsuarioEntity usuario = usuarioRepository.findById(
                            dto.getUsuarioId())
                    .orElseThrow(() -> new UserProfileNotFoundException("Usuario no encontrado"));

            ServicioEntity servicio = servicioRepository.findById(dto.getServicioId())
                    .orElseThrow(() -> new ServicioNotFoundException("Servicio no encontrado"));

            LocalTime horaInicio = dto.getHoraInicio();

            LocalTime horaFinCalculada = horaInicio.plusMinutes(servicio.getDuracionDelServicio());

            // CAMBIO: Obtener horarios del comercio del servicio, no del servicio
            validarHorarioAtencion(
                    servicio.getComercio(),
                    dto.getFecha(),
                    horaInicio,
                    horaFinCalculada);

            validarSuperposicionServicioExcluyendoActual(
                    turno.getIdTurno(),
                    servicio.getIdServicio(),
                    dto.getFecha(),
                    horaInicio,
                    horaFinCalculada);

            validarSuperposicionUsuarioExcluyendoActual(
                    turno.getIdTurno(),
                    usuario.getIdUsuario(),
                    dto.getFecha(),
                    horaInicio,
                    horaFinCalculada);

            turno.setFecha(dto.getFecha());
            turno.setHoraInicio(horaInicio);
            turno.setHoraFin(horaFinCalculada);
            turno.setUsuario(usuario);
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

        private void validarSuperposicionServicio(
                Long servicioId,
                LocalDate fecha,
                LocalTime horaInicio,
                LocalTime horaFin) {
            boolean existeSuperposicion =
                    turnoRepository.findByServicio_IdServicioAndFecha(
                                    servicioId,
                                    fecha)
                            .stream()
                            .filter(turno ->
                                    turno.getEstado() != EstadoTurno.CANCELADO)
                            .anyMatch(turno ->
                                    haySuperposicion(
                                            turno,
                                            horaInicio,
                                            horaFin));

            if (existeSuperposicion) {
                throw new DuplicateTurnException(
                        "El horario seleccionado ya está ocupado");
            }
        }

        private void validarSuperposicionUsuario(
                Long usuarioId,
                LocalDate fecha,
                LocalTime horaInicio,
                LocalTime horaFin) {
            boolean existeSuperposicion =
                    turnoRepository.findByUsuario_IdUsuarioAndFecha(
                                    usuarioId,
                                    fecha)
                            .stream()
                            .filter(turno ->
                                    turno.getEstado() != EstadoTurno.CANCELADO)
                            .anyMatch(turno ->
                                    haySuperposicion(
                                            turno,
                                            horaInicio,
                                            horaFin));

            if (existeSuperposicion) {
                throw new DuplicateTurnException(
                        "El usuario ya posee un turno en ese horario");
            }
        }

        private void validarSuperposicionServicioExcluyendoActual(
                Long idTurno,
                Long servicioId,
                LocalDate fecha,
                LocalTime horaInicio,
                LocalTime horaFin) {
            boolean existeSuperposicion =
                    turnoRepository.findByServicio_IdServicioAndFecha(
                                    servicioId,
                                    fecha)
                            .stream()
                            .filter(turno ->
                                    !turno.getIdTurno().equals(idTurno))
                            .filter(turno ->
                                    turno.getEstado() != EstadoTurno.CANCELADO)
                            .anyMatch(turno ->
                                    haySuperposicion(
                                            turno,
                                            horaInicio,
                                            horaFin));

            if (existeSuperposicion) {
                throw new DuplicateTurnException(
                        "El horario seleccionado ya está ocupado");
            }
        }

        private void validarSuperposicionUsuarioExcluyendoActual(
                Long idTurno,
                Long usuarioId,
                LocalDate fecha,
                LocalTime horaInicio,
                LocalTime horaFin) {
            boolean existeSuperposicion =
                    turnoRepository.findByUsuario_IdUsuarioAndFecha(
                                    usuarioId,
                                    fecha)
                            .stream()
                            .filter(turno ->
                                    !turno.getIdTurno().equals(idTurno))
                            .filter(turno ->
                                    turno.getEstado() != EstadoTurno.CANCELADO)
                            .anyMatch(turno ->
                                    haySuperposicion(
                                            turno,
                                            horaInicio,
                                            horaFin));

            if (existeSuperposicion) {
                throw new DuplicateTurnException(
                        "El usuario ya posee un turno en ese horario");
            }
        }

        private boolean haySuperposicion(
                TurnoEntity turno,
                LocalTime horaInicio,
                LocalTime horaFin) {
            LocalTime inicioExistente = turno.getHoraInicio();

            LocalTime finExistente =
                    inicioExistente.plusMinutes(
                            turno.getServicio()
                                    .getDuracionDelServicio());

            return horaInicio.isBefore(finExistente)
                    && horaFin.isAfter(inicioExistente);
        }

        /**
         * CAMBIO PRINCIPAL: Ahora recibe ComercioEntity en lugar de ServicioEntity     * Los horarios están asociados directamente al comercio, no al servicio
         */
        private void validarHorarioAtencion(
                com.example.ProyectoFinalTurnos.features.comercio.ComercioEntity comercio,
                LocalDate fecha,
                LocalTime horaInicio,
                LocalTime horaFin) {

            // Mapear día de la fecha al enum Diasemana
            Diasemana diaTurno = mapearDiaSemana(fecha.getDayOfWeek());

            // Buscar un horario del comercio para ese día
            HorarioAtencionEntity horario = comercio.getHorarios().stream()
                    .filter(h -> h.getDiaSemana() == diaTurno)
                    .findFirst()
                    .orElseThrow(() -> new HorarioFueraDeAtencionException(
                            "El comercio no atiende los días " + diaTurno));

            // Validar que la hora de inicio esté dentro del horario
            if (horaInicio.isBefore(horario.getHoraApertura())) {
                throw new HorarioFueraDeAtencionException(
                        "El horario seleccionado es anterior a la apertura del comercio");
            }

            // Validar que la hora de fin esté dentro del horario
            if (horaFin.isAfter(horario.getHoraCierre())) {
                throw new HorarioFueraDeAtencionException(
                        "El horario seleccionado excede el horario de cierre del comercio");
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
    }

