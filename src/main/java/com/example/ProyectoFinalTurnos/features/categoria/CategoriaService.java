package com.example.ProyectoFinalTurnos.features.categoria;

import com.example.ProyectoFinalTurnos.features.categoria.dto.categoriaRequest;
import com.example.ProyectoFinalTurnos.features.categoria.dto.categoriaResponse;
import java.util.List;

public interface CategoriaService {
    
    // POST: Crear una categoría
    categoriaResponse crearCategoria(categoriaRequest requestDto);
    
    // GET: Listar todas las categorías
    List<categoriaResponse> listarCategorias();
    
    // GET by ID: Obtener una categoría por ID
    categoriaResponse obtenerCategoriaPorId(Long id);
    
    // PUT: Actualizar una categoría
    categoriaResponse actualizarCategoria(Long id, categoriaRequest requestDto);
    
    // DELETE: Eliminar una categoría
    void eliminarCategoria(Long id);
}

