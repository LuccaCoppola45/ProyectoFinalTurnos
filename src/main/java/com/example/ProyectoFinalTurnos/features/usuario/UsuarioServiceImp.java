package com.example.ProyectoFinalTurnos.features.usuario;


import com.example.ProyectoFinalTurnos.features.usuario.Dtos.UpdateUserRequestDTO;
import com.example.ProyectoFinalTurnos.features.usuario.Dtos.UpdateUserResponse;
import com.example.ProyectoFinalTurnos.features.usuario.Dtos.UsuarioRequest;
import com.example.ProyectoFinalTurnos.features.usuario.Dtos.UsuarioResponce;
import com.example.ProyectoFinalTurnos.features.usuario.Mapper.UsuarioMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImp implements UsuarioService
{

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;



    /// -Crear Usuario- ///
    @Override
    public UsuarioResponce crearUsuario(UsuarioRequest dto)
    {
        UsuarioEntity usuario = usuarioMapper.toEntity(dto);
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

    @Override
    @Transactional
    public UpdateUserResponse actualizarUsuario(Long id, UpdateUserRequestDTO dto) {
        UsuarioEntity user = usuarioRepository.findById(id).orElseThrow(()-> new RuntimeException("Usuario no encontrado"));

        user.setNombre(dto.getNombre());
        user.setApellido(dto.getApellido());
        user.setEmail(dto.getEmail());

        UsuarioEntity usuarioActualizado = usuarioRepository.save(user);


        return UpdateUserResponse.builder()
                .success(true)
                .message("Usuario actualizado correctamente")
                .data(usuarioMapper.toResponce(usuarioActualizado))
                .build();

    }
}
