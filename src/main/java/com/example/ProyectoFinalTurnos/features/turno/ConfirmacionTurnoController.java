package com.example.ProyectoFinalTurnos.features.turno;

import com.example.ProyectoFinalTurnos.features.turno.Dtos.ConfirmacionTurnoResponse;
import com.example.ProyectoFinalTurnos.features.turno.Dtos.RespuestaAsistenciaRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
