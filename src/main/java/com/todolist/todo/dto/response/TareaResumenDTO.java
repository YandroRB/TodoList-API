package com.todolist.todo.dto.response;
import com.todolist.todo.enumerator.EstadoTarea;
import com.todolist.todo.enumerator.PrioridadTarea;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Schema(description = "Datos basicos de la tarea")
@Data
public class TareaResumenDTO {
    @Schema(description = "ID unico de la tarea",example = "1")
    private Long identificador;
    @Schema(description = "Titulo de la tarea",example = "Tarea 1")
    private String titulo;
    @Schema(description = "Descripcion de la tarea",example = "La tarea a realizar es muy importante por aeso lo agrego")
    private String descripcion;
    @Schema(description = "Estado de la tarea",allowableValues = {"COMPLETA","PENDIENTE","EN_PROGRESO",
            "CANCELADA"})
    private EstadoTarea estado;
    @Schema(description = "Prioridad de la tarea",allowableValues = {"BAJA","MEDIA","ALTA","URGENTE"})
    private PrioridadTarea prioridad;
}
