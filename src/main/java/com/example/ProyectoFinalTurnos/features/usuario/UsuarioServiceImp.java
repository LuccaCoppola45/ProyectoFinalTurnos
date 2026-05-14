package com.example.ProyectoFinalTurnos.features.usuario;


import com.example.ProyectoFinalTurnos.features.servicio.ServicioEntity;
import com.example.ProyectoFinalTurnos.features.servicio.ServicioRepository;
import com.example.ProyectoFinalTurnos.features.usuario.Dtos.UsuarioRequest;
import com.example.ProyectoFinalTurnos.features.usuario.Dtos.UsuarioResponce;
import com.example.ProyectoFinalTurnos.features.usuario.Mapper.UsuarioMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImp implements UsuarioService
{

    private final UsuarioRepository usuarioRepository;
    private final ServicioRepository servicioRepository;
    private final UsuarioMapper usuarioMapper;

    /// -Crear Usuario- ///
    @Override
    public UsuarioResponce crearUsuario(UsuarioRequest dto)
    {
        List<ServicioEntity>servicio = servicioRepository.findAllById(dto.getServiciosIds());
        UsuarioEntity usuario = usuarioMapper.toEntity(dto, servicio);
        UsuarioEntity guardado = usuarioRepository.save(usuario);

        return usuarioMapper.toResponce(guardado);
    }

    /// -Obtener todos los usuarios- ///
    @Override
    public List<UsuarioResponce> obtenerTodos()
    {
        return usuarioRepository.findAll()
                .stream()
                .map(usuarioMapper::toResponce)
                .collect(Collectors.toList());
    }

    /// -Obtener por Id-///

    @Override
    public UsuarioResponce obtenerPorId (Long id)
    {
        UsuarioEntity usuario = usuarioRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Usuario no encontrado"));
        return usuarioMapper.toResponce(usuario);
    }

    /// -Eliminar Usuario- ///
    @Override
    public void eliminarUsuario (Long id)
    {
        if(!usuarioRepository.existsById(id))
        {
            throw new RuntimeException("Usuario no encontrado");
        }
        usuarioRepository.deleteById(id);
    }







}
