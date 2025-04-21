package com.todolist.todo.dto.response;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos del usuario")
public class UsuarioResponseDTO {
    @Schema(description = "ID unico del usuario",example = "1")
    private Long identificador;
    @Schema(description = "Nombre del usuario",example = "Pepe")
    private String nombre;
    @Schema(description = "Apellido del usuario",example = "Pluas")
    private String apellido;
    @Schema(description = "Correo del usuario",example = "pepe123@gmail.com")
    private String email;
    @Schema(description = "Apodo del usuario",example = "pepe123")
    private String username;
    @Schema(description = "Lista de las tareas que tiene el usuario")
    private List<TareaResumenDTO> tareas;
    @Schema(description = "Lista de categorias que tiene el usuario para las tareas")
    private Set<CategoriaResumeDTO> categorias;
    private boolean requiereLogin;
}
