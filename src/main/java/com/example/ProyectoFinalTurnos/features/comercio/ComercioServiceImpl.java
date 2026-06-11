package com.example.ProyectoFinalTurnos.features.comercio;

import com.example.ProyectoFinalTurnos.exceptions.auth.ResourceNotFoundException;
import com.example.ProyectoFinalTurnos.exceptions.catalog.ComercioAlreadyExistsException;
import com.example.ProyectoFinalTurnos.exceptions.catalog.ComercioNotFoundException;
import com.example.ProyectoFinalTurnos.features.categoria.CategoriaEntity;
import com.example.ProyectoFinalTurnos.features.categoria.CategoriaRepository;
import com.example.ProyectoFinalTurnos.features.comercio.Dtos.ComercioRequest;
import com.example.ProyectoFinalTurnos.features.comercio.Dtos.ComercioResponse;
import com.example.ProyectoFinalTurnos.features.comercio.Mapper.ComercioMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.ReadOnlyFileSystemException;
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
    public ComercioResponse crearComercio(ComercioRequest requestDto) {
        // 1. Regla de negocio: Verificar si ya existe el nombre
        if (comercioRepository.existsByNombreComercio(requestDto.getNombre())) {
            throw new ComercioAlreadyExistsException("Ya existe un comercio registrado con el nombre: " + requestDto.getNombre());
        }

        // 2. Integridad: Verificar si la categoría que le quieren asignar existe realmente
        CategoriaEntity categoria = categoriaRepository.findById(requestDto.getIdCategoria())
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la categoría con ID: " + requestDto.getIdCategoria()));

        ComercioEntity comercioEntity = comercioMapper.toEntity(requestDto);
        comercioEntity.setCategoria(categoria);

        ComercioEntity guardado = comercioRepository.save(comercioEntity);
        return comercioMapper.toResponse(guardado);
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

//    @Override
//    @Transactional
//    public ComercioResponse actualizarComercio(Long id, ComercioRequest request) {
//        ComercioEntity entity = comercioRepository.findById(id)
//                .orElseThrow(() -> new ComercioNotFoundException("No se puede actualizar: Comercio no encontrado con id: " + id));
//
//        List<CategoriaEntity> categoriasValidas = request.getListaCategoriasIds().stream()
//                .map(idCat -> categoriaRepository.findById(idCat)
//                        .orElseThrow(() -> new ResourceNotFoundException("La categoría con ID " + idCat + " no existe.")))
//                .collect(Collectors.toList());
//
//        entity.setNombre(request.getNombre().trim());
//        entity.setDireccion(request.getDireccion().trim());
//        entity.setCategorias(categoriasValidas);
//        entity.setTelefono(request.getTelefono().trim());
//
//        ComercioEntity updated = comercioRepository.save(entity);
//        return comercioMapper.toResponse(updated);
//    }

//    @Override
//    @Transactional
//    public void eliminarComercio(Long id) {
//        ComercioEntity entity = comercioRepository.findById(id)
//                .orElseThrow(() -> new ComercioNotFoundException("Comercio no encontrado con id: " + id));
//        comercioRepository.delete(entity);
//    }

    @Override
    @Transactional
    public ComercioResponse actualizarComercio(Long id, ComercioRequest requestDto) {
        // 1. Buscamos el comercio que queremos modificar
        ComercioEntity comercio = comercioRepository.findById(id)
                .orElseThrow(() -> new ComercioNotFoundException("Comercio no encontrado con ID: " + id));

        // 2. VALIDACIÓN CLAVE: Si le cambiaron el nombre, hay que verificar que el nuevo nombre no lo tenga ya otro comercio
        if (!comercio.getNombre().equalsIgnoreCase(requestDto.getNombre())) {
            if (comercioRepository.existsByNombreComercio(requestDto.getNombre())) {
                throw new ComercioAlreadyExistsException("Ya existe un comercio registrado con el nombre: " + requestDto.getNombre());
            }
        }

        // 3. Verificamos que la categoría nueva que le quieren poner también exista
        CategoriaEntity categoria = categoriaRepository.findById(requestDto.getIdCategoria())
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la categoría con ID: " + requestDto.getIdCategoria()));

        // 4. Actualizamos los datos de la Entidad
        comercio.setNombre(requestDto.getNombre());
        comercio.setDireccion(requestDto.getDireccion());
        comercio.setTelefono(requestDto.getTelefono());
        comercio.setCategoria(categoria);

        // 5. Guardamos en la base de datos
        ComercioEntity actualizado = comercioRepository.save(comercio);

        return comercioMapper.toResponse(actualizado);
    }

    @Override
    @Transactional
    public void eliminarComercio(Long id) {
        ComercioEntity comercio = comercioRepository.findById(id)
                .orElseThrow(() -> new ComercioNotFoundException("Comercio no encontrado con ID: " + id));

        // 3. Regla Crítica de Integridad: ¿Tiene servicios activos cargados?
        if (comercio.getServicios() != null && !comercio.getServicios().isEmpty()) {
            throw new IllegalStateException("No se puede eliminar el comercio porque tiene servicios activos asociados. Elimínelos primero.");
        }

        comercioRepository.delete(comercio);
    }
}

