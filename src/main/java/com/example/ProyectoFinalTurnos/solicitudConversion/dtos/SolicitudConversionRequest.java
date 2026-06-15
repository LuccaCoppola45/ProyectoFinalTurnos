package com.example.ProyectoFinalTurnos.solicitudConversion.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudConversionRequest
{
    @NotBlank(message = "El nombre del comercio propuesto no puede estar vacío")
    private String nombreComercioPropuesto;

    @NotBlank(message = "La direccion del comercio no puede esta vacia")
    private String direccion;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "^[0-9+\\- ]+$", message = "El teléfono contiene caracteres inválidos")
    private String telefono;

    @NotBlank(message = "La descripcion de la solicitud no puede estar vacia")
    private String descripcion;

}
