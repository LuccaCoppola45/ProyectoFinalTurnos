package com.example.ProyectoFinalTurnos.features.turno;


import com.example.ProyectoFinalTurnos.features.servicio.ServicioEntity;
import com.example.ProyectoFinalTurnos.features.servicio.ServicioRepository;
import com.example.ProyectoFinalTurnos.features.turno.Dtos.TurnoRequestDto;
import com.example.ProyectoFinalTurnos.features.turno.Dtos.TurnoResponceDto;
import com.example.ProyectoFinalTurnos.features.turno.Mapper.TurnoMapper;
import com.example.ProyectoFinalTurnos.features.usuario.UsuarioEntity;
import com.example.ProyectoFinalTurnos.features.usuario.UsuarioRepository;
import com.example.ProyectoFinalTurnos.model.enums.EstadoTurno;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
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
    public TurnoResponceDto crearTurno(TurnoRequestDto dto)
    {
        validarFechaYHora(dto);

        UsuarioEntity usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(()-> new RuntimeException("Usuario no encontrado"));

        ServicioEntity servicio = servicioRepository.findById(dto.getServicioId())
                .orElseThrow(()->new RuntimeException("Servicio no encontrado"));

        if (turnoRepository.existsByUsuario_IdUsuarioAndFechaAndHoraInicio(
                dto.getUsuarioId(),
                dto.getFecha(),
                dto.getHoraInicio())) {

            throw new RuntimeException(
                    "El usuario ya tiene un turno en ese horario");
        }

        TurnoEntity turno = turnoMapper.toEntity(dto, usuario, servicio);

        TurnoEntity guardado = turnoRepository.save(turno);

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
                .orElseThrow(()->new RuntimeException("Turno no encontrado"));

        return turnoMapper.toDto(turno);
    }

    @Override
    public TurnoResponceDto actualizarTurno(Long id, TurnoRequestDto dto) {

        TurnoEntity turno = turnoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Turno no encontrado"));

        if (turno.getEstado() == EstadoTurno.CANCELADO)
        {
            throw new RuntimeException(
                    "No se puede modificar un turno cancelado");
        }

        validarFechaYHora(dto);

        UsuarioEntity usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        ServicioEntity servicio = servicioRepository.findById(dto.getServicioId())
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));

        boolean existeDuplicado =
                turnoRepository.existsByUsuario_IdUsuarioAndFechaAndHoraInicio(
                        dto.getUsuarioId(),
                        dto.getFecha(),
                        dto.getHoraInicio());

        if (existeDuplicado &&
                !turno.getUsuario().getIdUsuario().equals(dto.getUsuarioId())) {

            throw new RuntimeException(
                    "Ya existe un turno para ese usuario en ese horario");
        }

        turno.setFecha(dto.getFecha());
        turno.setHoraInicio(dto.getHoraInicio());
        turno.setEstado(dto.getEstado());
        turno.setUsuario(usuario);
        turno.setServicio(servicio);

        TurnoEntity actualizado = turnoRepository.save(turno);

        return turnoMapper.toDto(actualizado);
    }

    @Override
    public void eliminarUnTurno(Long id) {

        TurnoEntity turno = turnoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Turno no encontrado"));

        if (turno.getEstado() == EstadoTurno.FINALIZADO) {
            throw new RuntimeException(
                    "No se puede eliminar un turno finalizado");
        }

        turnoRepository.delete(turno);
    }


    private void validarFechaYHora(TurnoRequestDto dto)
    {
        if(dto.getFecha().isBefore(LocalDate.now()))
        {
            throw new RuntimeException(
                    "No se puede crear un turno en una fecha pasada");
        }

        if(dto.getFecha().equals(LocalDate.now())
                && dto.getHoraInicio().isBefore(LocalTime.now()))
        {
            throw new RuntimeException(
                    "No se puede crear un turno en una hora pasada");
        }
    }


}
