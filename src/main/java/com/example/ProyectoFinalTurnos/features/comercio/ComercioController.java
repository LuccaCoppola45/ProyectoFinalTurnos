package com.example.ProyectoFinalTurnos.features.comercio;

import com.example.ProyectoFinalTurnos.features.comercio.Dtos.ComercioRequest;
import com.example.ProyectoFinalTurnos.features.comercio.Dtos.ComercioResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comercios")
@RequiredArgsConstructor
public class ComercioController {

    private final IComercioService comercioService;

    @PostMapping
    public ResponseEntity<ComercioResponse> crearComercio(@Valid @RequestBody ComercioRequest request) {
        try {
            ComercioResponse response = comercioService.crearComercio(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response); // 201
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // 400
        }
    }

    @GetMapping
    public ResponseEntity<List<ComercioResponse>> listarComercioS() {
        try {
            List<ComercioResponse> comercios = comercioService.listarComercioS();
            if (comercios.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // 204
            }
            return ResponseEntity.ok(comercios); // 200
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComercioResponse> buscarPorId(@PathVariable long id) {
        try {
            ComercioResponse comercio = comercioService.buscarPorId(id);
            return ResponseEntity.ok(comercio); // 200
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ComercioResponse> actualizarComercio(@PathVariable long id, @Valid @RequestBody ComercioRequest request) {
        try {
            ComercioResponse response = comercioService.actualizarComercio(id, request);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(response); // 202
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarComercio(@PathVariable long id) {
        try {
            comercioService.eliminarComercio(id);
            return ResponseEntity.noContent().build(); // 204
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404
        }
    }
}

