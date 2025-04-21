package com.todolist.todo.dto.response;
import com.todolist.todo.enumerator.EstadoTarea;
import com.todolist.todo.enumerator.PrioridadTarea;
import com.todolist.todo.model.Usuario;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@Schema(description = "Datos de la tarea")
public class TareaResponseDTO {
    @Schema(description = "ID de la tarea",example = "1")
    private Long identificador;
    @Schema(description = "Titulo de la tarea",example = "Tarea 1")
    private String titulo;
    @Schema(description = "Descripcion de la tarea",example = "La tarea es muy importante para mi porque debo realizarla")
    private String descripcion;
    @Schema(description = "Estado de la tarea",allowableValues = {"COMPLETA","PENDIENTE","EN_PROGRESO",
            "CANCELADA"})
    private EstadoTarea estado;
    @Schema(description = "Datos del dueño de la tarea")
    private UsuarioResumenDTO usuario;
    @Schema(description = "Lista de usuarios con los que se ha compartido la tarea")
    private List<UsuarioResumenDTO> usuariosCompartidos;
    @Schema(description = "Dias de anticipacion para notificar la expiracion de la tarea",example = "3")
    private Integer diasAnticipadosRecordatorio;
    @Schema(description = "Nivel de importancia de la tarea",allowableValues = {"BAJA","MEDIA","ALTA","URGENTE"})
    private PrioridadTarea prioridad;
    @Schema(description = "Booleano que inidica que la notificacion de la tarea esta activada",example = "true")
    private boolean recordatorioActivado;
    @Schema(description = "Fecha limite que expira la tarea",example = "2025-04-04T10:15:30")
    private LocalDateTime fechaLimite;
    @Schema(description = "Lista de etiquetas o categorias en la que esta la tarea")
    private List<CategoriaResumeDTO> categorias;
    @Schema(description = "Lista de subtareas que tiene la tarea")
    private List<TareaResumenDTO> subTarea;
}
