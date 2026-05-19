package com.example.ProyectoFinalTurnos.features.categoria;

import com.example.ProyectoFinalTurnos.features.categoria.dto.categoriaRequest;
import com.example.ProyectoFinalTurnos.features.categoria.dto.categoriaResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias")
@RequiredArgsConstructor
public class CategoriaController {
    
    private final CategoriaService categoriaService;
    
    // POST: Crear una categoría
    @PostMapping
    public ResponseEntity<categoriaResponse> crearCategoria(@Valid @RequestBody categoriaRequest requestDto) {
        categoriaResponse response = categoriaService.crearCategoria(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    // GET: Listar todas las categorías
    @GetMapping
    public ResponseEntity<List<categoriaResponse>> listarCategorias() {
        List<categoriaResponse> categorias = categoriaService.listarCategorias();
        return ResponseEntity.ok(categorias);
    }
    
    // GET by ID: Obtener una categoría específica
    @GetMapping("/{id}")
    public ResponseEntity<categoriaResponse> obtenerCategoriaPorId(@PathVariable Long id) {
        categoriaResponse categoria = categoriaService.obtenerCategoriaPorId(id);
        return ResponseEntity.ok(categoria);
    }
    
    // PUT: Actualizar una categoría
    @PutMapping("/{id}")
    public ResponseEntity<categoriaResponse> actualizarCategoria(@PathVariable Long id, 
                                                                   @Valid @RequestBody categoriaRequest requestDto) {
        categoriaResponse response = categoriaService.actualizarCategoria(id, requestDto);
        return ResponseEntity.ok(response);
    }
    
    // DELETE: Eliminar una categoría
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCategoria(@PathVariable Long id) {
        categoriaService.eliminarCategoria(id);
        return ResponseEntity.noContent().build();
    }
}

