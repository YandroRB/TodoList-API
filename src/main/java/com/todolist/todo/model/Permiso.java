package com.todolist.todo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Permiso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer indentificador;
    @Column(unique=true, nullable=false)
    private String nombrePermiso;
    @Column(nullable=false)
    private String descripcionPermiso;
    @ManyToMany(mappedBy = "permisos")
    private Set<Rol> roles;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Permiso permiso = (Permiso) o;
        return Objects.equals(nombrePermiso, permiso.nombrePermiso);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(nombrePermiso);
    }
}
