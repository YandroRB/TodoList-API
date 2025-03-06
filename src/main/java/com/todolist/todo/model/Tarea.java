package com.todolist.todo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.todolist.todo.enumerator.EstadoTarea;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class Tarea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long identificador;
    @NotBlank
    private String titulo;
    @NotBlank
    private String descripcion;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoTarea estado;
    @ManyToOne()
    @JoinColumn(name="usuario_id")
    private Usuario usuario;

    @ManyToMany
    @JoinTable(
            name = "categorias_tareas",
            joinColumns = @JoinColumn(name = "tarea_id"),
            inverseJoinColumns = @JoinColumn(name="categoria_id")
    )
    private Set<Categoria> categorias=new HashSet<>();
    @PrePersist
    protected void onCreate() {
        if(estado==null)estado=EstadoTarea.PENDIENTE;
    }
    public void addCategoria(Categoria categoria) {
        categorias.add(categoria);
    }
    public void removeCategoria(Categoria categoria) {
        categorias.remove(categoria);
    }
}
