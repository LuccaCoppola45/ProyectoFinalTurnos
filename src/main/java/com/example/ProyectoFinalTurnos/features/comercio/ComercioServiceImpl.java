package com.example.ProyectoFinalTurnos.features.comercio;

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

    public ComercioServiceImpl(ComercioRepository comercioRepository, ComercioMapper comercioMapper) {
        this.comercioRepository = comercioRepository;
        this.comercioMapper = comercioMapper;
    }

    @Override
    @Transactional
    public ComercioResponse crearComercio(ComercioRequest request) {
        ComercioEntity entity = comercioMapper.toEntity(request);
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
    public ComercioResponse buscarPorId(long id) {
        ComercioEntity entity = comercioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comercio no encontrado con id: " + id));
        return comercioMapper.toResponse(entity);
    }

    @Override
    @Transactional
    public ComercioResponse actualizarComercio(long id, ComercioRequest request) {
        ComercioEntity entity = comercioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comercio no encontrado con id: " + id));
        comercioMapper.updateEntity(request, entity);
        ComercioEntity updated = comercioRepository.save(entity);
        return comercioMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void eliminarComercio(long id) {
        ComercioEntity entity = comercioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comercio no encontrado con id: " + id));
        comercioRepository.delete(entity);
    }
}

