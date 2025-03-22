package com.todolist.todo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PermisoDTO {
    private String nombrePermiso;
    private String descripcionPermiso;
}
