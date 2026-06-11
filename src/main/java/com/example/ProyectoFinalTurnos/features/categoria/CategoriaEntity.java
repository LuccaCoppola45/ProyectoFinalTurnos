package com.example.ProyectoFinalTurnos.features.categoria;

import com.example.ProyectoFinalTurnos.features.comercio.ComercioEntity;
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

    // Relación ManyToMany para poder consultar los comercios asociados
    // y validar antes de eliminar (Integridad referencial)
    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL) // <-- Le sacamos la "s"
    private List<ComercioEntity> comercios;
}
