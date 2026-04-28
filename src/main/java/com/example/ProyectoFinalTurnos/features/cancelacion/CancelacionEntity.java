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

    @Column(name = "fecha_cancelacion", nullable = false)
    private LocalDateTime fechaCancelacion;

    @Column(name = "motivo", length = 500)
    private String motivo;

//    @Column(name = "monto_penalizacion")
//    private BigDecimal montoPenalizacion;


    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private UsuarioEntity usuario;

    @OneToOne
    @JoinColumn(name = "id_turno", referencedColumnName = "id_turno")
    private TurnoEntity turno;

}
