package com.todolist.todo.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Datos de la categoria")
public class CategoriaResumeDTO {
    @Schema(description = "ID unico de la categoria",example = "1")
    private Long identificador;
    @Schema(description = "Nombre de la categoria",example = "Importante")
    private String nombre;
}
