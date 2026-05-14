package com.example.ProyectoFinalTurnos.features.usuario;


import com.example.ProyectoFinalTurnos.features.usuario.Dtos.UsuarioRequest;
import com.example.ProyectoFinalTurnos.features.usuario.Dtos.UsuarioResponce;

import java.util.List;

public interface UsuarioService
{
    public UsuarioResponce crearUsuario(UsuarioRequest dto);
    public List<UsuarioResponce> obtenerTodos();
    public UsuarioResponce obtenerPorId (Long id);
    public void eliminarUsuario (Long id);
}
