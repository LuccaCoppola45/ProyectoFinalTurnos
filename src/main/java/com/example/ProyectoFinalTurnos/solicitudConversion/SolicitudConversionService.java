package com.example.ProyectoFinalTurnos.solicitudConversion;

import com.example.ProyectoFinalTurnos.solicitudConversion.dtos.SolicitudConversionRequest;
import com.example.ProyectoFinalTurnos.solicitudConversion.dtos.SolicitudConversionResponse;

import java.util.List;

public interface SolicitudConversionService
{
    SolicitudConversionResponse crearSolicitud(SolicitudConversionRequest request, String emailUsuario);
    List<SolicitudConversionResponse> listarSolicitudesPendientes();
    SolicitudConversionResponse aprobarSolicitud(Long idSolicitud, String emailAdmin);
    SolicitudConversionResponse rechazarSolicitud(Long idSolicitud, String emailAdmin, String motivo);

}
