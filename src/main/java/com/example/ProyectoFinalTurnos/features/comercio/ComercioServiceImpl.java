package com.example.ProyectoFinalTurnos.features.comercio;

import com.example.ProyectoFinalTurnos.exceptions.catalog.ComercioNotFoundException;
import com.example.ProyectoFinalTurnos.features.categoria.CategoriaEntity;
import com.example.ProyectoFinalTurnos.features.categoria.CategoriaRepository;
import com.example.ProyectoFinalTurnos.features.comercio.Dtos.ComercioRequest;
import com.example.ProyectoFinalTurnos.features.comercio.Dtos.ComercioResponse;
import com.example.ProyectoFinalTurnos.features.comercio.Mapper.ComercioMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ComercioServiceImpl implements IComercioService {

    private final ComercioRepository comercioRepository;
    private final ComercioMapper comercioMapper;
    private final CategoriaRepository categoriaRepository;

    public ComercioServiceImpl(ComercioRepository comercioRepository, ComercioMapper comercioMapper, CategoriaRepository categoriaRepository) {
        this.comercioRepository = comercioRepository;
        this.comercioMapper = comercioMapper;
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    @Transactional
    public ComercioResponse crearComercio(ComercioRequest request) {

        // 1. Limpiar espacios en blanco de los textos
        String nombreLimpio = request.getNombre().trim();
        String direccionLimpia = request.getDireccion().trim();

        // 2. Validar que no exista un comercio duplicado (mismo nombre + dirección)
        boolean existeDuplicado = comercioRepository.findByNombreIgnoreCaseAndDireccionIgnoreCase(nombreLimpio, direccionLimpia).isPresent();
        if (existeDuplicado) {
            throw new IllegalArgumentException("Ya existe un comercio registrado con ese nombre en esa dirección.");
        }

        // valida que las categorias existan en la base de datos
        List<CategoriaEntity> categoriasValidas = request.getListaCategoriasIds().stream()
                .map(id -> categoriaRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("La categoría con ID " + id + " no existe.")))
                .collect(Collectors.toList());

        ComercioEntity entity = comercioMapper.toEntity(request);

        entity.setNombre(nombreLimpio);
        entity.setDireccion(direccionLimpia);
        entity.setCategorias(categoriasValidas);


        ComercioEntity saved = comercioRepository.save(entity);
        return comercioMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ComercioResponse> listarComercioS() {
        return comercioRepository.findAll()
                .stream()
                .map(comercioMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ComercioResponse buscarPorId(Long id) {
        ComercioEntity entity = comercioRepository.findById(id)
                .orElseThrow(() -> new ComercioNotFoundException("Comercio no encontrado con id: " + id));
        return comercioMapper.toResponse(entity);
    }

    @Override
    @Transactional
    public ComercioResponse actualizarComercio(Long id, ComercioRequest request) {
        ComercioEntity entity = comercioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comercio no encontrado con id: " + id));
        

        entity.setNombre(request.getNombre().trim());
        entity.setDireccion(request.getDireccion().trim());
        

        List<CategoriaEntity> categoriasValidas = request.getListaCategoriasIds().stream()
                .map(idCat -> categoriaRepository.findById(idCat)
                        .orElseThrow(() -> new RuntimeException("La categoría con ID " + idCat + " no existe.")))
                .collect(Collectors.toList());
        entity.setCategorias(categoriasValidas);
        
     
        ComercioEntity updated = comercioRepository.save(entity);
        return comercioMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void eliminarComercio(Long id) {
        ComercioEntity entity = comercioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comercio no encontrado con id: " + id));
        comercioRepository.delete(entity);
    }
}

