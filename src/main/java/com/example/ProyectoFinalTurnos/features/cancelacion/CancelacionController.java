package com.example.ProyectoFinalTurnos.features.cancelacion;

import com.example.ProyectoFinalTurnos.features.cancelacion.dto.CancelacionRequest;
import com.example.ProyectoFinalTurnos.features.cancelacion.dto.CancelacionResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cancelaciones")
@RequiredArgsConstructor
public class CancelacionController {
    private final CancelacionService cancelacionService;

    //POST: Crear una cancelacion
    @PostMapping
    public ResponseEntity<?> cancelarTurno(@Valid @RequestBody CancelacionRequest requestDTO) {


        try {
            CancelacionResponse response = cancelacionService.cancelarTurno(requestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

    }

    @GetMapping
    public ResponseEntity<List<CancelacionResponse>> listarCancelaciones() {
        List<CancelacionResponse> cancelaciones = cancelacionService.listarCancelaciones();
        return ResponseEntity.ok(cancelaciones);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CancelacionResponse> buscarCancelacionId(@Valid @PathVariable long id) {
        CancelacionResponse cancelacion = cancelacionService.obtenerCancelacionPorId(id);
        return ResponseEntity.ok(cancelacion);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCancelacion(@Valid @PathVariable long id) {
        cancelacionService.eliminarCancelacion(id);
        return ResponseEntity.noContent().build();
    }
}