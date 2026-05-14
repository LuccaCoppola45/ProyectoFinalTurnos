package com.example.ProyectoFinalTurnos.features.cancelacion;

import com.example.ProyectoFinalTurnos.features.cancelacion.dto.CancelacionRequest;
import com.example.ProyectoFinalTurnos.features.cancelacion.dto.CancelacionResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cancelaciones")
@RequiredArgsConstructor
public class CancelacionController {
private final CancelacionService cancelacionService;
//POST: Crear una cancelacion
    @PostMapping
    public ResponseEntity<CancelacionResponse> cancelarTurno(@Valid @RequestBody CancelacionRequest requestDTO) {
        CancelacionResponse response = cancelacionService.cancelarTurno(requestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<CancelacionResponse>> listarCancelaciones() {
        List<CancelacionResponse> cancelaciones = cancelacionService.listarCancelaciones();
        return ResponseEntity.ok(cancelaciones);
    }

}
