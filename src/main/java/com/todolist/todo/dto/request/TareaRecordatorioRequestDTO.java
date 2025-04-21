package com.todolist.todo.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos requeridos para activar el recordatorio en la tarea")
public class TareaRecordatorioRequestDTO {
    @NotNull
    @Schema(description = "Fecha limite en el que debe cumplirse la tarea",example = "2025-04-04T10:15:30")
    private LocalDateTime fechaLimite;
    @Schema(description = "Dias de anticipacion para notificar, se notificara a partir de los dias especificados. " +
            "Los dias anticipados no deben exceder a los dias transcurridos desde la fecha actual con la fecha limite"
            ,example = "3")
    private Integer diasAnticipadosRecordatorio;


}
