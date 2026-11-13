package com.example.ProyectoFinalTurnos.features.turno;

import com.example.ProyectoFinalTurnos.features.cancelacion.CancelacionEntity;
import com.example.ProyectoFinalTurnos.features.comercio.ComercioEntity;
import com.example.ProyectoFinalTurnos.features.servicio.ServicioEntity;
import com.example.ProyectoFinalTurnos.features.usuario.UsuarioEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.example.ProyectoFinalTurnos.model.enums.EstadoTurno;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "turnos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TurnoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_turno")
    private Long idTurno;

    @Column(nullable = false)
    private LocalDate fecha;


    @Column(nullable = false)
    private LocalTime horaInicio;

    @Column(nullable = false)
    private LocalTime horaFin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoTurno estado; // Pendiente, Confirmado, etc.

    // RELACIÓN: Muchos turnos pueden ser de un Usuario
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario",
    foreignKey = @ForeignKey(name = "fk_turno_usuario"))
    @JsonIgnore
    private UsuarioEntity usuario;

    // RELACIÓN: Muchos turnos pueden ser para un Servicio
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_servicio",
    foreignKey = @ForeignKey (name = "fk_turno_servicio"))
    @JsonIgnore
    private ServicioEntity servicio;

    // RELACIÓN: Muchos turnos pertenecen a un Comercio
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_comercio",
            foreignKey = @ForeignKey(name = "fk_turno_comercio"))
    @JsonIgnore
    private ComercioEntity comercio;

    // RELACIÓN 1 a 1: Un turno puede tener una cancelación
    // 'mappedBy' indica que la FK está en la tabla Cancelacion
    @OneToOne(mappedBy = "turno", cascade = CascadeType.ALL)
    private CancelacionEntity cancelacion;

    // ===== Recordatorio 24 h antes (RecordatorioDeTurnos) =====

    /** true cuando ya se mandó el mail "¿Vas a ir?" (null en turnos viejos = no se mandó). */
    @Column(name = "recordatorio_enviado")
    private Boolean recordatorioEnviado;

    /** Código único que va en el link del mail para confirmar o cancelar sin iniciar sesión. */
    @Column(name = "token_confirmacion", length = 36, unique = true)
    @JsonIgnore
    private String tokenConfirmacion;
}
