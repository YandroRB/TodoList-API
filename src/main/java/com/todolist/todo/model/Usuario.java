package com.todolist.todo.model;

import com.todolist.todo.enumerator.TipoRol;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long identificador;

    @NotBlank
    private String nombre;

    @NotBlank
    private String apellido;

    @Column(nullable = false,unique = true)
    private String email;

    @Column(nullable = false,unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "roles_usuario",joinColumns = @JoinColumn(name = "usuario_id"))
    @Column(name = "rol")
    private Set<TipoRol> roles = new HashSet<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Tarea> tareas=new ArrayList<>();


    @OneToMany(mappedBy = "usuario",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<Categoria> categorias=new HashSet<>();

    public void addTarea(Tarea tarea) {
        this.tareas.add(tarea);
        tarea.setUsuario(this);
    }
    public void removeTarea(Tarea tarea) {
        this.tareas.remove(tarea);
        tarea.setUsuario(null);
    }

    public Categoria addCategoria(Categoria categoria) {
        this.categorias.add(categoria);
        categoria.setUsuario(this);
        return categoria;
    }
    public void removeCategoria(Categoria categoria) {
        this.categorias.remove(categoria);
        categoria.setUsuario(null);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(rol->new SimpleGrantedAuthority("ROLE_"+rol.name())).toList();
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }
}
