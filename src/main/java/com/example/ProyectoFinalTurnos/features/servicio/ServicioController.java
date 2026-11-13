package com.example.ProyectoFinalTurnos.features.servicio;

import com.example.ProyectoFinalTurnos.features.servicio.Dtos.ServicioRequest;
import com.example.ProyectoFinalTurnos.features.servicio.Dtos.ServicioResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/servicios")
@RequiredArgsConstructor
public class ServicioController {

    private final IServicioService servicioService;

    @PostMapping
    public ResponseEntity<ServicioResponse> crearServicio(@Valid @RequestBody ServicioRequest request) {
        ServicioResponse response = servicioService.crearServicio(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ServicioResponse>> listarServicios() {
        List<ServicioResponse> servicios = servicioService.listarServicios();
        if (servicios.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(servicios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicioResponse> buscarPorId(@PathVariable long id) {
        ServicioResponse servicio = servicioService.BuscarporId(id);
        return ResponseEntity.ok(servicio);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServicioResponse> actualizarServicio(@PathVariable long id, @Valid @RequestBody ServicioRequest request) {
        ServicioResponse response = servicioService.ActualizarServicio(id, request);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarServicio(@PathVariable long id) {
        servicioService.eliminarServicio(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/comercio/{comercioId}")
    public ResponseEntity<List<ServicioResponse>> serviciosPorComercio(@PathVariable Long comercioId) {
        List<ServicioResponse> servicios = servicioService.serviciosPorComercio(comercioId);
        return servicios.isEmpty() ?
                ResponseEntity.noContent().build() :
                ResponseEntity.ok(servicios);
    }
}
