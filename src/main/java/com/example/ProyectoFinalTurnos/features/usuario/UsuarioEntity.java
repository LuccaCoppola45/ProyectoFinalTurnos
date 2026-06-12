package com.example.ProyectoFinalTurnos.features.usuario;

import com.example.ProyectoFinalTurnos.features.cancelacion.CancelacionEntity;
import com.example.ProyectoFinalTurnos.features.servicio.ServicioEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.example.ProyectoFinalTurnos.features.turno.TurnoEntity;
import com.example.ProyectoFinalTurnos.model.enums.Rol;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Table (name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class UsuarioEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;



    private String nombre;



    private String apellido;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "El rol es obligatorio")
    private Rol rol = Rol.CLIENTE;


    private String email;


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

    @JsonIgnore
    private List<ServicioEntity> serviciosVinculados;


    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private Set<TurnoEntity> turnosVinculados;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<CancelacionEntity> cancelacionEntitiesVinculados;


    @PrePersist
    public void init(){
        this.serviciosVinculados = new ArrayList<>();
        this.turnosVinculados = new HashSet<>();
        this.cancelacionEntitiesVinculados = new ArrayList<>();
    }


    // --- MÉTODOS OBLIGATORIOS DE USERDETAILS ---
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Transformamos el Rol en un GrantedAuthority que Spring entiende [2]
        return List.of(new SimpleGrantedAuthority(rol.name()));
    }

    @Override
    public String getUsername() {
        return this.email; // Usamos el email como nombre de usuario para el login
    }

    // Los siguientes métodos por defecto los dejamos en "true" para no bloquear la cuenta
    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return true; }
}

