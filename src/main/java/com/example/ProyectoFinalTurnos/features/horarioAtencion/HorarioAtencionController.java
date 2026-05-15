package com.example.ProyectoFinalTurnos.features.horarioAtencion;

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

    @PostMapping
    public ResponseEntity<HorarioAtencionResponse> crearHorarioAtencion(@Valid @RequestBody HorarioAtencionRequest request) {
        try {
            HorarioAtencionResponse response = horarioAtencionService.crearHorariotencion(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response); // 201
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // 400
        }
    }

    @GetMapping
    public ResponseEntity<List<HorarioAtencionResponse>> listarHorarios() {
        try {
            List<HorarioAtencionResponse> horarios = horarioAtencionService.ListarHorarios();
            if (horarios.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // 204
            }
            return ResponseEntity.ok(horarios); // 200
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<HorarioAtencionResponse> obtenerHorarioPorId(@PathVariable long id) {
        try {
            HorarioAtencionResponse horario = horarioAtencionService.listarHorarios(id);
            return ResponseEntity.ok(horario); // 200
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<HorarioAtencionResponse> actualizarHorarioAtencion(@PathVariable long id, @Valid @RequestBody HorarioAtencionRequest request) {
        try {
            HorarioAtencionResponse response = horarioAtencionService.ActualizarHorarioAtencion(id, request);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(response); // 202
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarHorarioAtencion(@PathVariable long id) {
        try {
            horarioAtencionService.EliminarHorarioAtencion(id);
            return ResponseEntity.noContent().build(); // 204
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404
        }
    }
}
