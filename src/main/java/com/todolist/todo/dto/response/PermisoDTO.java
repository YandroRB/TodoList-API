package com.todolist.todo.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Datos del permiso")
public class PermisoDTO {
    @Schema(description = "Nombre del permiso",example = "TAREA_LEER")
    private String nombrePermiso;
    @Schema(description = "Descripcion del permiso",example = "Permite obtener la lista del usuario")
    private String descripcionPermiso;
}
