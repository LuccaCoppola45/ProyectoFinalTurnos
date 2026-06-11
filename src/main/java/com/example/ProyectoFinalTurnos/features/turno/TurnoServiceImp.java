package com.example.ProyectoFinalTurnos.features.turno;


import com.example.ProyectoFinalTurnos.exceptions.auth.ResourceNotFoundException;
import com.example.ProyectoFinalTurnos.exceptions.turno.*;
import com.example.ProyectoFinalTurnos.exceptions.user.UserProfileNotFoundException;
import com.example.ProyectoFinalTurnos.features.servicio.ServicioEntity;
import com.example.ProyectoFinalTurnos.features.servicio.ServicioRepository;
import com.example.ProyectoFinalTurnos.features.turno.Dtos.TurnoRequestDto;
import com.example.ProyectoFinalTurnos.features.turno.Dtos.TurnoResponceDto;
import com.example.ProyectoFinalTurnos.features.turno.Mapper.TurnoMapper;
import com.example.ProyectoFinalTurnos.features.usuario.UsuarioEntity;
import com.example.ProyectoFinalTurnos.features.usuario.UsuarioRepository;
import com.example.ProyectoFinalTurnos.model.enums.Diasemana;
import com.example.ProyectoFinalTurnos.model.enums.EstadoTurno;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TurnoServiceImp implements TurnoService
{

    private final TurnoRepository turnoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ServicioRepository servicioRepository;
    private final TurnoMapper turnoMapper;

    public TurnoServiceImp(TurnoRepository turnoRepository, UsuarioRepository usuarioRepository, ServicioRepository servicioRepository, TurnoMapper turnoMapper)
    {
        this.turnoRepository = turnoRepository;
        this.usuarioRepository = usuarioRepository;
        this.servicioRepository = servicioRepository;
        this.turnoMapper = turnoMapper;
    }

    @Override
    @Transactional
    public TurnoResponceDto crearTurno(TurnoRequestDto requestDto) {
        // 1. Validar que la fecha y hora no sean en el pasado
        LocalDateTime fechaHoraDelTurno = LocalDateTime.of(requestDto.getFecha(), requestDto.getHoraInicio());
        if (fechaHoraDelTurno.isBefore(LocalDateTime.now())) {
            throw new TurnoPastDateException("No se puede reservar un turno en una fecha u hora que ya pasaron.");
        }

        // 2. Buscar el usuario y el servicio
        UsuarioEntity usuario = usuarioRepository.findById(requestDto.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + requestDto.getUsuarioId()));

        ServicioEntity servicio = servicioRepository.findById(requestDto.getServicioId())
                .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado con ID: " + requestDto.getServicioId()));

        // 3. --- CÁLCULO DE LA HORA FIN ---
        LocalTime horaInicio = requestDto.getHoraInicio();
        LocalTime horaFinCalculada = horaInicio.plusMinutes(servicio.getDuracionDelServicio());

        // 4. Validar Superposición (Overbooking)
        // Buscamos si ya existe algún turno activo que se cruce con este horario
        boolean existeSuperposicion = turnoRepository.existsSuperposicion(
                servicio.getIdServicio(),
                requestDto.getFecha(),
                horaInicio,
                horaFinCalculada
        );

        if (existeSuperposicion) {
            throw new TurnoOutsideBusinessException("El horario seleccionado ya no se encuentra disponible. Por favor, elija otro.");
        }

        // (OPCIONAL) 5. Validar Horario de Atención del Comercio
        // Aquí deberías traer el horario del comercio y validar que horaInicio y horaFinCalculada
        // estén dentro del rango de apertura y cierre del local.

        // 6. Mapear y Setear datos
        TurnoEntity turnoEntity = turnoMapper.toEntity(requestDto);
        turnoEntity.setHoraFin(horaFinCalculada);
        turnoEntity.setUsuario(usuario);
        turnoEntity.setServicio(servicio);
        turnoEntity.setEstado(EstadoTurno.PENDIENTE); // El estado por defecto al crearlo

        // 7. Guardar en la BD
        TurnoEntity guardado = turnoRepository.save(turnoEntity);

        return turnoMapper.toDto(guardado);
    }

    @Override
    public List<TurnoResponceDto> obtenerTodos ()
    {
        return turnoRepository.findAll()
                .stream()
                .map(turnoMapper :: toDto)
                .collect(Collectors.toList());
    }

    @Override
    public TurnoResponceDto obtenerPorId (Long id)
    {
        TurnoEntity turno = turnoRepository.findById(id)
                .orElseThrow(()->new TurnoNotFoundException("Turno no encontrado"));

        return turnoMapper.toDto(turno);
    }

    @Override
    public TurnoResponceDto actualizarTurno(Long id, TurnoRequestDto dto) {

        TurnoEntity turno = turnoRepository.findById(id)
                .orElseThrow(() -> new TurnoNotFoundException("Turno no encontrado"));

        if (turno.getEstado() == EstadoTurno.CANCELADO)
        {
            throw new TurnoAlreadyCancelledException(
                    "No se puede modificar un turno cancelado");
        }

        validarFechaYHora(dto);

        UsuarioEntity usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new UserProfileNotFoundException("Usuario no encontrado"));

        ServicioEntity servicio = servicioRepository.findById(dto.getServicioId())
                .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado"));

        boolean existeDuplicado =
                turnoRepository.existsByUsuario_IdUsuarioAndFechaAndHoraInicio(
                        dto.getUsuarioId(),
                        dto.getFecha(),
                        dto.getHoraInicio());

        if (existeDuplicado &&
                !turno.getUsuario().getIdUsuario().equals(dto.getUsuarioId())) {

            throw new DuplicateTurnException(
                    "Ya existe un turno para ese usuario en ese horario");
        }

        turno.setFecha(dto.getFecha());
        turno.setHoraInicio(dto.getHoraInicio());
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
            throw new TurnFinishedException(
                    "No se puede eliminar un turno finalizado");
        }

        turnoRepository.delete(turno);
    }


    private void validarFechaYHora(TurnoRequestDto dto)
    {
        if(dto.getFecha().isBefore(LocalDate.now()))
        {
            throw new TurnoPastDateException(
                    "No se puede crear un turno en una fecha pasada");
        }

        if(dto.getFecha().equals(LocalDate.now())
                && dto.getHoraInicio().isBefore(LocalTime.now()))
        {
            throw new TurnoPastDateException(
                    "No se puede crear un turno en una hora pasada");
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
