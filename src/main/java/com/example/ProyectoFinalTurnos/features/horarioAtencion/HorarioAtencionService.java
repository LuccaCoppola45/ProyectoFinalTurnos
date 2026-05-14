package com.example.ProyectoFinalTurnos.features.horarioAtencion;

import com.example.ProyectoFinalTurnos.features.horarioAtencion.Dtos.HorarioAtencionRequest;
import com.example.ProyectoFinalTurnos.features.horarioAtencion.Dtos.HorarioAtencionResponse;
import com.example.ProyectoFinalTurnos.features.horarioAtencion.Mapper.HorarioAtencionMapper;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class HorarioAtencionService implements IHorarioAtencionService{

    private final HorarioAtencionRepository repository;
    private final HorarioAtencionMapper mapper;


    public HorarioAtencionService(HorarioAtencionRepository repository, HorarioAtencionMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }




    @Override
        public HorarioAtencionResponse crearHorariotencion(HorarioAtencionRequest request) {
        HorarioAtencionEntity horarioAtencion = mapper.toEntity(request);

        HorarioAtencionEntity guardado = repository.save(horarioAtencion);

        return mapper.toDto(guardado);
        }

        @Override
        public List<HorarioAtencionResponse> ListarHorarios(){
            return repository.findAll().stream()
                    .map(mapper::toDto)
                    .toList();
        }


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
        horarioAtencion.setDiaSemana(request.getDia());
        horarioAtencion.setHoraApertura(request.getApertura());
        horarioAtencion.setHoraCierre(request.getCierre());

        HorarioAtencionEntity actualizado = repository.save(horarioAtencion);
        return mapper.toDto(actualizado);
        }
}
