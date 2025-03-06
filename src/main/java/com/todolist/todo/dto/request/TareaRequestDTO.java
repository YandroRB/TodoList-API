package com.todolist.todo.dto.request;

import com.todolist.todo.enumerator.EstadoTarea;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TareaRequestDTO {
    @NotBlank(message = "El titulo es obligatorio")
    private String titulo;
    @NotBlank(message = "La descripcion es obligatoria")
    private String descripcion;
    private EstadoTarea estado;

    public boolean tieneCampos(){
        return titulo!=null || descripcion!=null || estado!=null;
    }
}
