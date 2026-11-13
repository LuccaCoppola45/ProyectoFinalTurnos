package com.example.ProyectoFinalTurnos.features.categoria;

import com.example.ProyectoFinalTurnos.features.categoria.dto.categoriaRequest;
import com.example.ProyectoFinalTurnos.features.categoria.dto.categoriaResponse;
import java.util.List;

public interface CategoriaService {
    
    categoriaResponse crearCategoria(categoriaRequest requestDto);
    
    List<categoriaResponse> listarCategorias();
    
    categoriaResponse obtenerCategoriaPorId(Long id);
    
    categoriaResponse actualizarCategoria(Long id, categoriaRequest requestDto);
    
    void eliminarCategoria(Long id);
}
