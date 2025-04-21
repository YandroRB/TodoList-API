package com.todolist.todo.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Datos requeridos para iniciar sesion")
public class LoginRequestDTO {
    @Schema(description = "username del usuario",example = "admin")
    private String username;
    @Schema(description = "contraseña del usuario",example = "admin123")
    private String password;
}
