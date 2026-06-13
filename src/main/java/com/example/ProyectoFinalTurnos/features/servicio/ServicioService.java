package com.example.ProyectoFinalTurnos.features.servicio;

import com.example.ProyectoFinalTurnos.exceptions.auth.ResourceNotFoundException;
import com.example.ProyectoFinalTurnos.exceptions.catalog.ComercioNotFoundException;
import com.example.ProyectoFinalTurnos.exceptions.servicio.ServicioNotFoundException;
import com.example.ProyectoFinalTurnos.features.comercio.ComercioEntity;
import com.example.ProyectoFinalTurnos.features.comercio.ComercioRepository;
import com.example.ProyectoFinalTurnos.features.horarioAtencion.HorarioAtencionEntity;
import com.example.ProyectoFinalTurnos.features.horarioAtencion.HorarioAtencionRepository;
import com.example.ProyectoFinalTurnos.features.servicio.Dtos.ServicioRequest;
import com.example.ProyectoFinalTurnos.features.servicio.Dtos.ServicioResponse;
import com.example.ProyectoFinalTurnos.features.servicio.Mapper.ServicioMapper;
import com.example.ProyectoFinalTurnos.features.turno.TurnoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServicioService implements IServicioService{

    private final ServicioRepository servicioRepository;
    private final ServicioMapper servicioMapper;
    private final ComercioRepository comercioRepository;
    private final HorarioAtencionRepository horarioAtencionRepository;
    private final TurnoRepository turnoRepository;

    public ServicioService(ServicioRepository servicioRepository, ServicioMapper servicioMapper, ComercioRepository comercioRepository, HorarioAtencionRepository horarioAtencionRepository, TurnoRepository turnoRepository) {
        this.servicioRepository = servicioRepository;
        this.servicioMapper = servicioMapper;
        this.comercioRepository = comercioRepository;
        this.horarioAtencionRepository = horarioAtencionRepository;
        this.turnoRepository = turnoRepository;
    }


    @Override
    public ServicioResponse crearServicio(ServicioRequest request) {

        ComercioEntity comercio = comercioRepository.findById(request.getIdComercio())
                .orElseThrow(() -> new ComercioNotFoundException("No se encontró el comercio con ID: " + request.getIdComercio()));

        /// evita nombre duplicados para el servicio
        boolean existeNombre = servicioRepository.existsByNombreServicioAndIdComercio(request.getNombreServicio(),request.getIdComercio());
        if(existeNombre) {
            throw new IllegalStateException("Ya existe un servicio con el mismo nombre para este comercio");
        }

        HorarioAtencionEntity horario = horarioAtencionRepository.findById(request.getIdHorario())
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el horario con ID: " + request.getIdHorario()));

        ServicioEntity servicioEntity = servicioMapper.toEntity(request);
        servicioEntity.setIdServicio(null);
        servicioEntity.setComercio(comercio);
        servicioEntity.setHorarios(List.of(horario));

        ServicioEntity guardado = servicioRepository.save(servicioEntity);
        return servicioMapper.toDto(guardado);
    }

    @Override
    public List<ServicioResponse> listarServicios() {
        return servicioRepository.findAll().stream()
                .map(servicioMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ServicioResponse BuscarporId(long id) {
        ServicioEntity servicioEntity = servicioRepository.findById(id)
                .orElseThrow(() -> new ServicioNotFoundException("No se encontró el servicio con ID: " + id));
        return servicioMapper.toDto(servicioEntity);
    }

    @Override
    public void eliminarServicio(long id) {
        ServicioEntity servicioEntity = servicioRepository.findById(id)
                .orElseThrow(() -> new ServicioNotFoundException("No se encontró el servicio con ID: " + id));
        servicioRepository.delete(servicioEntity);
        if (turnoRepository.existsByUsuario_IdUsuario(id)){
            throw new IllegalStateException("No se puede eliminar el servicio porque tiene turnos asociados");
        }

    }

    @Override
    public ServicioResponse ActualizarServicio(long id, ServicioRequest request) {
        ServicioEntity servicioEntity = servicioRepository.findById(id)
                .orElseThrow(() -> new ServicioNotFoundException("No se encontró el servicio con ID: " + id));

        ComercioEntity comercio = comercioRepository.findById(request.getIdComercio())
                .orElseThrow(() -> new ComercioNotFoundException("No se encontró el comercio con ID: " + request.getIdComercio()));

        HorarioAtencionEntity horario = horarioAtencionRepository.findById(request.getIdHorario())
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el horario con ID: " + request.getIdHorario()));

        servicioEntity.setNombre(request.getNombreServicio());
        servicioEntity.setPrecio(request.getPrecio());
        servicioEntity.setDuracionDelServicio(request.getDuracionDelServicio());
        servicioEntity.setComercio(comercio);
        servicioEntity.setHorarios(List.of(horario));

        ServicioEntity actualizado = servicioRepository.save(servicioEntity);
        return servicioMapper.toDto(actualizado);
    }
}
