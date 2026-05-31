package com.example.ProyectoFinalTurnos.features.turno;


import com.example.ProyectoFinalTurnos.features.servicio.ServicioEntity;
import com.example.ProyectoFinalTurnos.features.servicio.ServicioRepository;
import com.example.ProyectoFinalTurnos.features.turno.Dtos.TurnoRequestDto;
import com.example.ProyectoFinalTurnos.features.turno.Dtos.TurnoResponceDto;
import com.example.ProyectoFinalTurnos.features.turno.Mapper.TurnoMapper;
import com.example.ProyectoFinalTurnos.features.usuario.UsuarioEntity;
import com.example.ProyectoFinalTurnos.features.usuario.UsuarioRepository;
import org.springframework.stereotype.Service;


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
        UsuarioEntity usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(()-> new RuntimeException("Usuario no encontrado"));

        ServicioEntity servicio = servicioRepository.findById(dto.getServicioId())
                .orElseThrow(()->new RuntimeException("Servicio no encontrado"));

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
    public void eliminarUnTurno (Long id)
    {
        if(!turnoRepository.existsById(id))
        {
            throw new RuntimeException("Turno no encontrado");
        }

        turnoRepository.deleteById(id);
    }





}
