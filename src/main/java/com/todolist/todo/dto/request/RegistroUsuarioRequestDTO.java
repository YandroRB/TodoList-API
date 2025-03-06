package com.todolist.todo.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistroUsuarioRequestDTO {
    @NotBlank
    private String username;
    @NotBlank
    private String password;

    @NotBlank
    private String email;
    @NotBlank
    private String nombre;
    @NotBlank
    private String apellido;
}
