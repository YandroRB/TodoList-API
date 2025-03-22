package com.todolist.todo.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RolRequestDTO {
    @NotBlank(message = "El campo nombreRol es requerido")
    private String nombreRol;
}
