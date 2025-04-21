package com.todolist.todo.documentation;

import com.todolist.todo.dto.request.CategoriaRequestDTO;
import com.todolist.todo.dto.response.CategoriaResumeDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
@Tag(name="Categoria",description = "Etiquetas para categorizar las tareas de los usuarios")
public interface CategoriaControllerDocumentation {

    @Operation(summary = "Obtiene las categorias creadas por el usuario", description = "Obtiene las categorias que ha creado el usuario" +
            ".Requiere el permiso CATEGORIA_LEER",tags = {"Categoria"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Categorias obtenidas exitosamente",
            content=@Content(mediaType = "application/json",schema = @Schema(type = "array",implementation = CategoriaResumeDTO.class))),
            @ApiResponse(responseCode = "403",description = "No tiene autorizacion para realizar esta operacion"
                    ,content = @Content(mediaType = "application/json"
                    ,schema = @Schema(type = "object",example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"}")))
    })
    List<CategoriaResumeDTO> obtenerCategoriasPorUsuario(Authentication authentication);

    @Operation(summary = "Obtiene categorias por la tarea",description = "Obtiene las categorias de una tarea a traves" +
            "del id de la misma. Requiere el permiso CATEGORIA_TAREA_LEER -> Sin importa si le pertenece o no la tarea" +
            "CATEGORIA_TAREA_USUARIO_LEER -> para segurar que la tarea le pertenezca al usuario"
            ,tags = {"Categoria"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Se ha obtenido la tarea exitosamente"
                    ,content = @Content(mediaType = "application/json",schema = @Schema(implementation = CategoriaResumeDTO.class))),
            @ApiResponse(responseCode = "404", description = "Tarea no encontrada"
                    ,content = @Content(mediaType = "application/json"
                    ,schema = @Schema(type = "object",example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"}"))),
            @ApiResponse(responseCode = "403",description = "No tiene autorizacion para realizar esta operacion"
                    ,content = @Content(mediaType = "application/json"
                    ,schema = @Schema(type = "object",example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"}")))
    })
    List<CategoriaResumeDTO> obtenerCategoriasPorTarea(
            @Parameter(
                    description = "ID de la tarea a buscar las categorias",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id_tarea);

    @Operation(summary = "Elimina una categoria", description = "Elimina una categoria por id" +
            ".Requiere permiso CATEGORIA_ID_ELIMINAR -> Elimina sin importar si le pertenece, " +
            "CATEGORIA_ID_USUARIO_ELIMINAR -> Verifica que le pertenece"
            ,tags = {"Categoria"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",description = "Se ha eliminado la categoria correctamente"
                    ,content = @Content),
            @ApiResponse(responseCode = "404",description = "No se encontró la categoria"
                    ,content = @Content(mediaType = "application/json"
                    ,schema = @Schema(type = "object",example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"}"))),
            @ApiResponse(responseCode = "403", description = "No tiene autorizacion para realizar esta operacion"
                    ,content = @Content(mediaType = "application/json"
                    ,schema = @Schema(type = "object",example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"}")))
    })
    ResponseEntity<?> eliminarCategoria(
            @Parameter(
                    description = "ID de la categoria a eliminar",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id_categoria);

    @Operation(summary = "Actualiza los datos de una categoria",description = "Busca por id la categoria a editar sus datos" +
            ".Requiere permiso CATEGORIA_ID_ACTUALIZAR -> Para actualizar sin importar si le pertenece," +
            "CATEGORIA_ID_USUARIO_ACTUALIZAR -> Para actualizar comprobando que le pertenezca"
            ,tags = {"Categoria"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Se ha actualizado correctamente la categoria"
                    ,content = @Content(mediaType = "application/json",schema = @Schema(implementation = CategoriaResumeDTO.class))),
            @ApiResponse(responseCode = "404",description = "No se ha encontrado la categoria"
                    ,content = @Content(mediaType = "application/json"
                    ,schema = @Schema(type = "object",example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"}"))),
            @ApiResponse(responseCode = "403",description = "No tiene autorizacion para realizar esta operacion"
                    ,content = @Content(mediaType = "application/json"
                    ,schema = @Schema(type = "object",example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"}")))
    })
    ResponseEntity<CategoriaResumeDTO> actualizarCategoria(
            @Parameter(
                    description = "ID de la categoria a actualizar",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id_categoria,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos de la categoria a editar",
                    required = true,
                    content=@Content(schema=@Schema(implementation = CategoriaRequestDTO.class))
            )
            @Valid @RequestBody CategoriaRequestDTO categoria,
            BindingResult result);

    @Operation(summary = "Guarda una nueva categoria",description = "Guarda una nueva categoria con los datos " +
            "que el usuario le proporciona. Requiere permiso CATEGORIA_CREAR"
            ,tags = {"Categoria"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",description = "Se ha creado exitosamente la categoria"
                    ,content = @Content(mediaType = "application/json",schema = @Schema(implementation = CategoriaResumeDTO.class))),
            @ApiResponse(responseCode = "400",description = "Hubo un error en la validacion de datos"
                    ,content = @Content(mediaType = "application/json"
                    ,schema = @Schema(type = "object",example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"}"))),
            @ApiResponse(responseCode = "403",description = "No tiene autorizacion para realizar esta operacion"
                    ,content = @Content(mediaType = "application/json"
                    ,schema = @Schema(type = "object",example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"}")))
    })
    ResponseEntity<CategoriaResumeDTO> guardarCategoria(Authentication authentication,
                                                        @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                                                description = "Datos de la categoria a crear",
                                                                required = true,
                                                                content = @Content(schema = @Schema(implementation = CategoriaRequestDTO.class))
                                                        )
                                                        @Valid @RequestBody CategoriaRequestDTO categoria,
                                                        BindingResult result);
}
