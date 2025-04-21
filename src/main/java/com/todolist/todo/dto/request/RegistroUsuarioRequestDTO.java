package com.todolist.todo.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Datos requeridos para registrar un usuario")
public class RegistroUsuarioRequestDTO {
    @NotBlank
    @Schema(description = "Apodo del usuario",example = "pepito123")
    private String username;
    @Schema(description = "Contraseña del usuario",example = "123pepitoelmejor")
    @NotBlank
    private String password;
    @Schema(description = "Correo electronico del usuario",example ="pepito123@gmail.com")
    @NotBlank
    private String email;
    @Schema(description = "Nombre del usuario",example = "Pepe")
    @NotBlank
    private String nombre;
    @Schema(description = "Apellido del usuario",example = "Feliz")
    @NotBlank
    private String apellido;
}
