package com.example.ProyectoFinalTurnos.features.horarioAtencion;

import com.example.ProyectoFinalTurnos.features.comercio.ComercioEntity;
import com.example.ProyectoFinalTurnos.features.comercio.ComercioRepository;
import com.example.ProyectoFinalTurnos.features.horarioAtencion.Dtos.HorarioAtencionRequest;
import com.example.ProyectoFinalTurnos.features.horarioAtencion.Dtos.HorarioAtencionResponse;
import com.example.ProyectoFinalTurnos.features.horarioAtencion.Mapper.HorarioAtencionMapper;
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
        public HorarioAtencionResponse crearHorariotencion(HorarioAtencionRequest request) {// 1. Busca el comercio (Este es el primer SELECT de tu consola, el que SÍ tiene que estar)
        ComercioEntity comercio = comercioRepository.findById(request.getComercioIdRequest())
                .orElseThrow(() -> new RuntimeException("Comercio no encontrado con ID: " + request.getComercioIdRequest()));

        // 2. Mapea plano usando el Mapper configurado
        HorarioAtencionEntity horarioEntity = mapper.toEntity(request);

        horarioEntity.setId(null); // Aseguramos que el ID sea null para que se genere uno nuevo al guardar

        // 3. Le asociamos el comercio real que encontramos arriba
        horarioEntity.setComercio(comercio);

        // 4. Guarda en la BD (Acá va a meter el INSERT de una)
        HorarioAtencionEntity guardado = repository.save(horarioEntity);

        // 5. Devuelve la respuesta automática con ModelMapper
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
