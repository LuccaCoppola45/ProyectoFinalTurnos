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
        List<HorarioAtencionResponse> horarios = horarioAtencionService.ListarHorarios();
        return ResponseEntity.ok(horarios);
    }


    @GetMapping("/{id}")
    public ResponseEntity<HorarioAtencionResponse> obtenerHorarioPorId(@PathVariable long id) {
       HorarioAtencionResponse response = horarioAtencionService.ObtenerHorarioPorId(id);
       return ResponseEntity.ok(response);
    }


    @PutMapping("/{id}")
    public ResponseEntity<HorarioAtencionResponse> actualizarHorarioAtencion(@PathVariable long id,@Valid @RequestBody HorarioAtencionRequest request) {

            HorarioAtencionResponse response = horarioAtencionService.ActualizarHorarioAtencion(id, request);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(response); // 202 Accepted

    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarHorarioAtencion(@PathVariable long id) {
            horarioAtencionService.EliminarHorarioAtencion(id);
            return ResponseEntity.noContent().build();
    }
}
