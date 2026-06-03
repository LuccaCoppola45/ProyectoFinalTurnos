package com.example.ProyectoFinalTurnos.features.horarioAtencion;

import com.example.ProyectoFinalTurnos.exceptions.catalog.ComercioNotFoundException;
import com.example.ProyectoFinalTurnos.features.comercio.ComercioEntity;
import com.example.ProyectoFinalTurnos.features.comercio.ComercioRepository;
import com.example.ProyectoFinalTurnos.features.horarioAtencion.Dtos.HorarioAtencionRequest;
import com.example.ProyectoFinalTurnos.features.horarioAtencion.Dtos.HorarioAtencionResponse;
import com.example.ProyectoFinalTurnos.features.horarioAtencion.Mapper.HorarioAtencionMapper;
import com.example.ProyectoFinalTurnos.features.servicio.ServicioEntity;
import com.example.ProyectoFinalTurnos.features.servicio.ServicioRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
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
        if (request.getServicioIdRequest() != null) {
            servicio = servicioRepository.findById(request.getServicioIdRequest())
                    .orElseThrow(() -> new RuntimeException("Servicio no encontrado con ID: " + request.getServicioIdRequest()));

            comercio = servicio.getComercio();

            // TRAEMOS TODOS LOS HORARIOS DE ESTE COMERCIO PARA EL DÍA (Tanto del local como de otros empleados)
            List<HorarioAtencionEntity> horariosDelDia = repository.findByComercioIdAndDiaSemana(comercio.getId(), request.getDiaSemana());

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
                    .filter(h -> h.getServicio() != null && h.getServicio().getIdServicio().equals(request.getServicioIdRequest()))
                    .anyMatch(h -> request.getHoraApertura().isBefore(h.getHoraCierre()) &&
                            request.getHoraCierre().isAfter(h.getHoraApertura()));

            if (seSolapaConMismoPeluquero) {
                throw new IllegalArgumentException("Este peluquero ya tiene un bloque horario que se solapa para el día " + request.getDiaSemana());
            }

            // 3. Si viene solo el ID de comercio, es el horario GENERAL DEL LOCAL
        } else if (request.getComercioIdRequest() != null) {
            comercio = comercioRepository.findById(request.getComercioIdRequest())
                    .orElseThrow(() -> new ComercioNotFoundException("Comercio no encontrado con ID: " + request.getComercioIdRequest()));

            // STREAMS: Validar solapamiento con otro horario general del local
            boolean seSolapaHorarioComercio = repository.findByComercioIdAndDiaSemana(comercio.getId(), request.getDiaSemana())
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
        horarioEntity.setId(null);
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
                orElseThrow(() -> new RuntimeException("Horario de atención no encontrado con id: " + id));
        return mapper.toDto(horarioAtencion);
        }


        @Override
        public void EliminarHorarioAtencion(long id) {
        HorarioAtencionEntity horarioAtencion = repository.findById(id).
                orElseThrow(() -> new RuntimeException("Horario de atención no encontrado con id: " + id));
        repository.delete(horarioAtencion);
        }

        @Override
        public HorarioAtencionResponse ActualizarHorarioAtencion(long id, HorarioAtencionRequest request) {
        HorarioAtencionEntity horarioAtencion = repository.findById(id).
                orElseThrow(() -> new RuntimeException("Horario de atención no encontrado con id: " + id));
        horarioAtencion.setDiaSemana(request.getDiaSemana());
        horarioAtencion.setHoraApertura(request.getHoraApertura());
        horarioAtencion.setHoraCierre(request.getHoraCierre());

        HorarioAtencionEntity actualizado = repository.save(horarioAtencion);
        return mapper.toDto(actualizado);
        }


}
