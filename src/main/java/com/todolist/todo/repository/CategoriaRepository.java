package com.todolist.todo.repository;

import com.todolist.todo.model.Categoria;
import com.todolist.todo.model.Tarea;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface CategoriaRepository extends JpaRepository<Categoria,Long> {
    List<Categoria> findByTareasIdentificador(Long identificador);
    List<Categoria> findByUsuarioUsername(String username);
    boolean existsByIdentificadorAndUsuarioUsername(Long identificador, String username);
    boolean existsByNombreAndUsuarioUsername(String nombre, String username);
}
