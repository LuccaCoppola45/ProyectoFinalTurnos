package com.example.ProyectoFinalTurnos.features.auth;

import com.example.ProyectoFinalTurnos.features.usuario.Dtos.LoginResponseDTO;
import com.example.ProyectoFinalTurnos.features.usuario.Dtos.LoginResquestDTO;
import com.example.ProyectoFinalTurnos.features.usuario.Dtos.UsuarioRequest;
import com.example.ProyectoFinalTurnos.features.usuario.UsuarioEntity;
import com.example.ProyectoFinalTurnos.features.usuario.UsuarioRepository;
import com.example.ProyectoFinalTurnos.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    // 1. Lógica de Registro
    public String registrar(UsuarioRequest request) {

        // (Opcional pero recomendado) Validar que el email no exista ya en la BD
        if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("El email ya está registrado");
            // Acá podés usar tu UserEmailAlreadyInUseException si la tenés armada
        }

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setNombre(request.getNombre());
        usuario.setApellido(request.getApellido());
        usuario.setEmail(request.getEmail());

        // ¡SÚPER IMPORTANTE! Encriptamos la contraseña
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));

        usuarioRepository.save(usuario);

        return "Usuario registrado con éxito. Por favor, inicie sesión.";
    }

    // 2. Lógica de Inicio de Sesión (Login)
    public LoginResponseDTO login(LoginResquestDTO request) {

        // El AuthenticationManager de Spring Security compara la contraseña encriptada por nosotros
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // Si pasa la línea anterior, las credenciales están bien. Buscamos al usuario:
        UsuarioEntity usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Fabricamos el Token JWT
        String jwtToken = jwtService.generateToken(usuario);

        // Devolvemos tu DTO armado
        return new LoginResponseDTO(true, "Inicio de sesión exitoso", jwtToken);
    }
}
