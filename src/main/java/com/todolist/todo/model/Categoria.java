package com.todolist.todo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"identificador","nombre"})
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long identificador;
    @Column(unique = true, nullable = false)
    private String nombre;
    @ManyToMany(mappedBy = "categorias")
    private Set<Tarea> tareas=new HashSet<>();
    @ManyToOne()
    @JoinColumn(name="usuario_id")
    private Usuario usuario;

}
