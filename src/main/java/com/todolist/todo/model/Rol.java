package com.todolist.todo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rol {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    @Column(unique=true, nullable=false)
    private String nombreRol;
    @ManyToMany(mappedBy = "roles")
    private Set<Usuario> usuarios=new HashSet<>();
    @ManyToMany
    @JoinTable(
            name = "roles_permisos",
            joinColumns= @JoinColumn(name = "rol_id"),
            inverseJoinColumns = @JoinColumn(name = "permiso_id")
    )
    private Set<Permiso> permisos=new HashSet<>();
}
