package com.example.ProyectoFinalTurnos.features.auth;

import com.example.ProyectoFinalTurnos.exceptions.auth.VerificationTokenInvalidException;
import com.example.ProyectoFinalTurnos.exceptions.user.UserEmailAlreadyInUseException;
import com.example.ProyectoFinalTurnos.features.auth.dtos.OlvidePasswordRequest;
import com.example.ProyectoFinalTurnos.features.auth.dtos.RestablecerPasswordRequest;
import com.example.ProyectoFinalTurnos.features.notificacion.RecuperarPasswordEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;
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
    private final ApplicationEventPublisher eventPublisher;

    private static final int MINUTOS_VALIDEZ_RESET = 30;

    public String registrar(UsuarioRequest request) {

        if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserEmailAlreadyInUseException("El email ya está registrado");
        }

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setNombre(request.getNombre());
        usuario.setApellido(request.getApellido());
        usuario.setEmail(request.getEmail());

        usuario.setPassword(passwordEncoder.encode(request.getPassword()));

        usuarioRepository.save(usuario);

        return "Usuario registrado con éxito. Por favor, inicie sesión.";
    }

    public LoginResponseDTO login(LoginResquestDTO request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        UsuarioEntity usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String jwtToken = jwtService.generateToken(usuario);

        return new LoginResponseDTO(true, "Inicio de sesión exitoso", jwtToken);
    }

    /**
     * Genera un token de un solo uso y manda el mail de recuperación.
     * Responde siempre lo mismo para no revelar qué emails están registrados.
     */
    @Transactional
    public String solicitarRecuperacion(OlvidePasswordRequest request) {
        usuarioRepository.findByEmail(request.email()).ifPresent(usuario -> {
            String token = UUID.randomUUID().toString();
            usuario.setResetPasswordToken(token);
            usuario.setResetPasswordExpira(LocalDateTime.now().plusMinutes(MINUTOS_VALIDEZ_RESET));
            usuarioRepository.save(usuario);

            eventPublisher.publishEvent(new RecuperarPasswordEvent(
                    usuario.getEmail(), usuario.getNombre(), token, MINUTOS_VALIDEZ_RESET));
        });

        return "Si el email está registrado, te enviamos un enlace para restablecer la contraseña.";
    }

    @Transactional
    public String restablecerPassword(RestablecerPasswordRequest request) {
        UsuarioEntity usuario = usuarioRepository.findByResetPasswordToken(request.token())
                .orElseThrow(() -> new VerificationTokenInvalidException("El enlace de recuperación no es válido."));

        if (usuario.getResetPasswordExpira() == null
                || usuario.getResetPasswordExpira().isBefore(LocalDateTime.now())) {
            throw new VerificationTokenInvalidException("El enlace de recuperación venció. Pedí uno nuevo.");
        }

        usuario.setPassword(passwordEncoder.encode(request.nuevaPassword()));
        invalidarToken(usuario);

        return "La contraseña se actualizó correctamente. Ya podés iniciar sesión.";
    }

    private void invalidarToken(UsuarioEntity usuario) {
        usuario.setResetPasswordToken(null);
        usuario.setResetPasswordExpira(null);
        usuarioRepository.save(usuario);
    }
}
