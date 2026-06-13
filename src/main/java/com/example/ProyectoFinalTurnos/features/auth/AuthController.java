package com.example.ProyectoFinalTurnos.features.auth;

import com.example.ProyectoFinalTurnos.features.usuario.Dtos.LoginResquestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;


    @PostMapping("/login")
    public ResponseEntity<LoginResquestDTO> login(@RequestBody LoginResquestDTO loginResquestDTO){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginResquestDTO.getEmail(),
                        loginResquestDTO.getPassword()
                )
        );
        return ResponseEntity.ok(new LoginResquestDTO());
    }
}
