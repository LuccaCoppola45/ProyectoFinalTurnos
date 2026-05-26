package com.example.ProyectoFinalTurnos.features.usuario;

import com.example.ProyectoFinalTurnos.features.usuario.Dtos.UsuarioRequest;
import com.example.ProyectoFinalTurnos.features.usuario.Dtos.UsuarioResponce;
import jakarta.validation.Valid;
import org.apache.catalina.LifecycleState;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public UsuarioResponce crearUsuario(@RequestBody @Valid UsuarioRequest dto)
    {
        return usuarioService.crearUsuario(dto);
    }

    @GetMapping
    public List<UsuarioResponce> obtenerTodos()
    {
        return usuarioService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public UsuarioResponce obtenerUsuarioPorId(@PathVariable Long id)
    {
        return usuarioService.obtenerPorId(id);
    }

    @DeleteMapping("/{id}")
    public void eliminarUsuario(@PathVariable Long id)
    {
        usuarioService.eliminarUsuario(id);
    }

}
