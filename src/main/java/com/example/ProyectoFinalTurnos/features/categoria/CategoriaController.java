package com.example.ProyectoFinalTurnos.features.categoria;

import com.example.ProyectoFinalTurnos.features.categoria.dto.categoriaRequest;
import com.example.ProyectoFinalTurnos.features.categoria.dto.categoriaResponse;
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
@RequestMapping("/categorias")
@RequiredArgsConstructor
@Validated
@CrossOrigin(origins = "*")
public class CategoriaController {
    
    private final CategoriaService categoriaService;

    @PostMapping
    public ResponseEntity<categoriaResponse> crearCategoria(@Valid @RequestBody categoriaRequest requestDto) {
        categoriaResponse response = categoriaService.crearCategoria(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<categoriaResponse>> listarCategorias() {
        List<categoriaResponse> categorias = categoriaService.listarCategorias();
        return ResponseEntity.ok(categorias);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<categoriaResponse> obtenerCategoriaPorId(@PathVariable @Positive(message = "El ID debe ser mayor a 0") Long id) {
        categoriaResponse categoria = categoriaService.obtenerCategoriaPorId(id);
        return ResponseEntity.ok(categoria);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<categoriaResponse> actualizarCategoria(@PathVariable @Positive(message = "El ID debe ser mayor a cero") Long id,
                                                                   @Valid @RequestBody categoriaRequest requestDto) {
        categoriaResponse response = categoriaService.actualizarCategoria(id, requestDto);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCategoria(@PathVariable @Positive(message = "El ID debe ser mayor a cero") Long id) {
        categoriaService.eliminarCategoria(id);
        return ResponseEntity.noContent().build();
    }
}
