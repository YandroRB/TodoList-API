package com.todolist.todo.dto.response;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponseDTO {
    private Long identificador;
    private String nombre;
    private String apellido;
    private String email;
    private String username;
    private List<TareaResumenDTO> tareas;
    private Set<CategoriaResumeDTO> categorias;
}
