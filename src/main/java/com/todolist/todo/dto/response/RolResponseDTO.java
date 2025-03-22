package com.todolist.todo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RolResponseDTO {
    private Integer identificador;
    private String nombreRol;
    private Set<PermisoDTO> permisos;
}
