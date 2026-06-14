package com.example.ProyectoFinalTurnos.features.horarioAtencion;

import com.example.ProyectoFinalTurnos.features.comercio.ComercioEntity;
import com.example.ProyectoFinalTurnos.features.servicio.ServicioEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.example.ProyectoFinalTurnos.model.enums.Diasemana;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalTime;

@Entity
@Table(name = "horarios_atencion")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class HorarioAtencionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_HorarioAtencion")
    private Long idHorarioAtencion;

    // MODIFICADO: Ahora es nullable = true (para cuando el horario sea de un peluquero)
    @ManyToOne
    @JoinColumn(name = "id_comercio",
            foreignKey = @ForeignKey(name = "FK_horario_comercio"),
            nullable = true)
    @JsonIgnore
    private ComercioEntity comercio;


    @Enumerated(EnumType.STRING)
    @Column(name = "dia_semana", nullable = false)
    private Diasemana diaSemana;

    @Column(name = "hora_apertura", nullable = false)
    private LocalTime horaApertura;

    @Column(name = "hora_cierre", nullable = false)
    private LocalTime horaCierre;

}
