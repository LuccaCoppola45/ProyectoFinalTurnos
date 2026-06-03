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
    public ResponseEntity<?> crearUsuario(@Valid @RequestBody UsuarioRequest dto)
    {
        try
        {
            UsuarioResponce usuarioResponce=usuarioService.crearUsuario(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioResponce);
        }
        catch(Exception e)
        {
            Map<String, Object> response = new HashMap<>();
            response.put("error", e.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponce>> obtenerTodos()
    {
        try
        {
            List<UsuarioResponce> usuario = usuarioService.obtenerTodos();
            if (usuario.isEmpty())
            {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            return ResponseEntity.ok(usuario);
        } catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponce> obtenerUsuarioPorId(@PathVariable Long id)
    {
        try
        {
            UsuarioResponce usuario = usuarioService.obtenerPorId(id);
            return ResponseEntity.ok(usuario);
        }
        catch(RuntimeException e)
            {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id)
    {
        try
        {
            usuarioService.eliminarUsuario(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        catch(RuntimeException e)
            {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

    }

    @PutMapping("/update/{id}")
    public ResponseEntity<@NonNull UpdateUserResponse> updateUser(@PathVariable Long id , @Valid @RequestBody UpdateUserRequestDTO request){
       return ResponseEntity.ok(this.usuarioService.actualizarUsuario(id, request));
    }

}
