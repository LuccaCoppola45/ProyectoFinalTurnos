package com.example.ProyectoFinalTurnos.features.horarioAtencion;

import com.example.ProyectoFinalTurnos.features.horarioAtencion.Dtos.HorarioAtencionRequest;
import com.example.ProyectoFinalTurnos.features.horarioAtencion.Dtos.HorarioAtencionResponse;

import java.util.List;

public interface IHorarioAtencionService {


    HorarioAtencionResponse crearHorariotencion(HorarioAtencionRequest request);

    List<HorarioAtencionResponse> ListarHorarios();

    HorarioAtencionResponse listarHorarios(long id);

    void EliminarHorarioAtencion(long id);

    HorarioAtencionResponse ActualizarHorarioAtencion(long id, HorarioAtencionRequest request);
}
