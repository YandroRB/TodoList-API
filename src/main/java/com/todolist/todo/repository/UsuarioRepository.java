package com.todolist.todo.repository;

import com.todolist.todo.model.Rol;
import com.todolist.todo.model.Usuario;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario,Long> {
    Optional<Usuario> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Optional<Usuario> findByEmail(String email);
    List<Usuario> findByRolesContaining(Rol rol);
    Optional<Usuario> findByTokenVerificacion(String token);
}
