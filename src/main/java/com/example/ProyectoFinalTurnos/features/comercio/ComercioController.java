package com.example.ProyectoFinalTurnos.features.comercio;

import com.example.ProyectoFinalTurnos.exceptions.catalog.ComercioNotFoundException;
import com.example.ProyectoFinalTurnos.features.categoria.CategoriaEntity;
import com.example.ProyectoFinalTurnos.features.comercio.Dtos.ComercioRequest;
import com.example.ProyectoFinalTurnos.features.comercio.Dtos.ComercioResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comercios")
@RequiredArgsConstructor
@Validated
public class ComercioController {

    private final IComercioService comercioService;

//    @PostMapping
//    public ResponseEntity<ComercioResponse> crearComercio(@Valid @RequestBody ComercioRequest request) {
//        // Si la validación falla, Tira un error 400 automáticamente, sin necesidad de escribir código adicional.
//        ComercioResponse response = comercioService.crearComercio(request);
//        return ResponseEntity.status(HttpStatus.CREATED).body(response);
//    }
//
//
//    @GetMapping
//    public ResponseEntity<List<ComercioResponse>> listarComercioS() {
//        try {
//            List<ComercioResponse> comercios = comercioService.listarComercioS();
//            if (comercios.isEmpty()) {
//                return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // 204
//            }
//            return ResponseEntity.ok(comercios); // 200
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500
//        }
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<ComercioResponse> buscarPorId(@PathVariable long id) {
//            return ResponseEntity.ok(comercioService.buscarPorId(id)); // 200
//
//    }
//
    @PutMapping("/{id}")
    public ResponseEntity<ComercioResponse> actualizarComercio(@PathVariable long id, @Valid @RequestBody ComercioRequest request) {
        ComercioResponse response = comercioService.actualizarComercio(id, request);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> eliminarComercio(@PathVariable long id) {
//        try {
//            comercioService.eliminarComercio(id);
//            return ResponseEntity.noContent().build(); // 204
//        } catch (RuntimeException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404
//        }
//    }

    @PostMapping
    public ResponseEntity<ComercioResponse> crearComercio(@Valid @RequestBody ComercioRequest requestDto) {
        ComercioResponse response = comercioService.crearComercio(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComercioResponse> buscarComercioPorId(
            @PathVariable @Positive(message = "El ID debe ser mayor a 0") Long id) {
        ComercioResponse response = comercioService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarComercio(
            @PathVariable @Positive(message = "El ID debe ser mayor a 0") Long id) {
        comercioService.eliminarComercio(id);
        return ResponseEntity.noContent().build(); // Devuelve un HTTP 204 No Content (Éxito sin cuerpo)
    }
}