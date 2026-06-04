package com.example.ProyectoFinalTurnos.features.cancelacion;

import com.example.ProyectoFinalTurnos.features.turno.TurnoEntity;
import com.example.ProyectoFinalTurnos.features.usuario.UsuarioEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cancelaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class CancelacionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha_cancelacion", nullable = false, updatable = false)
    private LocalDateTime fechaCancelacion;

    @Column(name = "motivo", length = 500, nullable = false, updatable = false)
    private String motivo;


    @ManyToOne
    @JoinColumn(name = "id_usuario",
            nullable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_cancelacion_usuario"))
    private UsuarioEntity usuario;

    @OneToOne
    @JoinColumn(name = "id_turno", referencedColumnName = "id_turno", nullable = false,
            updatable = false,
    foreignKey = @ForeignKey (name = "fk_cancelacion_turno"))
    private TurnoEntity turno;

}
