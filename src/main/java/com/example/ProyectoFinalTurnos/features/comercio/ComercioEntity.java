package com.example.ProyectoFinalTurnos.features.comercio;

import com.example.ProyectoFinalTurnos.features.categoria.CategoriaEntity;
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
    private Long id;

    private String nombre;
    private String direccion;
    private String telefono;

    @ManyToOne
    @JoinColumn(name = "id_categoria", nullable = false, foreignKey = @ForeignKey(name = "fk_comercio_categoria"))
    private CategoriaEntity categoria;

    // Relación con servicios (Un comercio tiene muchos servicios)
    @OneToMany(mappedBy = "comercio", cascade = CascadeType.ALL)
    private List<ServicioEntity> servicios;


}
