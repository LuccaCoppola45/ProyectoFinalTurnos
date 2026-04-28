package com.example.ProyectoFinalTurnos.features.turno;

import com.example.ProyectoFinalTurnos.features.cancelacion.CancelacionEntity;
import com.example.ProyectoFinalTurnos.features.servicio.ServicioEntity;
import com.example.ProyectoFinalTurnos.features.usuario.UsuarioEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TurnoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_turno")
    private Long idTurno;

    @NotBlank(message = "La fecha es obligatoria")
    private LocalDate fecha;

    @NotBlank(message = "La hora de inicio es obligatoria")
    private LocalTime horaInicio;

    @NotBlank(message = "La hora de fin es obligatoria")
    private LocalTime horaFin;

    private String estado; // Pendiente, Confirmado, etc.

    // RELACIÓN: Muchos turnos pueden ser de un Usuario
    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private UsuarioEntity usuario;

    // RELACIÓN: Muchos turnos pueden ser para un Servicio
    @ManyToOne
    @JoinColumn(name = "id_servicio")
    private ServicioEntity servicio;

    // RELACIÓN 1 a 1: Un turno puede tener una cancelación
    // 'mappedBy' indica que la FK está en la tabla Cancelacion
    @OneToOne(mappedBy = "turno", cascade = CascadeType.ALL)
    private CancelacionEntity cancelacion;
}
