package com.example.ProyectoFinalTurnos.features.usuario.Dtos;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserResponse {

    boolean success;
    String message;
    UsuarioResponce data;
}
