package com.example.ProyectoFinalTurnos.features.foto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "fotos", uniqueConstraints = {
        @UniqueConstraint(name = "uk_foto_tipo_referencia", columnNames = {"tipo", "referencia_id"})
})
public class FotoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_foto")
    private Long idFoto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoFoto tipo;

    @Column(name = "referencia_id", nullable = false)
    private Long referenciaId;

    @Column(name = "content_type", nullable = false, length = 50)
    private String contentType;

    @Lob
    @Column(nullable = false, columnDefinition = "LONGBLOB")
    private byte[] datos;

    @Column(nullable = false)
    private LocalDateTime actualizada;
}
