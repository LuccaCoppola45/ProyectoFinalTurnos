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
    private final ServicioRepository servicioRepository;

    public HorarioAtencionService(HorarioAtencionRepository repository, HorarioAtencionMapper mapper,ServicioRepository servicioRepository, ComercioRepository comercioRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.comercioRepository = comercioRepository;
        this.servicioRepository = servicioRepository;
    }




    @Override
    public HorarioAtencionResponse crearHorariotencion(HorarioAtencionRequest request) {
        // 1. Validaciones básicas de tiempo
        if (request.getHoraApertura().isAfter(request.getHoraCierre()) ||
                request.getHoraApertura().equals(request.getHoraCierre())) {
            throw new IllegalArgumentException("Rango de horas inválido. Apertura debe ser anterior al cierre.");
        }

        ComercioEntity comercio = null;
        ServicioEntity servicio = null;

        // 2. Si viene un ID de servicio, es horario de un PELUQUERO
        if (request.getServicioId() != null) {
            servicio = servicioRepository.findById(request.getServicioId())
                    .orElseThrow(() -> new ServicioNotFoundException("Servicio no encontrado con ID: " + request.getServicioId()));

            comercio = servicio.getComercio();

            // TRAEMOS TODOS LOS HORARIOS DE ESTE COMERCIO PARA EL DÍA (Tanto del local como de otros empleados)
            List<HorarioAtencionEntity> horariosDelDia = repository.findByComercio_IdComercioAndDiaSemana(comercio.getIdComercio(), request.getDiaSemana());

            // STREAMS: Validar que el horario solicitado esté DENTRO del horario general del comercio (servicio == null)
            boolean dentroDeHorarioComercio = horariosDelDia.stream()
                    .filter(h -> h.getServicio() == null) // Filtramos para quedarnos con el horario del local
                    .anyMatch(hc -> (request.getHoraApertura().isAfter(hc.getHoraApertura()) || request.getHoraApertura().equals(hc.getHoraApertura())) &&
                            (request.getHoraCierre().isBefore(hc.getHoraCierre()) || request.getHoraCierre().equals(hc.getHoraCierre())));

            if (!dentroDeHorarioComercio) {
                throw new IllegalArgumentException("El peluquero no puede atender fuera del horario de apertura del comercio o el local está cerrado este día.");
            }

            // STREAMS: Validar que este peluquero específico no tenga solapamiento
            boolean seSolapaConMismoPeluquero = horariosDelDia.stream()
                    .filter(h -> h.getServicio() != null && h.getServicio().getIdServicio().equals(request.getServicioId()))
                    .anyMatch(h -> request.getHoraApertura().isBefore(h.getHoraCierre()) &&
                            request.getHoraCierre().isAfter(h.getHoraApertura()));

            if (seSolapaConMismoPeluquero) {
                throw new IllegalArgumentException("Este peluquero ya tiene un bloque horario que se solapa para el día " + request.getDiaSemana());
            }

            // 3. Si viene solo el ID de comercio, es el horario GENERAL DEL LOCAL
        } else if (request.getComercioId() != null) {
            comercio = comercioRepository.findById(request.getComercioId())
                    .orElseThrow(() -> new ComercioNotFoundException("Comercio no encontrado con ID: " + request.getComercioId()));

            // STREAMS: Validar solapamiento con otro horario general del local
            boolean seSolapaHorarioComercio = repository.findByComercio_IdComercioAndDiaSemana(comercio.getIdComercio(), request.getDiaSemana())
                    .stream()
                    .filter(h -> h.getServicio() == null) // Solo comparamos contra el horario general
                    .anyMatch(h -> request.getHoraApertura().isBefore(h.getHoraCierre()) &&
                            request.getHoraCierre().isAfter(h.getHoraApertura()));

            if (seSolapaHorarioComercio) {
                throw new IllegalArgumentException("Ya existe un horario general para el comercio que se solapa en el día " + request.getDiaSemana());
            }

        } else {
            throw new IllegalArgumentException("Debes especificar un ID de comercio o un ID de servicio.");
        }

        // 4. Mapeo y guardado definitivo
        HorarioAtencionEntity horarioEntity = mapper.toEntity(request);
        horarioEntity.setIdHorarioAtencion(null);
        horarioEntity.setComercio(comercio);
        horarioEntity.setServicio(servicio);

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

        // Validaciones básicas del request sobre horas (si corresponde)
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

        // Si viene servicioId en el request: asignamos ese servicio y derivamos comercio desde el servicio
        if (request.getServicioId() != null) {
            ServicioEntity servicio = servicioRepository.findById(request.getServicioId())
                    .orElseThrow(() -> new ServicioNotFoundException("Servicio no encontrado con ID: " + request.getServicioId()));
            horarioAtencion.setServicio(servicio);
            horarioAtencion.setComercio(servicio.getComercio());
        } else if (request.getComercioId() != null) {
            // Si viene solo comercioId: asignamos comercio y dejamos servicio en null (horario general del local)
            ComercioEntity comercio = comercioRepository.findById(request.getComercioId())
                    .orElseThrow(() -> new ComercioNotFoundException("Comercio no encontrado con ID: " + request.getComercioId()));
            horarioAtencion.setComercio(comercio);
            horarioAtencion.setServicio(null);
        }
        // Si no viene ni servicioId ni comercioId, dejamos la asociación previa (no tocamos)

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
