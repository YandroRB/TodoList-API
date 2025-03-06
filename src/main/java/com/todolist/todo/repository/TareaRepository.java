package com.todolist.todo.repository;

import com.todolist.todo.model.Tarea;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TareaRepository extends JpaRepository<Tarea,Long> {
    List<Tarea> findByUsuarioUsername(String username);
    boolean existsByIdentificadorAndUsuarioUsername(Long identificador, String username);
    List<Tarea> findByCategoriasNombre(String nombre);
    List<Tarea> findByCategoriasNombreLike(String nombre);
    boolean existsByIdentificadorAndCategoriasIdentificadorAndUsuarioUsername(Long identificador, Long categoriasIdentificador, String username);

}
