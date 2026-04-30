package com.example.ProyectoFinalTurnos.features.servicio;

import com.example.ProyectoFinalTurnos.features.comercio.ComercioEntity;
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

    @NotNull(message = "El nombre del servicio es obligatorio")
    @Column(name = "nombre_servicio", length = 50)
    private String nombre;

    @NotNull(message = "El precio es obligatorio")
    private Double precio;

    // Relación con Comercio: Muchos servicios pertenecen a un Comercio
    @ManyToOne
    @JoinColumn(name = "id_comercio",
    foreignKey = @ForeignKey(name = "fk_servicio_comercio"))
    private ComercioEntity comercio;

    // 1. RELACIÓN MUCHOS A MUCHOS (Tabla Usuarios_Servicios)
    // Es el "espejo" de la relación que definimos en Usuario
    @ManyToMany(mappedBy = "serviciosVinculados")
    private List<UsuarioEntity> usuariosVinculados;

    // 2. RELACIÓN CON TURNOS (Tabla intermedia con datos)
    // Un servicio puede estar presente en muchos turnos agendados
    @OneToMany(mappedBy = "servicio")
    private List<TurnoEntity> turnos;
}
