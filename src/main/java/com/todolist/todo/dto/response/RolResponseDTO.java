package com.todolist.todo.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Datos del rol")
public class RolResponseDTO {
    @Schema(description = "ID del rol",example = "1")
    private Integer identificador;
    @Schema(description = "Nombre del rol",example = "INVITADO")
    private String nombreRol;
    @Schema(description = "Lista de permisos que tiene el rol")
    private Set<PermisoDTO> permisos;
}
