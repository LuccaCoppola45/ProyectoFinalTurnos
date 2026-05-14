package com.example.ProyectoFinalTurnos.features.usuario.Dtos;


import com.example.ProyectoFinalTurnos.model.enums.Rol;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class UsuarioResponce
{
    private Long idUsuario;
    private String nombre;
    private String apellido;
    private Rol rol;
    private String email;

    private List<Long>serviciosId;
    private List<Long>turnosIds;
    private List<Long>cancelacionesIds;

}
