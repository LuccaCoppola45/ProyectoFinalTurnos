package com.example.ProyectoFinalTurnos.features.comercio;

import com.example.ProyectoFinalTurnos.features.categoria.CategoriaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "comercio")
public class ComercioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String nombre;
    private String direccion;
    private int telefono;

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
}
