package com.example.ProyectoFinalTurnos.features.foto;

import lombok.RequiredArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import java.time.ZoneId;

@RestController
@RequiredArgsConstructor
public class FotoController {

    private final FotoService fotoService;

    @GetMapping("/usuarios/{id}/foto")
    public ResponseEntity<byte[]> verFotoUsuario(@PathVariable Long id, WebRequest request) {
        return responderFoto(fotoService.obtener(TipoFoto.USUARIO, id), request);
    }

    @PutMapping(value = "/usuarios/{id}/foto", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> subirFotoUsuario(@PathVariable Long id, @RequestParam("foto") MultipartFile foto) {
        fotoService.guardar(TipoFoto.USUARIO, id, foto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/usuarios/{id}/foto")
    public ResponseEntity<Void> eliminarFotoUsuario(@PathVariable Long id) {
        fotoService.eliminar(TipoFoto.USUARIO, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/comercios/{id}/foto")
    public ResponseEntity<byte[]> verFotoComercio(@PathVariable Long id, WebRequest request) {
        return responderFoto(fotoService.obtener(TipoFoto.COMERCIO, id), request);
    }

    @PutMapping(value = "/api/comercios/{id}/foto", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> subirFotoComercio(@PathVariable Long id, @RequestParam("foto") MultipartFile foto) {
        fotoService.guardar(TipoFoto.COMERCIO, id, foto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/api/comercios/{id}/foto")
    public ResponseEntity<Void> eliminarFotoComercio(@PathVariable Long id) {
        fotoService.eliminar(TipoFoto.COMERCIO, id);
        return ResponseEntity.noContent().build();
    }

    private ResponseEntity<byte[]> responderFoto(FotoEntity foto, WebRequest request) {
        String etag = "\"" + foto.getActualizada().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() + "\"";
        if (request.checkNotModified(etag)) {
            return null;
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(foto.getContentType()))
                .cacheControl(CacheControl.noCache())
                .eTag(etag)
                .body(foto.getDatos());
    }
}
