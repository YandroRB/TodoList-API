package com.todolist.todo.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class HistorialResponseDTO {
    private Long id;
    private Long revisionId;
    private LocalDateTime fechaCambio;
    private String usuario;
    private String tipoRvision;
    private Map<String,CambioCampoDTO> cambios;
    @Data
    public static class CambioCampoDTO{
        private String valorAnterior;
        private String valorNuevo;
    }
}
