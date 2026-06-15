package com.example.ProyectoFinalTurnos.features.comercio.Dtos;

import com.example.ProyectoFinalTurnos.features.horarioAtencion.Dtos.HorarioAtencionResponse;
import com.example.ProyectoFinalTurnos.features.servicio.Dtos.ServicioResponse;
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

    private String descripcion;

    private List<String> categorias;

    private List<ServicioResponse> servicios;

    private List<HorarioAtencionResponse> horarios;
    
    private Long usuarioPropietarioId;
}
