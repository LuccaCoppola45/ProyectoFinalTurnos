package com.example.ProyectoFinalTurnos.features.comercio;

import com.example.ProyectoFinalTurnos.features.comercio.Dtos.ComercioRequest;
import com.example.ProyectoFinalTurnos.features.comercio.Dtos.ComercioResponse;

import java.util.List;

public interface IComercioService {
    ComercioResponse crearComercio(ComercioRequest request);

    List<ComercioResponse> listarComercioS();

    ComercioResponse buscarPorId(Long id);

    ComercioResponse actualizarComercio(Long id, ComercioRequest request);

    List<ComercioResponse> comerciosPorCategoria(Long categoriaId);

    void eliminarComercio(Long id);
}

