package com.example.ProyectoFinalTurnos.features.horarioAtencion;

import com.example.ProyectoFinalTurnos.exceptions.catalog.ComercioNotFoundException;
import com.example.ProyectoFinalTurnos.features.comercio.ComercioEntity;
import com.example.ProyectoFinalTurnos.features.comercio.ComercioRepository;
import com.example.ProyectoFinalTurnos.features.horarioAtencion.Dtos.HorarioAtencionRequest;
import com.example.ProyectoFinalTurnos.features.horarioAtencion.Dtos.HorarioAtencionResponse;
import com.example.ProyectoFinalTurnos.features.horarioAtencion.Mapper.HorarioAtencionMapper;
import org.springframework.stereotype.Service;

import java.time.Duration;
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
        // Validar que el comercio exista
        ComercioEntity comercio = comercioRepository.findById(request.getComercioIdRequest())
                .orElseThrow(() -> new ComercioNotFoundException("Comercio no encontrado con ID: " + request.getComercioIdRequest()));

        // Validar que la hora de apertura sea anterior a la de cierre
        if (request.getHoraApertura().isAfter(request.getHoraCierre())) {
            throw new IllegalArgumentException("La hora de apertura no puede ser posterior a la hora de cierre");
        }

        // Validar que no sean iguales
        if (request.getHoraApertura().equals(request.getHoraCierre())) {
            throw new IllegalArgumentException("La hora de apertura y cierre no pueden ser iguales");
        }

        // Validar que no exista un horario con solapamiento para el mismo día en el mismo comercio
        boolean existeSolapamiento = repository.findByComercioIdAndDiaSemana(comercio.getId(), request.getDiaSemana())
                .stream()
                .anyMatch(horario -> {
                    return (request.getHoraApertura().isBefore(horario.getHoraCierre()) &&
                            request.getHoraCierre().isAfter(horario.getHoraApertura()));
                });

        if (existeSolapamiento) {
            throw new IllegalArgumentException("Ya existe un horario que se solapa para el día " + request.getDiaSemana() + " en este comercio");
        }



        HorarioAtencionEntity horarioEntity = mapper.toEntity(request);
        horarioEntity.setId(null);
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
