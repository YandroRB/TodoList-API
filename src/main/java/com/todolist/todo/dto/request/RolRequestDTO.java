package com.todolist.todo.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Datos requeridos para crear un rol")
public class RolRequestDTO {
    @Schema(description = "Nombre del rol a crear",example = "INVITADO")
    @NotBlank(message = "El campo nombreRol es requerido")
    private String nombreRol;
}
