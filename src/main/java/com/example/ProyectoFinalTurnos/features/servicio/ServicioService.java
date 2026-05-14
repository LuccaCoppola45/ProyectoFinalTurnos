package com.example.ProyectoFinalTurnos.features.servicio;

import com.example.ProyectoFinalTurnos.features.comercio.ComercioEntity;
import com.example.ProyectoFinalTurnos.features.comercio.ComercioRepository;
import com.example.ProyectoFinalTurnos.features.horarioAtencion.HorarioAtencionEntity;
import com.example.ProyectoFinalTurnos.features.horarioAtencion.HorarioAtencionRepository;
import com.example.ProyectoFinalTurnos.features.servicio.Dtos.ServicioRequest;
import com.example.ProyectoFinalTurnos.features.servicio.Dtos.ServicioResponse;
import com.example.ProyectoFinalTurnos.features.servicio.Mapper.ServicioMapper;

import java.util.List;
import java.util.stream.Collectors;

public class ServicioService implements IServicioService{

    private final ServicioRepository servicioRepository;
    private final ServicioMapper servicioMapper;
    private final ComercioRepository comercioRepository;
    private final HorarioAtencionRepository horarioAtencionRepository;

    public ServicioService(ServicioRepository servicioRepository, ServicioMapper servicioMapper, ComercioRepository comercioRepository, HorarioAtencionRepository horarioAtencionRepository) {
        this.servicioRepository = servicioRepository;
        this.servicioMapper = servicioMapper;
        this.comercioRepository = comercioRepository;
        this.horarioAtencionRepository = horarioAtencionRepository;
    }


    @Override
    public ServicioResponse crearServicio(ServicioRequest request) {

        ComercioEntity comercio = comercioRepository.findById(request.getIdComercio())
                .orElseThrow(() -> new RuntimeException("No se encontró el comercio con ID: " + request.getIdComercio()));


        HorarioAtencionEntity horario = horarioAtencionRepository.findById(request.getIdHorario())
                .orElseThrow(() -> new RuntimeException("No se encontró el horario con ID: " + request.getIdHorario()));


        ServicioEntity servicioEntity = servicioMapper.toEntity(request);


        servicioEntity.setComercio(comercio);
        servicioEntity.setHorario(horario);


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
                .orElseThrow(() -> new RuntimeException("No se encontró el servicio con ID: " + id));
        return servicioMapper.toDto(servicioEntity);
    }

    @Override
    public void eliminarServicio(long id) {
        ServicioEntity servicioEntity = servicioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontró el servicio con ID: " + id));
        servicioRepository.delete(servicioEntity);
    }

    @Override
    public ServicioResponse ActualizarServicio(long id, ServicioRequest request) {
        ServicioEntity servicioEntity = servicioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontró el servicio con ID: " + id));

        ComercioEntity comercio = comercioRepository.findById(request.getIdComercio())
                .orElseThrow(() -> new RuntimeException("No se encontró el comercio con ID: " + request.getIdComercio()));

        HorarioAtencionEntity horario = horarioAtencionRepository.findById(request.getIdHorario())
                .orElseThrow(() -> new RuntimeException("No se encontró el horario con ID: " + request.getIdHorario()));

        servicioEntity.setNombre(request.getNombreServicio());
        servicioEntity.setPrecio(request.getPrecio());
        servicioEntity.setComercio(comercio);
        servicioEntity.setHorario(horario);

        ServicioEntity actualizado = servicioRepository.save(servicioEntity);
        return servicioMapper.toDto(actualizado);
    }
}
