package com.example.ProyectoFinalTurnos.features.servicio;

import com.example.ProyectoFinalTurnos.features.servicio.Dtos.ServicioRequest;
import com.example.ProyectoFinalTurnos.features.servicio.Dtos.ServicioResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/servicios")
@RequiredArgsConstructor
public class ServicioController {

    private final IServicioService servicioService;

    @PostMapping
    public ResponseEntity<ServicioResponse> crearServicio(@Valid @RequestBody ServicioRequest request) {
        try {
            ServicioResponse response = servicioService.crearServicio(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response); // 201
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // 400
        }
    }

    @GetMapping
    public ResponseEntity<List<ServicioResponse>> listarServicios() {
        try {
            List<ServicioResponse> servicios = servicioService.listarServicios();
            if (servicios.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // 204
            }
            return ResponseEntity.ok(servicios); // 200
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicioResponse> buscarPorId(@PathVariable long id) {
        try {
            ServicioResponse servicio = servicioService.BuscarporId(id);
            return ResponseEntity.ok(servicio); // 200
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServicioResponse> actualizarServicio(@PathVariable long id, @Valid @RequestBody ServicioRequest request) {
        try {
            ServicioResponse response = servicioService.ActualizarServicio(id, request);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(response); // 202
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarServicio(@PathVariable long id) {
        try {
            servicioService.eliminarServicio(id);
            return ResponseEntity.noContent().build(); // 204
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404
        }
    }
}

