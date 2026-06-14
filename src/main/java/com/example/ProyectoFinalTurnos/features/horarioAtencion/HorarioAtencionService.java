package com.example.ProyectoFinalTurnos.features.horarioAtencion;

import com.example.ProyectoFinalTurnos.exceptions.auth.ResourceNotFoundException;
import com.example.ProyectoFinalTurnos.exceptions.catalog.ComercioNotFoundException;
import com.example.ProyectoFinalTurnos.exceptions.servicio.ServicioNotFoundException;
import com.example.ProyectoFinalTurnos.features.comercio.ComercioEntity;
import com.example.ProyectoFinalTurnos.features.comercio.ComercioRepository;
import com.example.ProyectoFinalTurnos.features.horarioAtencion.Dtos.HorarioAtencionRequest;
import com.example.ProyectoFinalTurnos.features.horarioAtencion.Dtos.HorarioAtencionResponse;
import com.example.ProyectoFinalTurnos.features.horarioAtencion.Mapper.HorarioAtencionMapper;
import com.example.ProyectoFinalTurnos.features.servicio.ServicioEntity;
import com.example.ProyectoFinalTurnos.features.servicio.ServicioRepository;
import com.example.ProyectoFinalTurnos.model.enums.EstadoTurno;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class HorarioAtencionService implements IHorarioAtencionService{

    private final HorarioAtencionRepository repository;
    private final HorarioAtencionMapper mapper;
    private final ComercioRepository comercioRepository;


    public HorarioAtencionService(HorarioAtencionRepository repository, HorarioAtencionMapper mapper, ComercioRepository comercioRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.comercioRepository = comercioRepository;

    }




    @Override
    public HorarioAtencionResponse crearHorariotencion(HorarioAtencionRequest request) {
        // Validación básica de tiempo
        if (request.getHoraApertura().isAfter(request.getHoraCierre()) ||
                request.getHoraApertura().equals(request.getHoraCierre())) {
            throw new IllegalArgumentException("Rango de horas inválido. Apertura debe ser anterior al cierre.");
        }

        // El comercio es OBLIGATORIO ahora (no hay alternativa con servicio)
        if (request.getComercioId() == null) {
            throw new IllegalArgumentException("Debes especificar un ID de comercio.");
        }

        ComercioEntity comercio = comercioRepository.findById(request.getComercioId())
                .orElseThrow(() -> new ComercioNotFoundException("Comercio no encontrado con ID: " + request.getComercioId()));

        // Validar solapamiento con OTRO HORARIO del mismo comercio para ese día
        boolean seSolapa = repository.findByComercio_IdComercioAndDiaSemana(comercio.getIdComercio(), request.getDiaSemana())
                .stream()
                .anyMatch(h -> request.getHoraApertura().isBefore(h.getHoraCierre()) &&
                        request.getHoraCierre().isAfter(h.getHoraApertura()));

        if (seSolapa) {
            throw new IllegalArgumentException("Ya existe un horario para este comercio que se solapa en el día " + request.getDiaSemana());
        }

        // Mapeo y guardado
        HorarioAtencionEntity horarioEntity = mapper.toEntity(request);
        horarioEntity.setIdHorarioAtencion(null);
        horarioEntity.setComercio(comercio);

        HorarioAtencionEntity guardado = repository.save(horarioEntity);
        return mapper.toDto(guardado);
    }



        @Override
        public List<HorarioAtencionResponse> ListarHorarios(){
            return repository.findAll().stream()
                    .map(mapper::toDto)
                    .toList();
        }

        //

        @Override
        public HorarioAtencionResponse listarHorarios(long id) {
        HorarioAtencionEntity horarioAtencion = repository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Horario de atención no encontrado con id: " + id));
        return mapper.toDto(horarioAtencion);
        }


        @Override
        public void EliminarHorarioAtencion(long id) {
        HorarioAtencionEntity horarioAtencion = repository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Horario de atención no encontrado con id: " + id));
        repository.delete(horarioAtencion);
        }

    @Override
    public HorarioAtencionResponse ActualizarHorarioAtencion(long id, HorarioAtencionRequest request) {
        HorarioAtencionEntity horarioAtencion = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Horario de atención no encontrado con id: " + id));

        // Validar rango horario si viene en el request
        if (request.getHoraApertura() != null && request.getHoraCierre() != null) {
            if (request.getHoraApertura().isAfter(request.getHoraCierre()) ||
                    request.getHoraApertura().equals(request.getHoraCierre())) {
                throw new IllegalArgumentException("Rango de horas inválido. Apertura debe ser anterior al cierre.");
            }
            horarioAtencion.setHoraApertura(request.getHoraApertura());
            horarioAtencion.setHoraCierre(request.getHoraCierre());
        }

        if (request.getDiaSemana() != null) {
            horarioAtencion.setDiaSemana(request.getDiaSemana());
        }

        // Si viene comercioId: lo asignamos
        if (request.getComercioId() != null) {
            ComercioEntity comercio = comercioRepository.findById(request.getComercioId())
                    .orElseThrow(() -> new ComercioNotFoundException("Comercio no encontrado con ID: " + request.getComercioId()));
            horarioAtencion.setComercio(comercio);
        }
        // Si no viene comercioId: dejamos el comercio actual (no tocamos)

        HorarioAtencionEntity actualizado = repository.save(horarioAtencion);
        return mapper.toDto(actualizado);
    }


    @Override
    public HorarioAtencionResponse ObtenerHorarioPorId(long id) {
        HorarioAtencionEntity horarioAtencion = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Horario de atención no encontrado con id: " + id));
        return mapper.toDto(horarioAtencion);
    }


}
