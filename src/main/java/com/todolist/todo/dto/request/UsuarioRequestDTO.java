package com.todolist.todo.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UsuarioRequestDTO {
    @NotBlank(message = "El campo nombre es obligatorio")
    private String nombre;
    @NotBlank(message = "El campo apellido es obligatorio")
    private String apellido;
    @NotBlank(message = "El campo email es obligatorio")
    private String email;
    @NotBlank(message = "El campo username es obligatorio")
    private String username;
    @NotBlank
    private String password;


    public boolean tieneCampo(){
        return nombre != null || apellido != null || email != null || username != null || password != null;
    }
}
