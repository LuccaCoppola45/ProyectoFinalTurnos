package com.example.ProyectoFinalTurnos.features.usuario;


import com.example.ProyectoFinalTurnos.exceptions.user.UserEmailAlreadyInUseException;
import com.example.ProyectoFinalTurnos.exceptions.user.UserHasAssociatedTurnosException;
import com.example.ProyectoFinalTurnos.exceptions.user.UserProfileNotFoundException;
import com.example.ProyectoFinalTurnos.exceptions.user.UserWithoutChangesException;
import com.example.ProyectoFinalTurnos.features.turno.TurnoRepository;
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
    private final TurnoRepository turnoRepository;


    /// -Crear Usuario- ///
    @Override
    public UsuarioResponce crearUsuario(UsuarioRequest dto)
    {
        String email = dto.getEmail().trim().toLowerCase();
        if (usuarioRepository.existsByEmail(email))
        {
            throw new UserEmailAlreadyInUseException("Ya existe un usuario con ese email");
        }
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
                .orElseThrow(()-> new UserProfileNotFoundException("Usuario no encontrado"));
        return usuarioMapper.toResponce(usuario);
    }

    /// -Eliminar Usuario- ///
    @Override
    public void eliminarUsuario (Long id)
    {
        if(!usuarioRepository.existsById(id))
        {
            throw new UserProfileNotFoundException("Usuario no encontrado");
        }
        if(turnoRepository.existsByUsuario_IdUsuario(id))
        {
            throw new UserHasAssociatedTurnosException(
                    "No se puede eliminar un usuario con turnos asociados");
        }

        usuarioRepository.deleteById(id);
    }

    @Override
    @Transactional
    public UpdateUserResponse actualizarUsuario(Long id, UpdateUserRequestDTO dto) {
        UsuarioEntity user = usuarioRepository.findById(id)
                .orElseThrow(()-> new UserProfileNotFoundException("Usuario no encontrado"));

        String nuevoEmail = dto.getEmail().trim().toLowerCase();

        if(!user.getEmail().equalsIgnoreCase(nuevoEmail)
                && usuarioRepository.existsByEmail(nuevoEmail))
        {
            throw new UserEmailAlreadyInUseException(
                    "Ya existe un usuario con ese email");
        }
        if(user.getNombre().equals(dto.getNombre())
                && user.getApellido().equals(dto.getApellido())
                && user.getEmail().equalsIgnoreCase(nuevoEmail))
        {
            throw new UserWithoutChangesException(
                    "No se detectaron cambios para actualizar");
        }
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
