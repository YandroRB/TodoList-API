package com.todolist.todo.documentation;

import com.todolist.todo.dto.request.TareaRecordatorioRequestDTO;
import com.todolist.todo.dto.request.TareaRequestDTO;
import com.todolist.todo.dto.response.TareaResponseDTO;
import com.todolist.todo.dto.response.TareaResumenDTO;
import com.todolist.todo.enumerator.EstadoTarea;
import com.todolist.todo.enumerator.PrioridadTarea;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
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
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name="Tarea",description = "Gestion de tarea")
public interface TareaControllerDocumentation {

    @Operation(summary = "Obtener tareas", description = "Obtiene las tareas que le pertenece al usuario, con la opcion que " +
            "puedes traerlas de forma ordenada (ascendente o descendente por prioridad). Requiere permiso USUARIO_TAREA_LEER"
            ,tags = {"Tarea"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Lista de tareas obtenidas exitosamente"
                    ,content = @Content(mediaType = "application/json",schema = @Schema(type = "array",implementation = TareaResponseDTO.class))),
            @ApiResponse(responseCode = "404",description = "Usuario no encontrado"
                    ,content = @Content(mediaType = "application/json"
                    ,schema = @Schema(type = "object",example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"}"))),
            @ApiResponse(responseCode = "403",description = "No tiene autorizacion para realizar esta operacion"
                    ,content = @Content(mediaType = "application/json"
                    ,schema = @Schema(type = "object",example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"}")))
    })
    List<TareaResponseDTO> obtenerTareas(
            @Parameter(
                    description = "Parametro para ordenar de forma ascendente o descendente",
                    examples = {
                            @ExampleObject(
                                    name = "Ascendente",
                                    description = "Ordenar de forma ascendente",
                                    value = "asc"
                            ),
                            @ExampleObject(
                                    name = "Descendente",
                                    description = "Ordenar de forma descendente",
                                    value = "desc"
                            )
                    }
            )
            @RequestParam(required = false) String sortby
            , Authentication authentication);

    @Operation(summary = "Obtiene todas las tareas", description = "Obtiene todas las tareas sin importar si le pertenece" +
            "las tareas. Requiere permiso TAREA_TODAS_LEER",tags = {"Tarea"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Lista de tareas obtenidas exitosamente"
                    ,content = @Content(mediaType = "application/json",schema = @Schema(type = "array",implementation = TareaResponseDTO.class))),
            @ApiResponse(responseCode = "403",description = "No tiene autorizacion para realizar esta operacion"
                    ,content = @Content(mediaType = "application/json"
                    ,schema = @Schema(type = "object",example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"}")))
    })
    List<TareaResponseDTO> obtenerTodaTareas(
            @Parameter(
                    description = "Parametro para ordenar de forma ascendente o descendente",
                    examples = {
                            @ExampleObject(
                                    name = "Ascendente",
                                    description = "Ordenar de forma ascendente",
                                    value = "asc"
                            ),
                            @ExampleObject(
                                    name = "Descendente",
                                    description = "Ordenar de forma descendente",
                                    value = "desc"
                            )
                    }
            )
            @RequestParam(required = false) String sortby);

    @Operation(summary = "Obtiene la tarea por id",description = "Obtiene la tarea por el id de la tarea." +
            "Requiere el permiso TAREA_ID_LEER -> Para obtener la tarea sin ser el dueño de la tarea." +
            "TAREA_ID_USUARIO_LEER -> Para obtener la tarea verificando que seas dueño de la tarea"
            ,tags = {"Tarea"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Tarea obtenida exitosamente"
                    ,content = @Content(mediaType = "application/json",schema = @Schema(implementation = TareaResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "No se encontró la tarea"
                    ,content = @Content(mediaType = "application/json"
                    ,schema = @Schema(type = "object",example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"}"))),
            @ApiResponse(responseCode = "403",description = "No tiene autorizacion para realizar esta operacion"
                    ,content = @Content(mediaType = "application/json"
                    ,schema = @Schema(type = "object",example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"}")))
    })
    ResponseEntity<TareaResumenDTO> obtenerTareaPorId(
            @Parameter(
                    description = "ID de la tarea",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id);
    @Operation(summary = "Obtiene las tareas por categoria", description = "Obtiene las tareas que tenga cierta categoria." +
            "Requiere permiso TAREA_CATEGORIA_LEER -> Obtiene las tareas por categoria sin necesidad ser dueño de las tareas." +
            "Requiere permiso TAREA_USUARIO_CATEGORIA_LEER -> Obtiene las tareas por categoria asegurandose que le pertenece la categoria y la tarea"
            ,tags = {"Tarea"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Lista de tareas obtenida exitosamente"
                    ,content = @Content(mediaType = "application/json",schema = @Schema(type = "array",implementation = TareaResponseDTO.class))),
            @ApiResponse(responseCode = "400",description = "Entrada de datos no valida"
                    ,content = @Content(mediaType = "application/json"
                    ,schema = @Schema(type = "object",example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"}"))),
            @ApiResponse(responseCode = "403",description = "No tiene autorizacion para realizar esta operacion"
                    ,content = @Content(mediaType = "application/json"
                    ,schema = @Schema(type = "object",example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"}")))
    })
    List<TareaResponseDTO> obtenerTareasPorCategoria(
            @Parameter(
                    description = "Categoria de la tarea",
                    required = true,
                    example = "diarias"
            )
            @RequestParam String categoria);

    @Operation(summary = "Obtener tareas compartidas", description = "Permite obtener las tareas que te han compartido otros usuarios. " +
            "Requiere el permiso TAREAS_COMPARTIDAS_LEER"
            ,tags = {"Tarea"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Lista de tareas compartidas obtenidas exitosamente"
                    ,content = @Content(mediaType = "application/json",schema = @Schema(type = "array",implementation = TareaResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "No se encontro el usuario"
                    ,content = @Content(mediaType = "application/json"
                    ,schema = @Schema(type = "object",example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"}"))),
            @ApiResponse(responseCode = "403",description = "No tiene autorizacion para realizar esta operacion"
                    ,content = @Content(mediaType = "application/json"
                    ,schema = @Schema(type = "object",example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"}")))
    })
    List<TareaResponseDTO> obtenerTareasCompartidas(Authentication authentication);

    @Operation(summary = "Elimina la categoria de una tarea", description = "Elimina una categoria de una tarea a traves" +
            "del id de la tarea y el id de la categoria.Requiere permiso TAREA_CATEGORIA_ELIMINAR -> elimina la categoria sin importar el propietario." +
            "TAREA_USUARIO_CATEGORIA_ELIMINAR -> Verifica que la tarea le pertenezca con la categoria  y elimina la categoria"
            ,tags = {"Tarea"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Categoria eliminada de la tarea exitosamente"
                    ,content = @Content(mediaType = "application/json",schema = @Schema(implementation = TareaResponseDTO.class))),
            @ApiResponse(responseCode = "404",description = "No se encontró la categoria o la tarea"
                    ,content = @Content(mediaType = "application/json"
                    ,schema = @Schema(type = "object",example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"}"))),
            @ApiResponse(responseCode = "403",description = "No tiene autorizacion para realizar esta operacion"
                    ,content = @Content(mediaType = "application/json"
                    ,schema = @Schema(type = "object",example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"}")))
    })
    ResponseEntity<TareaResponseDTO> eliminarCategoriaTarea(
            @Parameter(
                    description = "ID de la tarea a eliminar la categoria",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id_tarea,
            @Parameter(
                    description = "ID de la categoria a quitar de la tarea",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id_categoria);

    @Operation(summary = "Elimina una subtarea",description = "Elimina una sub tarea de la tarea padre a traves del id de la tarea y el id" +
            "de la sub tarea. Requiere TAREA_SUBTAREA_ELIMINAR -> Elimina la sub tarea sin importar el propietario." +
            "TAREA_USUARIO_SUBTAREA_ELIMINAR -> Elimina la sub tarea verificando que la tarea le pertenezca al usuario"
            ,tags = {"Tarea"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",description = "Se ha eliminado exitosamente la tarea"
                    ,content = @Content),
            @ApiResponse(responseCode = "404",description = "No se encontró la tarea a eliminar"
                    ,content = @Content(mediaType = "application/json"
                    ,schema = @Schema(type = "object",example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"}"))),
            @ApiResponse(responseCode = "403",description = "No tiene autorizacion para realizar esta operacion"
                    ,content = @Content(mediaType = "application/json"
                    ,schema = @Schema(type = "object",example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"}")))
    })
    ResponseEntity<Void> eliminarSubTarea(
            @Parameter(
                    description = "ID de la tarea donde se encuentra la subtarea",
                    example = "1",
                    required = true
            )
            @PathVariable Long id_tarea,
            @Parameter(
                    description = "ID de la subtarea a eliminar",
                    example = "3",
                    required = true
            )
            @PathVariable Long id_sub);

    @Operation(summary = "Guardar tarea", description = "Guarda una tarea con los datos ingresados." +
            "Requiere el permiso TAREA_GUARDAR",tags = {"Tarea"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",description = "Se ha guardado la tarea exitosamente"
                    ,content = @Content(mediaType = "application/json",schema = @Schema(implementation = TareaResumenDTO.class))),
            @ApiResponse(responseCode = "400",description = "Hubo un error en la validacion de datos"
                    ,content = @Content(mediaType = "application/json"
                    ,schema = @Schema(type = "object",example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"}"))),
            @ApiResponse(responseCode = "403",description = "No tiene autorizacion para realizar esta operacion"
                    ,content = @Content(mediaType = "application/json"
                    ,schema = @Schema(type = "object",example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"}")))
    })
    ResponseEntity<TareaResumenDTO> guardarTarea(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos de la tarea a guardar",
                    required = true,
                    content = @Content(schema = @Schema(implementation = TareaRequestDTO.class))
            )
            @Valid @RequestBody TareaRequestDTO tarea,
                                                 BindingResult result, Authentication authentication);

    @Operation(summary = "Guardar subtarea",description = "Guarda una subtarea dentro de una tarea a traves del ID de la tarea." +
            "Requiere el permiso TAREA_SUBTAREA_GUARDAR -> Sin importar quien sea el dueño de la tarea." +
            " TAREA_SUBTAREA_USUARIO_GUARDAR -> Revisa que la tarea tenga compartida o le pertenezca y agrega la subtarea",
    tags = {"Tarea"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",description = "Se ha agregado la tarea exitosamente"
                    ,content = @Content(mediaType = "application/json",schema = @Schema(implementation = TareaResponseDTO.class))),
            @ApiResponse(responseCode = "400",description = "Hubo un error en la validacion de datos"
                    ,content = @Content(mediaType = "application/json"
                    ,schema = @Schema(type = "object",example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"}"))),
            @ApiResponse(responseCode = "403",description = "No tiene autorizacion para realizar esta operacion"
                    ,content = @Content(mediaType = "application/json"
                    ,schema = @Schema(type = "object",example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"}"))),
            @ApiResponse(responseCode = "404",description = "No se ha encontrado la tarea"
                    ,content = @Content(mediaType = "application/json"
                    ,schema = @Schema(type = "object",example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"}")))
    })
    ResponseEntity<TareaResponseDTO> guardarSubTarea(
            @Parameter(
                    description = "ID de la tarea a agregar la subtarea",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id_tarea,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos de la subtarea a agregar",
                    required = true,
                    content = @Content(schema = @Schema(implementation = TareaRequestDTO.class))
            )
            @Valid @RequestBody TareaRequestDTO tareaRequestDTO,
                                                     BindingResult result);

    @Operation(summary = "Asigna una categoria a una tarea",description = "Asigna una categoria existente a una tarea." +
            "Requeire el permiso TAREA_ASIGNAR_CATEGORIA -> Para asignarle una categoria sin verificar si le pertenece la tarea." +
            "TAREA_USUARIO_ASIGNAR_CATEGORIA -> Verifica que la tarea le pertenezca al usuario y le asigna una categoria que le pertenezca",
    tags = {"Tarea"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Se ha asignado la categoria correctamente"
                    ,content = @Content(mediaType = "application/json",schema = @Schema(implementation = TareaResponseDTO.class))),
            @ApiResponse(responseCode = "404",description = "No se encontro la tarea o la categoria"
                    ,content = @Content(mediaType = "application/json"
                    ,schema = @Schema(type = "object",example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"}"))),
            @ApiResponse(responseCode = "403",description = "No tiene autorizacion para realizar esta operacion"
                    ,content = @Content(mediaType = "application/json"
                    ,schema = @Schema(type = "object",example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"}")))
    })
    ResponseEntity<TareaResponseDTO> asignarCategoriaTarea(
            @Parameter(
                    description = "ID de la tarea a agregarle una categoria",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id_tarea,
            @Parameter(
                    description = "ID de la categoria existente",
                    required = true,
                    example = "3"
            )
            @PathVariable Long id_categoria);

    @Operation(summary = "Compartir tarea",description = "Comparte la tarea con un usuario a traves del id de la tarea y id del usuario." +
            "Requiere el permiso TAREA_COMPARTIR_ESCRIBIR -> Comparte la tarea sin verificar que sea propietario de la misma." +
            "TAREA_COMPARTIR_USUARIO_ESCRIBIR -> Comparte la tarea verificando que sea propietario de la misma",
    tags = {"Tarea"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Se ha compartido exitosamente la tarea"
                    ,content = @Content(mediaType = "application/json",schema = @Schema(implementation = TareaResponseDTO.class))),
            @ApiResponse(responseCode = "404",description = "No se ha encontrado la tarea o el usuario"
                    ,content = @Content(mediaType = "application/json"
                    ,schema = @Schema(type = "object",example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"}"))),
            @ApiResponse(responseCode = "403",description = "No tiene autorizacion para realizar esta operacion"
                    ,content = @Content(mediaType = "application/json"
                    ,schema = @Schema(type = "object",example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"}")))
    })
    ResponseEntity<TareaResponseDTO> compartirTarea(
            @Parameter(
                    description = "ID de la tarea",
                    required = true,
                    example = "1"
            )
            @PathVariable Long tareaId,
            @Parameter(
                    description = "ID del usuario",
                    required = true,
                    example = "3"
            )
            @PathVariable Long usuarioId);

    @Operation(summary = "Dejar de compartir tarea",description = "Quita permisos al usuario de la tarea, deja de compartir la tarea al usuario." +
            "Requiere el permiso TAREA_DEJAR_COMPARTIR_ELIMINAR -> Deja de compartir la tarea sin verificar que la tarea le pertenezca." +
            "TAREA_DEJAR_COMPARTIR_USUARIO_ELIMINAR -> Deja de compartir la tarea verificando que la tarea le pertenezca",
    tags = {"Tarea"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Se ha dejado de compartir la tarea exitosamente"
                    ,content = @Content(mediaType = "application/json",schema = @Schema(implementation = TareaResponseDTO.class))),
            @ApiResponse(responseCode = "404",description = "No se ha encontrado la tarea o el usuario"
                    ,content = @Content(mediaType = "application/json"
                    ,schema = @Schema(type = "object",example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"}"))),
            @ApiResponse(responseCode = "403",description = "No tiene autorizacion para realizar esta operacion"
                    ,content = @Content(mediaType = "application/json"
                    ,schema = @Schema(type = "object",example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"}")))
    })
    ResponseEntity<TareaResponseDTO> dejarCompartir(
            @Parameter(
                    description = "ID de la tarea a dejar de compartir",
                    required = true,
                    example = "1"
            )
            @PathVariable Long tareaId,
            @Parameter(
                    description = "ID del usuario a dejar de compartir",
                    required = true,
                    example = "3"
            )
            @PathVariable Long usuarioId);

    @Operation(summary = "Actualizar tarea completa",description = "Actualiza la tarea como requisito todos los datos de la tarea son requeridos." +
            "Requiere el permiso TAREA_ACTUALIZAR -> Permite actualizar la tarea sin verificar que le pertenezca." +
            "TAREA_USUARIO_ACTUALIZAR -> Permite actualizar la tarea verificando que la tarea le pertenezca"
            ,tags = {"Tarea"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Se ha actualizado la tarea exitosamente"
                    ,content = @Content(mediaType = "application/json",schema = @Schema(implementation = TareaResumenDTO.class))),
            @ApiResponse(responseCode = "400",description = "Hubo un error en la validacion de los datos"
                    ,content = @Content(mediaType = "application/json"
                    ,schema = @Schema(type = "object",example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"}"))),
            @ApiResponse(responseCode = "404",description = "No se encontro la tarea a actualizar los datos"
                    ,content = @Content(mediaType = "application/json"
                    ,schema = @Schema(type = "object",example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"}"))),
            @ApiResponse(responseCode = "403",description = "No tiene autorizacion para realizar esta operacion"
                    ,content = @Content(mediaType = "application/json"
                    ,schema = @Schema(type = "object",example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"}")))
    })
    ResponseEntity<TareaResumenDTO> actualizarTarea(
            @Parameter(
                    description = "ID de la tarea a actualizar los datos",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos de la tarea a actualizar",
                    required = true,
                    content = @Content(schema = @Schema(implementation = TareaRequestDTO.class))
            )
            @Valid @RequestBody TareaRequestDTO tarea,
            BindingResult result);

    @Operation(summary = "Establecer recordatorio",description = "Establece un recordatorio a la tarea y establece con cuantos dias de anticipacion quiere" +
            "que haga un recordatorio y envie un correo electronico. " +
            "Requiere permiso TAREA_ESTABLECER_RECORDATORIO_EDITAR -> Establece el recordatorio a una tarea sin que sea propietario de la tarea." +
            "TAREA_ESTABLECER_USUARIO_RECORDATORIO_EDITAR -> Establece el recordatorio a una tarea verificando que el usuario sea propietario de la tarea"
            ,tags = {"Tarea"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Se ha establecido correctamente el recordatorio"
                    ,content = @Content(mediaType = "application/json",schema = @Schema(implementation = TareaResponseDTO.class))),
            @ApiResponse(responseCode = "403",description = "No tiene autorizacion para realizar esta operacion"
                    ,content = @Content(mediaType = "application/json"
                    ,schema = @Schema(type = "object",example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"}"))),
            @ApiResponse(responseCode = "400",description = "No tiene autorizacion para realizar esta operacion"
                    ,content = @Content(mediaType = "application/json"
                    ,schema = @Schema(type = "object",example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"}")))
    })
    ResponseEntity<TareaResponseDTO> establecerRecordatorio(
            @Parameter(
                    description = "ID de la tarea a establecer recordatorio",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del recordatorio a establecer",
                    required = true,
                    content = @Content(schema = @Schema(implementation = TareaRecordatorioRequestDTO.class))
            )
            @RequestBody @Valid TareaRecordatorioRequestDTO tareaRecordatorio,
            BindingResult result);

    @Operation(summary = "Actualizar subtarea",description = "Actualiza los datos de la subtarea." +
            "Requiere el permiso TAREA_SUBTAREA_ACTUALIZAR -> Actualiza la subtarea sin importar el propietario de la tarea." +
            "TAREA_USUARIO_SUBTAREA_ACTUALIZAR -> Actualiza la subtarea verificando que la tarea padre que contiene la subtarea le pertenezca al usuario"
            ,tags = {"Tarea"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Se ha actualizado la subtarea exitosamente"
                    ,content = @Content(mediaType = "application/json",schema = @Schema(implementation = TareaResponseDTO.class))),
            @ApiResponse(responseCode = "400",description = "Hubo un problema en la validacion de datos"
                    ,content = @Content(mediaType = "application/json"
                    ,schema = @Schema(type = "object",example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"}"))),
            @ApiResponse(responseCode = "403",description = "No tiene autorizacion para realizar esta operacion"
                    ,content = @Content(mediaType = "application/json"
                    ,schema = @Schema(type = "object",example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"}"))),
    })
    ResponseEntity<TareaResponseDTO> actualizarSubTarea(
            @Parameter(
                    description = "ID de la tarea que contiene la subtarea",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id_tarea,
            @Parameter(
                    description = "ID de la subtarea a editar los datos",
                    required = true,
                    example = "3"
            )
            @PathVariable Long id_sub,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos actualizados de la subtarea",
                    required = true,
                    content = @Content(schema = @Schema(implementation = TareaRequestDTO.class))
            )
            @Valid @RequestBody TareaRequestDTO tarea,BindingResult result);

    @Operation(summary = "Obtener tareas por recordatorio", description = "Obtiene las tareas que tengan el recordatorio activado." +
            "Requiere permiso TAREA_OBTENER_RECORDATORIO_LEER -> Obtiene las tareas con el recordatorio activado sin importar el dueño de la tarea." +
            "TAREA_OBTENER_RECORDATORIO_USUARIO_LEER -> Obtiene las tareas con el recordatorio activado del usuario"
            ,tags = {"Tarea"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Lista de tareas con recordatorio activo obtenida exitosamente"
                    ,content = @Content(mediaType = "application/json",schema = @Schema(type = "array",implementation = TareaResponseDTO.class))),
            @ApiResponse(responseCode = "403",description = "No tiene autorizacion para realizar esta operacion"
                    ,content = @Content(mediaType = "application/json"
                    ,schema = @Schema(type = "object",example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"}")))
    })
    ResponseEntity<List<TareaResponseDTO>> obtenerTareasRecordatorio(
            @Parameter(
                    description = "Dias de anticipacion que tiene la tarea con recordatorio activo.Valor por defecto 1",
                    example = "2"
            )
            @RequestParam (defaultValue = "1") Integer dias,
            Authentication authentication);

    @Operation(summary = "Actualizar parcialmente la tarea",description = "Actualiza la tarea parcialmente, es decir no requiere que se actualicen todos los campos." +
            "TAREA_PARCIAL_ACTUALIZAR -> Actualiza la tarea sin importar el propietario de la misma." +
            "TAREA_USUARIO_PARCIAL_ACTUALIZAR -> Actualiza la tarea verificando si es propietario de la tarea"
            ,tags = {"Tarea"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Se ha actualizado correctamente la tarea"
                    ,content = @Content(mediaType = "application/json",schema = @Schema(implementation = TareaResumenDTO.class))),
            @ApiResponse(responseCode = "404",description = "Hubo un error en la validacion de datos"
                    ,content = @Content(mediaType = "application/json"
                    ,schema = @Schema(type = "object",example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"}"))),
            @ApiResponse(responseCode = "400",description = "No se encontró la tarea a actualizar"
                    ,content = @Content(mediaType = "application/json"
                    ,schema = @Schema(type = "object",example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"}"))),
            @ApiResponse(responseCode = "403",description = "No tienes autorizacion para realizar esta operacion"
                    ,content = @Content(mediaType = "application/json"
                    ,schema = @Schema(type = "object",example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"}")))
    })
    ResponseEntity<TareaResumenDTO> actualizarParcialTarea(
            @Parameter(
                    description = "ID de la tarea a actualizar",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos actualizados de la tarea",
                    required = true,
                    content = @Content(schema = @Schema(implementation = TareaRequestDTO.class))
            )
            @RequestBody TareaRequestDTO tarea,
            BindingResult result);


    @Operation(summary = "Actualizar estado de la tarea",description = "Actualiza es estado de la tarea." +
            "Requiere el permiso TAREA_ESTADO_ACTUALIZAR -> Actualiza el estado sin importar el propietario de la tarea." +
            "TAREA_USUARIO_ESTADO_ACTUALIZAR -> Actualiza el estado de la tarea verificando si es propietario de la tarea"
            ,tags = {"Tarea"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Se ha actualizado exitosamente el estado de la tarea"
                    ,content = @Content(mediaType = "application/json",schema = @Schema(implementation = TareaResumenDTO.class))),
            @ApiResponse(responseCode = "400",description = "Hubo un error en la validacion de datos"
                    ,content = @Content(mediaType = "application/json"
                    ,schema = @Schema(type = "object",example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"}"))),
            @ApiResponse(responseCode = "404",description = "No se encontro la tarea"
                    ,content = @Content(mediaType = "application/json"
                    ,schema = @Schema(type = "object",example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"}"))),
            @ApiResponse(responseCode = "403",description = "No tiene autorizacion para realizar esta operacion"
                    ,content = @Content(mediaType = "application/json"
                    ,schema = @Schema(type = "object",example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"}")))
    })
    ResponseEntity<TareaResumenDTO> actualizarEstadoTarea(
            @Parameter(
                    description = "ID de la tarea a actualizar el estado",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id,
            @Parameter(
                    description = "Estado de la tarea a ingresar",
                    required = true,
                    examples = {
                            @ExampleObject(
                                    name = "Completa",
                                    description = "Indica que la tarea esta completa",
                                    value = "COMPLETA"
                            ),
                            @ExampleObject(
                                    name = "Pendiente",
                                    description = "Indica que la tarea esta pendiente",
                                    value = "PENDIENTE"
                            ),
                            @ExampleObject(
                                    name = "En progreso",
                                    description = "Indica que la tarea esta en progreso",
                                    value="EN_PROGRESO"
                            ),
                            @ExampleObject(
                                    name = "Cancelada",
                                    description = "Indica que la tarea esta cancelada",
                                    value = "CANCELADA"
                            )
                    }
            )
            @PathVariable EstadoTarea estado);

    @Operation(summary = "Actualizar prioridad de la tarea", description = "Actualiza la prioridad de la tarea, esto indica la importancia que tiene la tarea sobre otras." +
            "Requiere el permiso TAREA_PRIORIDAD_ACTUALIZAR -> Actualiza la prioridad de la tarea sin comprobar si es propietario de la tarea." +
            "TAREA_USUARIO_PRIORIDAD_ACTUALIZAR -> Actualiza la prioridad de la tarea comprobando que le pertenezca al usuario"
            ,tags = {"Tarea"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Se ha actualizado la prioridad exitosamente"
                    ,content = @Content(mediaType = "application/json",schema = @Schema(implementation = TareaResumenDTO.class))),
            @ApiResponse(responseCode = "400",description = "Hubo un error en la validacion de datos"
                    ,content = @Content(mediaType = "application/json"
                    ,schema = @Schema(type = "object",example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"}"))),
            @ApiResponse(responseCode = "403",description = "No tiene autorizacion para realizar esta operacion"
                    ,content = @Content(mediaType = "application/json"
                    ,schema = @Schema(type = "object",example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"}")))
    })

    ResponseEntity<TareaResumenDTO> actualizarPrioridadTarea(
            @Parameter(
                    description = "ID de la tarea a actualizar la prioridad",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id,
            @Parameter(
                    description = "Prioridad de la tarea",
                    required = true,
                    examples = {
                            @ExampleObject(
                                    name="Baja",
                                    description = "Indica la prioridad de la tarea como baja",
                                    value = "BAJA"
                            ),
                            @ExampleObject(
                                    name = "Media",
                                    description = "Indica la prioridad de la tarea como media",
                                    value = "MEDIA"
                            ),
                            @ExampleObject(
                                    name = "Alta",
                                    description = "Indica la prioridad de la tarea como alta",
                                    value = "ALTA"
                            ),
                            @ExampleObject(
                                    name = "Urgente",
                                    description = "Indica la prioridad de la tarea como urgente",
                                    value = "URGENTE"
                            )
                    }
            )
            @PathVariable PrioridadTarea prioridad);

    @Operation(summary = "Eliminar tarea",description = "Elimina la tarea." +
            "Requiere el permiso TAREA_ELIMINAR -> Elimina la tarea sin importar el propietario de la tarea." +
            "TAREA_USUARIO_ELIMINAR -> Elimina la tarea verificando si la tarea le pertenece al usuario"
            ,tags = {"Tarea"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",description = "Tarea eliminada exitosamente"
                    ,content = @Content),
            @ApiResponse(responseCode = "404",description = "La tarea no se encontró"
                    ,content = @Content(mediaType = "application/json"
                    ,schema = @Schema(type = "object",example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"}"))),
            @ApiResponse(responseCode = "403",description = "No tiene autorizacion para realizar esta operacion"
                    ,content = @Content(mediaType = "application/json"
                    ,schema = @Schema(type = "object",example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"}")))

    })
    ResponseEntity<Void> eliminarTarea(@PathVariable Long id);

}
