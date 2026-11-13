package com.example.ProyectoFinalTurnos.features.auth;

import com.example.ProyectoFinalTurnos.exceptions.user.UserEmailAlreadyInUseException;
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
}
