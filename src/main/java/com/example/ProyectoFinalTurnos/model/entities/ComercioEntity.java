package com.example.ProyectoFinalTurnos.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class ComercioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String nombre;
    private String direccion;
    private int telefono;

    @ManyToMany
    @JoinTable(
            name = "comercio_categoria", // Nombre de la tabla intermedia
            joinColumns = @JoinColumn(name = "id_comercio"), // FK de esta clase
            inverseJoinColumns = @JoinColumn(name = "id_categoria") // FK de la otra clase
    )
    private List<Categoria> categorias;
}
