package com.example.ProyectoFinalTurnos.features.comercio.Dtos;

import com.example.ProyectoFinalTurnos.features.servicio.ServicioEntity;
import lombok.Data;

import java.util.List;


@Data
public class ComercioResponse
{
    private Long idComercio;

    private String nombre;

    private String direccion;

    private String telefono;

    private List<String> categorias;

    private List<Long> serviciosIds;

    private List<Long> horariosIds;
}
