package com.example.ProyectoFinalTurnos.features.turno;

import com.example.ProyectoFinalTurnos.features.turno.Dtos.ConfirmacionTurnoResponse;
import com.example.ProyectoFinalTurnos.features.turno.Dtos.RespuestaAsistenciaRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Endpoints PÚBLICOS (ver SecurityConfig) para el link del mail de recordatorio.
 * GET  -> datos del turno para mostrar en la página "¿Vas a ir?"
 * POST -> el cliente responde: { "asiste": true } confirma, { "asiste": false } cancela.
 */
@RestController
@RequestMapping("/api/turnos/confirmacion")
@RequiredArgsConstructor
public class ConfirmacionTurnoController {

    private final ConfirmacionTurnoService confirmacionService;

    @GetMapping("/{token}")
    public ResponseEntity<ConfirmacionTurnoResponse> obtener(@PathVariable String token) {
        return ResponseEntity.ok(confirmacionService.obtener(token));
    }

    @PostMapping("/{token}")
    public ResponseEntity<ConfirmacionTurnoResponse> responder(
            @PathVariable String token,
            @Valid @RequestBody RespuestaAsistenciaRequest request) {
        return ResponseEntity.ok(confirmacionService.responder(token, request.asiste()));
    }
}
