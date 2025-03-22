package com.todolist.todo.repository;

import com.todolist.todo.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolRepository extends JpaRepository<Rol,Integer> {
    Optional<Rol> findByNombreRol(String name);
    boolean existsByNombreRol(String nombre);
    void deleteByNombreRol(String nombre);
}
