package com.example.ProyectoFinalTurnos.features.usuario;

import com.example.ProyectoFinalTurnos.features.usuario.Dtos.UpdateUserRequestDTO;
import com.example.ProyectoFinalTurnos.features.usuario.Dtos.UpdateUserResponse;
import com.example.ProyectoFinalTurnos.features.usuario.Dtos.UsuarioRequest;
import com.example.ProyectoFinalTurnos.features.usuario.Dtos.UsuarioResponce;
import jakarta.validation.Valid;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController
{
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService)
    {
        this.usuarioService=usuarioService;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponce> crearUsuario(@Valid @RequestBody UsuarioRequest dto)
    {
        UsuarioResponce usuario = usuarioService.crearUsuario(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(usuario);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponce>> obtenerTodos()
    {
        List<UsuarioResponce> usuarios = usuarioService.obtenerTodos();

        if (usuarios.isEmpty())
        {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponce> obtenerUsuarioPorId(@PathVariable Long id)
    {
        UsuarioResponce usuario = usuarioService.obtenerPorId(id);

        return ResponseEntity.ok(usuario);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id)
    {
        usuarioService.eliminarUsuario(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<@NonNull UpdateUserResponse> updateUser(@PathVariable Long id, @Valid @RequestBody UpdateUserRequestDTO request)
    {
        UpdateUserResponse response =
                usuarioService.actualizarUsuario(id, request);

        return ResponseEntity.ok(response);
    }

}
