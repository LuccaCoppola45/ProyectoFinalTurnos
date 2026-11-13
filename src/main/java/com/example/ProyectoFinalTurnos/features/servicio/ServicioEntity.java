package com.example.ProyectoFinalTurnos.features.servicio;

import com.example.ProyectoFinalTurnos.features.comercio.ComercioEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.example.ProyectoFinalTurnos.features.horarioAtencion.HorarioAtencionEntity;
import com.example.ProyectoFinalTurnos.features.turno.TurnoEntity;
import com.example.ProyectoFinalTurnos.features.usuario.UsuarioEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "servicios")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class ServicioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_servicio")
    private Long idServicio;

    @Column(name = "nombre_servicio", length = 50)
    private String nombre;

    @Column(name = "duracion_servicio", nullable = false)
    private Integer duracionDelServicio;

    @Column(nullable = false)
    private Double precio;

    @ManyToOne
    @JoinColumn(name = "id_comercio",
    foreignKey = @ForeignKey(name = "fk_servicio_comercio"))
    @JsonIgnore
    private ComercioEntity comercio;

    @ManyToMany(mappedBy = "serviciosVinculados")
    @JsonIgnore
    private List<UsuarioEntity> usuariosVinculados;

    @OneToMany(mappedBy = "servicio")
    private List<TurnoEntity> turnos;

}
