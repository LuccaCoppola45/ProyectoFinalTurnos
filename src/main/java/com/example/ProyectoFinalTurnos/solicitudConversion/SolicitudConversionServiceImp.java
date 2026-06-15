package com.example.ProyectoFinalTurnos.solicitudConversion;

import com.example.ProyectoFinalTurnos.features.comercio.ComercioEntity;
import com.example.ProyectoFinalTurnos.features.comercio.ComercioRepository;
import com.example.ProyectoFinalTurnos.features.usuario.UsuarioEntity;
import com.example.ProyectoFinalTurnos.features.usuario.UsuarioRepository;
import com.example.ProyectoFinalTurnos.model.enums.EstadoSolicitud;
import com.example.ProyectoFinalTurnos.model.enums.Rol;
import com.example.ProyectoFinalTurnos.solicitudConversion.dtos.SolicitudConversionRequest;
import com.example.ProyectoFinalTurnos.solicitudConversion.dtos.SolicitudConversionResponse;
import com.example.ProyectoFinalTurnos.solicitudConversion.mapper.SolicitudConversionMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SolicitudConversionServiceImp implements SolicitudConversionService {

    private final SolicitudConversionRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final ComercioRepository comercioRepository;
    private final SolicitudConversionMapper mapper;  // ← añadir

    @Override
    public SolicitudConversionResponse crearSolicitud(SolicitudConversionRequest request, String emailUsuario) {
        UsuarioEntity usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        boolean existePendiente = repository.findByEstado(EstadoSolicitud.PENDIENTE)
                .stream().anyMatch(s -> s.getUsuario().getIdUsuario().equals(usuario.getIdUsuario()));
        if (existePendiente) {
            throw new IllegalStateException("Ya existe una solicitud pendiente para este usuario");
        }

        // ← Usar mapper para convertir DTO a entidad
        SolicitudConversionEntity s = mapper.toEntity(request);
        s.setUsuario(usuario);
        s.setEstado(EstadoSolicitud.PENDIENTE);
        repository.save(s);

        // ← Usar mapper para convertir entidad a DTO
        return mapper.toDto(s);
    }

    @Override
    public List<SolicitudConversionResponse> listarSolicitudesPendientes() {
        return repository.findByEstado(EstadoSolicitud.PENDIENTE)
                .stream()
                .map(mapper::toDto)  // ← usar mapper
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SolicitudConversionResponse aprobarSolicitud(Long idSolicitud, String emailAdmin) {
        SolicitudConversionEntity s = repository.findById(idSolicitud)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        if (s.getEstado() != EstadoSolicitud.PENDIENTE) {
            throw new IllegalStateException("La solicitud ya fue procesada");
        }

        UsuarioEntity usuario = s.getUsuario();

        ComercioEntity comercio = new ComercioEntity();
        comercio.setNombre(s.getNombreComercioPropuesto());
        comercio.setDireccion(s.getDireccion());
        comercio.setTelefono(s.getTelefono());
        comercio.setDescripcion(s.getDescripcion());
        comercio.setUsuarioPropietario(usuario);


        ComercioEntity guardado = comercioRepository.save(comercio);

        usuario.setRol(Rol.COMERCIO);
        usuarioRepository.save(usuario);

        s.setEstado(EstadoSolicitud.APROBADA);
        repository.save(s);

        // ← Usar mapper para convertir a DTO
        return mapper.toDto(s);
    }

    @Override
    public SolicitudConversionResponse rechazarSolicitud(Long idSolicitud, String emailAdmin, String motivo) {
        SolicitudConversionEntity s = repository.findById(idSolicitud)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        if (s.getEstado() != EstadoSolicitud.PENDIENTE) {
            throw new IllegalStateException("La solicitud ya fue procesada");
        }

        s.setEstado(EstadoSolicitud.RECHAZADA);
        repository.save(s);

        // ← Usar mapper para convertir a DTO
        return mapper.toDto(s);
    }

}