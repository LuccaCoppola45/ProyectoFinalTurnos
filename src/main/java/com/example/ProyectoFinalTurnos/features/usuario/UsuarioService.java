package com.example.ProyectoFinalTurnos.features.usuario;


import com.example.ProyectoFinalTurnos.features.usuario.Dtos.UpdateUserRequestDTO;
import com.example.ProyectoFinalTurnos.features.usuario.Dtos.UpdateUserResponse;
import com.example.ProyectoFinalTurnos.features.usuario.Dtos.UsuarioRequest;
import com.example.ProyectoFinalTurnos.features.usuario.Dtos.UsuarioResponce;

import java.util.List;

public interface UsuarioService
{
     UsuarioResponce crearUsuario(UsuarioRequest dto);
     List<UsuarioResponce> obtenerTodos();
     UsuarioResponce obtenerPorId (Long id);
     void eliminarUsuario (Long id);

    UpdateUserResponse actualizarUsuario(Long id, UpdateUserRequestDTO dto);
}
