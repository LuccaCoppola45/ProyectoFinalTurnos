package com.example.ProyectoFinalTurnos.features.horarioAtencion;

import com.example.ProyectoFinalTurnos.exceptions.catalog.ComercioNotFoundException;
import com.example.ProyectoFinalTurnos.features.horarioAtencion.Dtos.HorarioAtencionRequest;
import com.example.ProyectoFinalTurnos.features.horarioAtencion.Dtos.HorarioAtencionResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/horarios-atencion")
@RequiredArgsConstructor
public class HorarioAtencionController {

    private final IHorarioAtencionService horarioAtencionService;

    /**
     * POST - Crear un nuevo horario de atención
     * @param request datos del horario a crear
     * @return 201 Created con el horario creado
     *         400 Bad Request si los datos son inválidos o hay conflicto
     *         404 Not Found si el comercio no existe
     */
    @PostMapping
    public ResponseEntity<HorarioAtencionResponse> crearHorarioAtencion(@Valid @RequestBody HorarioAtencionRequest request) {
        try {
            HorarioAtencionResponse response = horarioAtencionService.crearHorariotencion(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response); // 201 Created
        } catch (ComercioNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // 400 Bad Request
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500
        }
    }

    /**
     * GET - Listar todos los horarios de atención
     * @return 200 OK con lista de horarios
     *         204 No Content si no hay horarios
     *         500 Internal Server Error en caso de error
     */
    @GetMapping
    public ResponseEntity<List<HorarioAtencionResponse>> listarHorarios() {
        try {
            List<HorarioAtencionResponse> horarios = horarioAtencionService.ListarHorarios();
            if (horarios.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // 204 No Content
            }
            return ResponseEntity.ok(horarios); // 200 OK
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500
        }
    }

    /**
     * GET - Obtener un horario por ID
     * @param id del horario
     * @return 200 OK con el horario encontrado
     *         404 Not Found si el horario no existe
     */
    @GetMapping("/{id}")
    public ResponseEntity<HorarioAtencionResponse> obtenerHorarioPorId(@PathVariable long id) {
        try {
            HorarioAtencionResponse horario = horarioAtencionService.listarHorarios(id);
            return ResponseEntity.ok(horario); // 200 OK
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
        }
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
