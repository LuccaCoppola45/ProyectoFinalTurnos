package com.example.ProyectoFinalTurnos.features.servicio;

import com.example.ProyectoFinalTurnos.features.servicio.Dtos.ServicioRequest;
import com.example.ProyectoFinalTurnos.features.servicio.Dtos.ServicioResponse;

import java.util.List;

public interface IServicioService {

    ServicioResponse crearServicio(ServicioRequest request);

    List<ServicioResponse> listarServicios();

    ServicioResponse  BuscarporId(long id);

    void eliminarServicio(long id);

    ServicioResponse ActualizarServicio(long id, ServicioRequest request);

}
