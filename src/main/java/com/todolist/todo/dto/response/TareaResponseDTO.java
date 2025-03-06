package com.todolist.todo.dto.response;
import com.todolist.todo.enumerator.EstadoTarea;
import com.todolist.todo.model.Usuario;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class TareaResponseDTO {
    private Long identificador;
    private String titulo;
    private String descripcion;
    private EstadoTarea estado;
    private UsuarioResumenDTO usuario;
    private List<CategoriaResumeDTO> categorias;
}
