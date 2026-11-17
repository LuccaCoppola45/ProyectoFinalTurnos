package com.example.ProyectoFinalTurnos.features.auth;

import com.example.ProyectoFinalTurnos.features.auth.dtos.OlvidePasswordRequest;
import com.example.ProyectoFinalTurnos.features.auth.dtos.RestablecerPasswordRequest;
import com.example.ProyectoFinalTurnos.features.usuario.Dtos.LoginResponseDTO;
import com.example.ProyectoFinalTurnos.features.usuario.Dtos.LoginResquestDTO;
import com.example.ProyectoFinalTurnos.features.usuario.Dtos.UsuarioRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> registrar(@Valid @RequestBody UsuarioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registrar(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginResquestDTO request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> olvidePassword(@Valid @RequestBody OlvidePasswordRequest request) {
        return ResponseEntity.ok(authService.solicitarRecuperacion(request));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> restablecerPassword(@Valid @RequestBody RestablecerPasswordRequest request) {
        return ResponseEntity.ok(authService.restablecerPassword(request));
    }
}
