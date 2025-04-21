package com.todolist.todo.dto.request;

import com.todolist.todo.dto.response.CategoriaResumeDTO;
import com.todolist.todo.enumerator.EstadoTarea;
import com.todolist.todo.enumerator.PrioridadTarea;
import lombok.Data;

import java.util.List;
@Data
public class TareaExportDTO {
    private String titulo;
    private String descripcion;
    private EstadoTarea estado;
    private PrioridadTarea prioridad;
}
