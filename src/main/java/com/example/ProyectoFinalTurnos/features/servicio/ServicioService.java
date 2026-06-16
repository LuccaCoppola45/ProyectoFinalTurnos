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
public class ServicioService implements IServicioService {

    private final ServicioRepository servicioRepository;
    private final ServicioMapper servicioMapper;
    private final ComercioRepository comercioRepository;
    private final TurnoRepository turnoRepository;

    public ServicioService(
            ServicioRepository servicioRepository,
            ServicioMapper servicioMapper,
            ComercioRepository comercioRepository,
            TurnoRepository turnoRepository) {
        this.servicioRepository = servicioRepository;
        this.servicioMapper = servicioMapper;
        this.comercioRepository = comercioRepository;
        this.turnoRepository = turnoRepository;
    }

    /**     * Crear un nuevo servicio.     * Los horarios ya no se asocian directamente al servicio.     * Se asocian al comercio.     */
    @Override
    public ServicioResponse crearServicio(ServicioRequest request) {

        ComercioEntity comercio = comercioRepository.findById(request.getIdComercio())
                .orElseThrow(() -> new ComercioNotFoundException(
                        "No se encontró el comercio con ID: " + request.getIdComercio()));

        // Validar que no exista un servicio con el mismo nombre para este comercio
        boolean existeNombre = servicioRepository
                .existsByNombreServicioAndIdComercio(
                        request.getNombreServicio(),
                        request.getIdComercio());

        if (existeNombre) {
            throw new IllegalStateException(
                    "Ya existe un servicio con el mismo nombre para este comercio");
        }

        ServicioEntity servicioEntity = servicioMapper.toEntity(request);
        servicioEntity.setIdServicio(null);
        servicioEntity.setComercio(comercio);
        // NO asignamos horario porque ya no existe esa relación

        ServicioEntity guardado = servicioRepository.save(servicioEntity);
        return servicioMapper.toDto(guardado);
    }

    @Override
    public List<ServicioResponse> listarServicios() {
        return servicioRepository.findAll()
                .stream()
                .map(servicioMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ServicioResponse BuscarporId(long id) {
        ServicioEntity servicioEntity = servicioRepository.findById(id)
                .orElseThrow(() -> new ServicioNotFoundException(
                        "No se encontró el servicio con ID: " + id));
        return servicioMapper.toDto(servicioEntity);
    }



    /**     * Eliminar un servicio.     * Primero validar que no tenga turnos asociados.     * NOTA: El método turnoRepository.existsByUsuario_IdUsuario(id) parece incorrecto.     * Debería ser algo como turnoRepository.existsByServicio_IdServicio(id)     */
    @Override
    public void eliminarServicio(long id) {
        ServicioEntity servicioEntity = servicioRepository.findById(id)
                .orElseThrow(() -> new ServicioNotFoundException(
                        "No se encontró el servicio con ID: " + id));

        // Validar que no tenga turnos asociados antes de eliminar
        if (turnoRepository.existsByServicio_IdServicio(id)) {
            throw new IllegalStateException(
                    "No se puede eliminar el servicio porque tiene turnos asociados");
        }

        servicioRepository.delete(servicioEntity);
    }

    /**     * Actualizar un servicio.     * Los horarios ya no se asocian directamente al servicio.     */
    @Override
    public ServicioResponse ActualizarServicio(long id, ServicioRequest request) {
        ServicioEntity servicioEntity = servicioRepository.findById(id)
                .orElseThrow(() -> new ServicioNotFoundException(
                        "No se encontró el servicio con ID: " + id));

        ComercioEntity comercio = comercioRepository.findById(request.getIdComercio())
                .orElseThrow(() -> new ComercioNotFoundException(
                        "No se encontró el comercio con ID: " + request.getIdComercio()));

        servicioEntity.setNombre(request.getNombreServicio());
        servicioEntity.setPrecio(request.getPrecio());
        servicioEntity.setDuracionDelServicio(request.getDuracionDelServicio());
        servicioEntity.setComercio(comercio);
        // NO asignamos horario porque ya no existe esa relación

        ServicioEntity actualizado = servicioRepository.save(servicioEntity);
        return servicioMapper.toDto(actualizado);
    }

    @Override
    public List<ServicioResponse> serviciosPorComercio(Long comercioId) {
        // Validar que el comercio existe
        if (!comercioRepository.existsById(comercioId)) {
            throw new ComercioNotFoundException(
                    "No se encontró el comercio con ID: " + comercioId);
        }

        return servicioRepository.findByComercio_IdComercio(comercioId)
                .stream()
                .map(servicioMapper::toDto)
                .collect(Collectors.toList());
    }

}
