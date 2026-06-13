package com.example.ProyectoFinalTurnos.features.comercio;

import com.example.ProyectoFinalTurnos.features.categoria.CategoriaEntity;
import com.example.ProyectoFinalTurnos.features.horarioAtencion.HorarioAtencionEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.example.ProyectoFinalTurnos.features.servicio.ServicioEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "comercio", uniqueConstraints = {
    @UniqueConstraint(name = "uk_comercio_nombre_direccion", columnNames = {"nombre", "direccion"})
})
public class ComercioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_comercio")
    private Long idComercio;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String direccion;

    @Column(nullable = false)
    private String telefono;

    @ManyToMany
    @JoinTable(
            name = "comercio_categoria",
            joinColumns = @JoinColumn(
                    name = "id_comercio",
                    foreignKey = @ForeignKey(name = "fk_comercio_categoria_comercio")
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "id_categoria",
                    foreignKey = @ForeignKey(name = "fk_comercio_categoria_categoria")
            )
    )
    private List<CategoriaEntity> categorias;

    @OneToMany(mappedBy = "comercio")
    @JsonIgnore
    private List<ServicioEntity> servicios;

    @OneToMany(
            mappedBy = "comercio",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JsonIgnore
    private List<HorarioAtencionEntity> horarios;


}
