package com.todolist.todo.dto.request;

import com.todolist.todo.enumerator.EstadoTarea;
import com.todolist.todo.enumerator.PrioridadTarea;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Datos que se requieren para crear una tarea")
public class TareaRequestDTO {
    @Schema(description = "Titulo de la tarea",example = "Tarea 1")
    @NotBlank(message = "El titulo es obligatorio")
    private String titulo;
    @Schema(description = "Descripcion de la tarea",example = "Descripcion de la tarea que se esta realizando porque quieres realizarla")
    @NotBlank(message = "La descripcion es obligatoria")
    private String descripcion;
    @Schema(description = "Estado que le quiere indicar a la tarea",allowableValues = {"COMPLETA","PENDIENTE","EN_PROGRESO",
    "CANCELADA"})
    private EstadoTarea estado;
    @Schema(description = "El nivel de importancia que debe de tener la tarea",allowableValues = {"BAJA","MEDIA","ALTA","URGENTE"})
    private PrioridadTarea prioridad;

    public boolean tieneCampos(){
        return titulo!=null || descripcion!=null || estado!=null|| prioridad!=null;
    }
}
