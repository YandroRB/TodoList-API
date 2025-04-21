package com.todolist.todo.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Datos requeridos para crear una categoria")
public class CategoriaRequestDTO {
    @NotBlank
    @Schema(description = "Nombre de la categoria",example = "Importantes")
    private String nombre;
}
