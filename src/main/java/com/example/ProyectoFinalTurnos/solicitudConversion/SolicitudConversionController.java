package com.example.ProyectoFinalTurnos.solicitudConversion;

import com.example.ProyectoFinalTurnos.solicitudConversion.dtos.SolicitudConversionRequest;
import com.example.ProyectoFinalTurnos.solicitudConversion.dtos.SolicitudConversionResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/solicitudes")
@RequiredArgsConstructor
public class SolicitudConversionController {

    private final SolicitudConversionService service;

    @PostMapping
    public ResponseEntity<SolicitudConversionResponse> crearSolicitud(@RequestBody SolicitudConversionRequest request) {
        String email = obtenerEmail();
        SolicitudConversionResponse resp = service.crearSolicitud(request, email);
        return ResponseEntity.ok(resp);
    }

    // Solo ADMIN
    @GetMapping
    public ResponseEntity<List<SolicitudConversionResponse>> listarPendientes() {
        List<SolicitudConversionResponse> lista = service.listarSolicitudesPendientes();
        return ResponseEntity.ok(lista);
    }

    // Solo ADMIN: aprobar
    @PutMapping("/{id}/aprobar")
    public ResponseEntity<SolicitudConversionResponse> aprobar(@PathVariable Long id) {
        String emailAdmin = obtenerEmail();
        SolicitudConversionResponse resp = service.aprobarSolicitud(id, emailAdmin);
        return ResponseEntity.ok(resp);
    }

    // Solo ADMIN: rechazar
    @PutMapping("/{id}/rechazar")
    public ResponseEntity<SolicitudConversionResponse> rechazar(@PathVariable Long id, @RequestParam(required = false) String motivo) {
        String emailAdmin = obtenerEmail();
        SolicitudConversionResponse resp = service.rechazarSolicitud(id, emailAdmin, motivo);
        return ResponseEntity.ok(resp);
    }

    private String obtenerEmail() {
        @Nullable Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) throw new RuntimeException("No autenticado");
        return auth.getName();
    }
}