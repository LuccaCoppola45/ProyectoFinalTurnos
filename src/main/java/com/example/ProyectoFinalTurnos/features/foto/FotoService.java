package com.example.ProyectoFinalTurnos.features.foto;

import com.example.ProyectoFinalTurnos.exceptions.catalog.ComercioNotFoundException;
import com.example.ProyectoFinalTurnos.exceptions.foto.FotoInvalidaException;
import com.example.ProyectoFinalTurnos.exceptions.foto.FotoNotFoundException;
import com.example.ProyectoFinalTurnos.exceptions.user.UnauthorizedUserAccessException;
import com.example.ProyectoFinalTurnos.exceptions.user.UserProfileNotFoundException;
import com.example.ProyectoFinalTurnos.features.comercio.ComercioEntity;
import com.example.ProyectoFinalTurnos.features.comercio.ComercioRepository;
import com.example.ProyectoFinalTurnos.features.usuario.UsuarioEntity;
import com.example.ProyectoFinalTurnos.features.usuario.UsuarioRepository;
import com.example.ProyectoFinalTurnos.model.enums.Rol;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FotoService {

    /** Tamaño máximo de una foto: 2 MB. */
    public static final long TAMANIO_MAXIMO = 2L * 1024 * 1024;

    private final FotoRepository fotoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ComercioRepository comercioRepository;

    @Transactional(readOnly = true)
    public FotoEntity obtener(TipoFoto tipo, Long referenciaId) {
        return fotoRepository.findByTipoAndReferenciaId(tipo, referenciaId)
                .orElseThrow(() -> new FotoNotFoundException("No hay foto cargada"));
    }

    @Transactional
    public void guardar(TipoFoto tipo, Long referenciaId, MultipartFile archivo) {
        verificarPermiso(tipo, referenciaId);

        byte[] datos = leerYValidar(archivo);
        String contentType = detectarTipoDeImagen(datos);

        FotoEntity foto = fotoRepository.findByTipoAndReferenciaId(tipo, referenciaId)
                .orElseGet(FotoEntity::new);
        foto.setTipo(tipo);
        foto.setReferenciaId(referenciaId);
        foto.setContentType(contentType);
        foto.setDatos(datos);
        foto.setActualizada(LocalDateTime.now());
        fotoRepository.save(foto);
    }

    @Transactional
    public void eliminar(TipoFoto tipo, Long referenciaId) {
        verificarPermiso(tipo, referenciaId);
        fotoRepository.deleteByTipoAndReferenciaId(tipo, referenciaId);
    }

    // ---------- Permisos ----------

    /** Solo el dueño (el propio usuario o el propietario del comercio) o un ADMIN pueden cambiar la foto. */
    private void verificarPermiso(TipoFoto tipo, Long referenciaId) {
        UsuarioEntity actual = usuarioAutenticado();
        if (actual.getRol() == Rol.ADMIN) {
            existeOFalla(tipo, referenciaId);
            return;
        }

        boolean esDuenio = switch (tipo) {
            case USUARIO -> {
                existeOFalla(tipo, referenciaId);
                yield actual.getIdUsuario().equals(referenciaId);
            }
            case COMERCIO -> {
                ComercioEntity comercio = comercioRepository.findById(referenciaId)
                        .orElseThrow(() -> new ComercioNotFoundException("Comercio no encontrado con id: " + referenciaId));
                yield comercio.getUsuarioPropietario() != null
                        && comercio.getUsuarioPropietario().getIdUsuario().equals(actual.getIdUsuario());
            }
        };

        if (!esDuenio) {
            throw new UnauthorizedUserAccessException("No podés cambiar la foto de otro usuario o comercio");
        }
    }

    private void existeOFalla(TipoFoto tipo, Long referenciaId) {
        switch (tipo) {
            case USUARIO -> {
                if (!usuarioRepository.existsById(referenciaId)) {
                    throw new UserProfileNotFoundException("Usuario no encontrado");
                }
            }
            case COMERCIO -> {
                if (!comercioRepository.existsById(referenciaId)) {
                    throw new ComercioNotFoundException("Comercio no encontrado con id: " + referenciaId);
                }
            }
        }
    }

    private UsuarioEntity usuarioAutenticado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) {
            throw new UnauthorizedUserAccessException("No autenticado");
        }
        return usuarioRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new UserProfileNotFoundException("Usuario autenticado no encontrado"));
    }

    // ---------- Validación del archivo ----------

    private byte[] leerYValidar(MultipartFile archivo) {
        if (archivo == null || archivo.isEmpty()) {
            throw new FotoInvalidaException("No se recibió ninguna imagen");
        }
        if (archivo.getSize() > TAMANIO_MAXIMO) {
            throw new FotoInvalidaException("La imagen no puede superar los 2 MB");
        }
        try {
            return archivo.getBytes();
        } catch (IOException e) {
            throw new FotoInvalidaException("No se pudo leer la imagen");
        }
    }

    /**
     * Se mira el contenido real del archivo (sus primeros bytes) en vez de confiar en
     * la extensión o en el Content-Type que manda el navegador, que se pueden falsear.
     */
    private String detectarTipoDeImagen(byte[] d) {
        if (d.length >= 3 && (d[0] & 0xFF) == 0xFF && (d[1] & 0xFF) == 0xD8 && (d[2] & 0xFF) == 0xFF) {
            return "image/jpeg";
        }
        if (d.length >= 8 && (d[0] & 0xFF) == 0x89 && d[1] == 'P' && d[2] == 'N' && d[3] == 'G'
                && d[4] == 0x0D && d[5] == 0x0A && d[6] == 0x1A && d[7] == 0x0A) {
            return "image/png";
        }
        if (d.length >= 12 && d[0] == 'R' && d[1] == 'I' && d[2] == 'F' && d[3] == 'F'
                && d[8] == 'W' && d[9] == 'E' && d[10] == 'B' && d[11] == 'P') {
            return "image/webp";
        }
        throw new FotoInvalidaException("Formato no soportado. Subí una imagen JPG, PNG o WEBP");
    }
}
