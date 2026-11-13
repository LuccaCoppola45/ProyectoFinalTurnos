package com.example.ProyectoFinalTurnos.features.categoria;

import com.example.ProyectoFinalTurnos.features.comercio.ComercioEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "categoria")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria")
    private long uniqueId;

    @Column(name = "Nombre_categoria",nullable =false, length = 50)
    private String nombreCategoria;

    @ManyToMany(mappedBy = "categorias")
    @JsonIgnore
    private List<ComercioEntity> comercios;
}
