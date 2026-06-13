package com.example.ProyectoFinalTurnos.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // 1. Verificar si el header existe y empieza con "Bearer " (el estándar de JWT)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // Si no hay token, dejamos que la petición siga su curso (quizás es una ruta pública como el /login)
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Extraer el token (sacando los primeros 7 caracteres: la palabra "Bearer ")
        jwt = authHeader.substring(7);

        // 3. Extraer el email del token usando tu JwtService
        userEmail = jwtService.extractUsername(jwt);

        // 4. Si encontramos un email y el usuario no está autenticado aún en el contexto actual
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Buscamos el usuario en la Base de Datos
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            // 5. Si el token es válido (le pertenece a ese usuario y no venció)
            if (jwtService.isTokenValid(jwt, userDetails)) {
                // Creamos el objeto de autenticación que Spring Security entiende
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null, // No pasamos la contraseña por seguridad
                        userDetails.getAuthorities()
                );

                // Le agregamos detalles extra sobre la petición web
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 6. ¡Le decimos a Spring que este usuario ya está autenticado y tiene permiso!
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 7. Continuar con el siguiente filtro en la cadena
        filterChain.doFilter(request, response);
    }
}
