package com.example.ProyectoFinalTurnos.features.usuario.Dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor


public class UsuarioRequest
{

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3)
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 3)
    private String apellido;

    @NotBlank(message = "El rol es obligatorio")
    private String rol;

    @NotBlank(message = "El email es obligatorio")
    @Size(min = 3)
    private String email;

    @NotBlank(message = "La contraseñaes obligatoria")
    @Size(min = 6)
    private String password;

    private List<Long> serviciosIds;
}
