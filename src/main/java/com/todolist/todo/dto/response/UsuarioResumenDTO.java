package com.todolist.todo.dto.response;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Datos basico del usuario")
public class UsuarioResumenDTO {
    @Schema(description = "ID unico del usuario",example = "1")
    private Long identificador;
    @Schema(description = "Nombre del usuario",example = "Pepe")
    private String nombre;
    @Schema(description = "Apellido del usuario",example = "Pluas")
    private String apellido;
    @Schema(description = "Correo electronico del usuario",example = "pepe123@gmail.com")
    private String email;
    @Schema(description = "Apodo del usuario",example = "pepe123")
    private String username;
}
