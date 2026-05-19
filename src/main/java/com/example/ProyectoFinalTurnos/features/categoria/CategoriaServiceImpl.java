package com.example.ProyectoFinalTurnos.features.categoria;

import com.example.ProyectoFinalTurnos.features.categoria.dto.categoriaRequest;
import com.example.ProyectoFinalTurnos.features.categoria.dto.categoriaResponse;
import com.example.ProyectoFinalTurnos.features.categoria.mapper.CategoriaMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CategoriaServiceImpl implements CategoriaService {
    
    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper categoriaMapper;
    
    @Override
    @Transactional
    public categoriaResponse crearCategoria(categoriaRequest requestDto) {
        // Mapear DTO a Entidad
        CategoriaEntity categoriaEntity = categoriaMapper.requestDtoToEntity(requestDto);
        
        // Guardar en la base de datos
        CategoriaEntity guardada = categoriaRepository.save(categoriaEntity);
        
        // Mapear Entidad a Response DTO
        return categoriaMapper.entityToResponseDto(guardada);
    }
    
    @Override
    public List<categoriaResponse> listarCategorias() {
        List<CategoriaEntity> categorias = categoriaRepository.findAll();
        
        return categorias.stream()
                .map(categoriaMapper::entityToResponseDto)
                .toList();
    }
    
    @Override
    public categoriaResponse obtenerCategoriaPorId(Long id) {
        CategoriaEntity categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Categoría no encontrada con ID: " + id));
        
        return categoriaMapper.entityToResponseDto(categoria);
    }
    
    @Override
    @Transactional
    public categoriaResponse actualizarCategoria(Long id, categoriaRequest requestDto) {
        CategoriaEntity categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Categoría no encontrada con ID: " + id));
        
        categoria.setNombreCategoria(requestDto.getNombreCategoria());
        
        CategoriaEntity actualizada = categoriaRepository.save(categoria);
        
        return categoriaMapper.entityToResponseDto(actualizada);
    }
    
    @Override
    @Transactional
    public void eliminarCategoria(Long id) {
        if (!categoriaRepository.existsById(id)) {
            throw new NoSuchElementException("No se puede eliminar. Categoría no encontrada con ID: " + id);
        }
        categoriaRepository.deleteById(id);
    }
}

