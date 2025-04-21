package com.todolist.todo.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Schema(description = "Historial de  cambios de la tarea")
public class HistorialResponseDTO {
    @Schema(description = "ID unico del cambio",example = "1")
    private Long id;
    @Schema(description = "ID de la revision de la tarea",example = "3")
    private Long revisionId;
    @Schema(description = "Fecha del cambio",example = "2025/05/05")
    private LocalDateTime fechaCambio;
    @Schema(description = "Usuario que realizo el cambio",example = "pepito123")
    private String usuario;
    @Schema(description = "Tipo de cambio que se realizo",example = "Modificacion")
    private String tipoRvision;
    @Schema(description = "Descripcion del cambio que se hizo, antes y despues")
    private Map<String,CambioCampoDTO> cambios;
    @Data
    public static class CambioCampoDTO{
        private String valorAnterior;
        private String valorNuevo;
    }
}
