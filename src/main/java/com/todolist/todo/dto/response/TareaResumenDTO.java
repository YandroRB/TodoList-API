package com.todolist.todo.dto.response;
import com.todolist.todo.enumerator.EstadoTarea;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class TareaResumenDTO {
    private Long identificador;
    private String titulo;
    private String descripcion;
    private EstadoTarea estado;
}
