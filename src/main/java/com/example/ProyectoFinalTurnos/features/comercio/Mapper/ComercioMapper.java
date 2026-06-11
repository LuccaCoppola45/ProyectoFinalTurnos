package com.example.ProyectoFinalTurnos.features.comercio.Mapper;

import com.example.ProyectoFinalTurnos.features.comercio.ComercioEntity;
import com.example.ProyectoFinalTurnos.features.comercio.Dtos.ComercioRequest;
import com.example.ProyectoFinalTurnos.features.comercio.Dtos.ComercioResponse;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ComercioMapper {

    private final ModelMapper modelMapper;

    public ComercioMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

//    public ComercioResponse toResponse(ComercioEntity entity) {
//        if (entity == null) {
//            return null;
//        }
//
//        ComercioResponse response = modelMapper.map(entity, ComercioResponse.class);
//
//        if (entity.getCategorias() != null) {
//            response.setCategorias(
//                    entity.getCategorias().stream()
//                            .map(cat -> cat.getNombreCategoria())
//                            .collect(Collectors.toList())
//            );
//        }
//
//        return response;
//    }
//
//    public ComercioEntity toEntity(ComercioRequest request) {
//        if (request == null) {
//            return null;
//        }
//
//        return modelMapper.map(request, ComercioEntity.class);
//    }
//
//    public void updateEntity(ComercioRequest request, ComercioEntity entity) {
//        if (request == null || entity == null) {
//            return;
//        }
//
//        modelMapper.map(request, entity);
//    }

    // Entidad -> Response
    public ComercioResponse entityToResponseDto(ComercioEntity entity) {
        if (entity == null) {
            return null;
        }

        // El ModelMapper mapea automáticamente el nombre, dirección, teléfono, etc.
        ComercioResponse response = modelMapper.map(entity, ComercioResponse.class);

        // --- OPCIÓN A: Si un comercio tiene UNA SOLA categoría ---
        if (entity.getCategoria() != null) {
            // Asumiendo que ComercioResponse tiene un "private String nombreCategoria;"
            response.setNombre(entity.getCategoria().getNombreCategoria());
        }
        return response;
    }

    // Request -> Entidad
    public ComercioEntity requestDtoToEntity(ComercioRequest request) {
        if (request == null) {
            return null;
        }

        // Nota: ModelMapper mapea todo lo básico. La asignación de la CategoríaEntity
        // a partir del idCategoria la seguís haciendo en el Servicio (como ya lo tenés).
        return modelMapper.map(request, ComercioEntity.class);
    }

    // Actualizar Entidad existente
    public void updateEntity(ComercioRequest request, ComercioEntity entity) {
        if (request == null || entity == null) {
            return;
        }

        // Esto sobrescribe los campos del Request en la Entidad que pasaste
        modelMapper.map(request, entity);
    }
}
