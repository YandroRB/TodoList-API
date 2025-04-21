package com.todolist.todo.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.format.DecimalStyle;

@Data
@Schema(description = "Datos del usuario")
public class UsuarioRequestDTO {
    @Schema(description = "Nombre del usuario",example = "Pepe")
    @NotBlank(message = "El campo nombre es obligatorio")
    private String nombre;
    @Schema(description = "Apellido del usuario",example = "Pluas")
    @NotBlank(message = "El campo apellido es obligatorio")
    private String apellido;
    @Schema(description = "Correo electronico del usuario",example = "pepe123@gmail.com")
    @NotBlank(message = "El campo email es obligatorio")
    private String email;
    @Schema(description = "Apodo del usuario",example = "pepe123")
    @NotBlank(message = "El campo username es obligatorio")
    private String username;
    @Schema(description = "Contraseña del usuario",example = "123pepe")
    @NotBlank
    private String password;


    public boolean tieneCampo(){
        return nombre != null || apellido != null || email != null || username != null || password != null;
    }
}
