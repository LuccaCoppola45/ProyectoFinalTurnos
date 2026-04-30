package com.example.ProyectoFinalTurnos.features.usuario;

import com.example.ProyectoFinalTurnos.features.cancelacion.CancelacionEntity;
import com.example.ProyectoFinalTurnos.features.servicio.ServicioEntity;
import com.example.ProyectoFinalTurnos.features.turno.TurnoEntity;
import com.example.ProyectoFinalTurnos.model.enums.Rol;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table (name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;


    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3)
    private String nombre;


    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 3)
    private String apellido;

    @NotBlank(message = "El rol es obligatorio")
    private Rol rol;

    @NotBlank(message = "El email es obligatorio")
    @Email
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6)
    private String password;

    @ManyToMany
    @JoinTable(
            name = "usuarios_servicios", // Nombre de la tabla intermedia en MySQL
            joinColumns = @JoinColumn(name = "id_usuario", // FK de esta clase
            foreignKey = @ForeignKey(name = "FK_usuario_servicio_usuario")
            ), // FK hacia la tabla Padre (Usuario)
            inverseJoinColumns = @JoinColumn(name = "id_servicio",
            foreignKey = @ForeignKey(name = "FK_usuario_servicio_servicio") // FK hacia la tabla Hija (Servicio)// FK de la otra clase

            )
    )
    private List<ServicioEntity> serviciosVinculados;


    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<TurnoEntity> turnosVinculados;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<CancelacionEntity> cancelacionEntitiesVinculados;

}
