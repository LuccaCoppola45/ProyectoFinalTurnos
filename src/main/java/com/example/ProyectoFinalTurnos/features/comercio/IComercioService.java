package com.example.ProyectoFinalTurnos.features.comercio;

import com.example.ProyectoFinalTurnos.features.comercio.Dtos.ComercioRequest;
import com.example.ProyectoFinalTurnos.features.comercio.Dtos.ComercioResponse;

import java.util.List;

public interface IComercioService {
    ComercioResponse crearComercio(ComercioRequest request);

    List<ComercioResponse> listarComercioS();

    ComercioResponse buscarPorId(long id);

    ComercioResponse actualizarComercio(long id, ComercioRequest request);

    void eliminarComercio(long id);
}

