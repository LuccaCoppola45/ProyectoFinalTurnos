package com.example.ProyectoFinalTurnos.features.cancelacion;

import com.example.ProyectoFinalTurnos.features.cancelacion.dto.CancelacionRequest;
import com.example.ProyectoFinalTurnos.features.cancelacion.dto.CancelacionResponse;
import com.example.ProyectoFinalTurnos.features.cancelacion.mapper.CancelacionMapper;
import com.example.ProyectoFinalTurnos.features.turno.TurnoEntity;
import com.example.ProyectoFinalTurnos.features.turno.TurnoRepository;
import com.example.ProyectoFinalTurnos.features.usuario.UsuarioEntity;
import com.example.ProyectoFinalTurnos.features.usuario.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CancelacionServiceImpl implements CancelacionService {
    private final CancelacionRepository cancelacionRepository;
    private final UsuarioRepository usuarioRepository;
    private final TurnoRepository turnoRepository;

    private final CancelacionMapper cancelacionMapper;

    //crear una cancelacion
    @Override
    @Transactional
    public CancelacionResponse cancelarTurno(CancelacionRequest requestDTO) {
        UsuarioEntity usuario = usuarioRepository.findById(requestDTO.getIdUsuario()).
                orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        TurnoEntity turno = turnoRepository.findById(requestDTO.getIdTurno())
                .orElseThrow(() -> new RuntimeException("Turno no encontrado con ID: " + requestDTO.getIdTurno()));

        // 2. USO DEL MAPPER (DTO -> Entidad)
        CancelacionEntity cancelacionEntity = cancelacionMapper.requestDtotoEntity(requestDTO);


        cancelacionEntity.setUsuario(usuario);
        cancelacionEntity.setTurno(turno);
        cancelacionEntity.setFechaCancelacion(LocalDateTime.now());

        turno.setEstado("CANCELADO");
        turnoRepository.save(turno);
        // 4. PERSISTENCIA
        CancelacionEntity guardada = cancelacionRepository.save(cancelacionEntity);

        // 5. USO DEL MAPPER (Entidad -> ResponseDTO) 👇
        String mensajeExito = "El turno ID: " + turno.getIdTurno() + " se canceló correctamente.";
        return cancelacionMapper.entitytoResponseDto(guardada, mensajeExito);
    }



}

