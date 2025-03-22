package com.todolist.todo.repository;

import com.todolist.todo.model.Permiso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PermisoRepository extends JpaRepository<Permiso,Integer> {
    Optional<Permiso> findByNombrePermiso(String nombre);
}
