package com.todolist.todo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TareaRecordatorioRequestDTO {
    @NotNull
    private LocalDateTime fechaLimite;
    private Integer diasAnticipadosRecordatorio;


}
