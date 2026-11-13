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
import com.example.ProyectoFinalTurnos.features.comercio.ComercioEntity;

@Entity
@Table (name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class UsuarioEntity implements UserDetails{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @Enumerated(EnumType.STRING)
    private Rol rol = Rol.CLIENTE;

    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @ManyToMany
    @JoinTable(
            name = "usuarios_servicios",
            joinColumns = @JoinColumn(name = "id_usuario",
            foreignKey = @ForeignKey(name = "FK_usuario_servicio_usuario")
            ),
            inverseJoinColumns = @JoinColumn(name = "id_servicio",
            foreignKey = @ForeignKey(name = "FK_usuario_servicio_servicio")

            )
    )

    @JsonIgnore
    private List<ServicioEntity> serviciosVinculados;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private Set<TurnoEntity> turnosVinculados;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<CancelacionEntity> cancelacionEntitiesVinculados;

    @OneToMany(mappedBy = "usuarioPropietario", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<ComercioEntity> comerciosPropios;

    @PrePersist
    public void init(){
        this.serviciosVinculados = new ArrayList<>();
        this.turnosVinculados = new HashSet<>();
        this.cancelacionEntitiesVinculados = new ArrayList<>();
        this.comerciosPropios = new ArrayList<>();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(rol.name()));
    }

    @Override
    public String getUsername() { return this.email;
    }

    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override

    public boolean isEnabled() { return true; }

}
