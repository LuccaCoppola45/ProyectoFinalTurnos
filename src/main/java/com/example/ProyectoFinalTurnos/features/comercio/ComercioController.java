package com.example.ProyectoFinalTurnos.features.comercio;

import com.example.ProyectoFinalTurnos.exceptions.catalog.ComercioNotFoundException;
import com.example.ProyectoFinalTurnos.features.categoria.CategoriaEntity;
import com.example.ProyectoFinalTurnos.features.comercio.Dtos.ComercioRequest;
import com.example.ProyectoFinalTurnos.features.comercio.Dtos.ComercioResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comercios")
@RequiredArgsConstructor
public class ComercioController {

    private final IComercioService comercioService;
    private final ComercioRepository comercioRepository;

    @PostMapping
    public ResponseEntity<ComercioResponse> crearComercio(@Valid @RequestBody ComercioRequest request) {
        // Si la validación falla, Tira un error 400 automáticamente, sin necesidad de escribir código adicional.
        ComercioResponse response = comercioService.crearComercio(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
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
            return ResponseEntity.ok(comercioService.buscarPorId(id)); // 200

    }

    @PutMapping("/{id}")
    public ResponseEntity<ComercioResponse> actualizarComercio(@PathVariable long id, @Valid @RequestBody ComercioRequest request) {
        ComercioResponse response = comercioService.actualizarComercio(id, request);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
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

    @GetMapping("/por-categoria/{categoriaId}")
    public ResponseEntity<List<ComercioResponse>> comerciosPorCategoria(@PathVariable Long categoriaId) {
        List<ComercioResponse> comercios = comercioService.comerciosPorCategoria(categoriaId);
        return comercios.isEmpty() ?
                ResponseEntity.noContent().build() :
                ResponseEntity.ok(comercios);
    }
}