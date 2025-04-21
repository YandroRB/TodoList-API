package com.todolist.todo.documentation;

import com.todolist.todo.dto.response.HistorialResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
@Tag(name = "Historial",description = "Muestra el historial de cambios de una entidad.(Por ahora la entidad Tarea)")
public interface HistorialControllerDocumentation {

    @Operation(summary = "Obtiene el historial de cambios de una tarea", description = "Obtiene el historial a traves del id de la tarea." +
            "Requiere permiso HISTORIAL_TAREA -> para obtener el historial sin importar el propietario de la tarea" +
            "HISTORIAL_USUARIO_TAREA -> Obtiene el historial de la tarea asegurandose que la tarea le pertenezca"
            ,tags = {"Historial"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Historial de la tarea obtenido exitosamente",
                    content = @Content(mediaType = "application/json",schema = @Schema(type = "array",implementation = HistorialResponseDTO.class))),

            @ApiResponse(responseCode = "404",description = "No se encontró la tarea",content = @Content),
            @ApiResponse(responseCode = "403",description = "No tiene autorizacion para realizar esta operacion"
                    ,content = @Content(mediaType = "application/json"
                    ,schema = @Schema(type = "object",example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"}")))
    })
    ResponseEntity<List<HistorialResponseDTO>> obtenerHistorialTarea(
            @Parameter(
                    description = "ID de la tarea a obtener el historial",
                    required = true,
                    example = "1"
            )
            @PathVariable Long tareaId);
}
