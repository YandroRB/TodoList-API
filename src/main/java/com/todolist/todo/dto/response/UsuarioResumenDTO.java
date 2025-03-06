package com.todolist.todo.dto.response;
import lombok.Data;

@Data
public class UsuarioResumenDTO {
    private Long identificador;
    private String nombre;
    private String apellido;
    private String email;
    private String username;
}
