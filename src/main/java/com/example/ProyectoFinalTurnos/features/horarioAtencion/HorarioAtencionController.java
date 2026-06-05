package com.example.ProyectoFinalTurnos.features.horarioAtencion;

import com.example.ProyectoFinalTurnos.exceptions.catalog.ComercioNotFoundException;
import com.example.ProyectoFinalTurnos.features.horarioAtencion.Dtos.HorarioAtencionRequest;
import com.example.ProyectoFinalTurnos.features.horarioAtencion.Dtos.HorarioAtencionResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/horarios-atencion")
@RequiredArgsConstructor
public class HorarioAtencionController {

    private final IHorarioAtencionService horarioAtencionService;


    @PostMapping
    public ResponseEntity<HorarioAtencionResponse> crearHorarioAtencion(@Valid @RequestBody HorarioAtencionRequest request) {
        HorarioAtencionResponse response = horarioAtencionService.crearHorariotencion(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response); // 201 Created
    }


    @GetMapping
    public ResponseEntity<List<HorarioAtencionResponse>> listarHorarios() {
        List<HorarioAtencionResponse> horarios = horarioAtencionService.listarHorarios();
        return ResponseEntity.ok(horarios);
    }


    @GetMapping("/{id}")
    public ResponseEntity<HorarioAtencionResponse> obtenerHorarioPorId(@PathVariable long id) {
       HorarioAtencionResponse response = horarioAtencionService.
    }

    /**
     * PUT - Actualizar un horario de atención
     * @param id del horario a actualizar
     * @param request datos actualizados
     * @return 202 Accepted si se actualiza correctamente
     *         400 Bad Request si los datos son inválidos o hay conflicto
     *         404 Not Found si el horario o comercio no existe
     */
    @PutMapping("/{id}")
    public ResponseEntity<HorarioAtencionResponse> actualizarHorarioAtencion(
            @PathVariable long id,
            @Valid @RequestBody HorarioAtencionRequest request) {
        try {
            HorarioAtencionResponse response = horarioAtencionService.ActualizarHorarioAtencion(id, request);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(response); // 202 Accepted
        } catch (ComercioNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // 400 Bad Request
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
        }
    }

    /**
     * DELETE - Eliminar un horario de atención
     * @param id del horario a eliminar
     * @return 204 No Content si se elimina correctamente
     *         404 Not Found si el horario no existe
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarHorarioAtencion(@PathVariable long id) {
        try {
            horarioAtencionService.EliminarHorarioAtencion(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
        }
    }
}
