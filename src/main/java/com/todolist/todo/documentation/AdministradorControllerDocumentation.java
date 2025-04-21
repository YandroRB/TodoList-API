package com.todolist.todo.documentation;

import com.todolist.todo.dto.request.RolRequestDTO;
import com.todolist.todo.dto.response.PermisoDTO;
import com.todolist.todo.dto.response.RolResponseDTO;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
@Tag(name="Admin",description = "Gestiona roles y permisos")
public interface AdministradorControllerDocumentation {
    @Operation(summary = "Obtiene todos los permisos",
            description = "Obtiene todos los permisos con sus descripcion de que autoriza cada uno de ellos." +
                    "Requiere permiso PERMISOS_TODOS_LEER.",
            tags = {"Admin"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Lista de permisos obtenidas exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "array",implementation = PermisoDTO.class))),
            @ApiResponse(responseCode = "403",description = "No tienes permisos para realizar esta operaticion",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(
                                    type = "object",
                                    example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"}"
                            )))
    })
    List<PermisoDTO> obtenerPermisos();


    @Operation(summary = "Obtiene todos los roles",
            description = "Obtiene todos los roles. Requiere permiso ROLES_TODOS_LEER",
            tags ={"Admin"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Lista de roles obtenida exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "array",implementation = RolResponseDTO.class))),
            @ApiResponse(responseCode = "403",description = "No tienes permiso para realizar esta operacion",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(
                                    type = "object",
                                    example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"} "
                            )))
    })
    List<RolResponseDTO> obtenerRoles();

    @Operation(summary = "Crea un rol",description = "Permite crear un rol para poder asignarle permisos y ser" +
            "utilizado en usuarios. Requiere permiso ROL_CREAR",
            tags = {"Admin"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Rol creado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RolResponseDTO.class))),
            @ApiResponse(responseCode = "400",description = "Error al validar los datos de entrada",content = @Content),
            @ApiResponse(responseCode = "403",description = "No tiene autorizacion para realizar esta operacion",content = @Content)
    })
    ResponseEntity<RolResponseDTO> crearRol(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true
                    ,description = "Detalles del rol a crear"
                    ,content = @Content(schema = @Schema(implementation = RolRequestDTO.class)))
            @Valid @RequestBody RolRequestDTO rolRequest, BindingResult result);


    @Operation(summary = "Edita un rol",description = "Permite editar un rol en especifico." +
            "Requiere permiso ROL_EDITAR"
            
            , tags = {"Admin"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Se ha editado rol exitosamente"
                    ,content = @Content(mediaType = "application/json"
                    ,schema = @Schema(implementation = RolResponseDTO.class))),
            @ApiResponse(responseCode = "400",description = "Hubo un error en la validacion de datos",content = @Content),
            @ApiResponse(responseCode = "403",description = "No tiene autorizacion para realizar esta operacion"
                    ,content = @Content(mediaType = "application/json",
                    schema = @Schema(
                            type = "object",
                            example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"} "
                    )))
    })
    ResponseEntity<RolResponseDTO> editarRol(
            @Parameter(description = "Nombre del rol a editar"
                    ,required = true
                    ,example = "ADMIN")
            @PathVariable String rolNombre,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true
                    ,description = "Datos nuevos del rol"
                    ,content = @Content(schema = @Schema(implementation = RolRequestDTO.class))
            )
            @Valid @RequestBody RolRequestDTO rolRequest,BindingResult result);


    @Operation(summary = "Elimina un rol",description = "Elimina un rol en especifico." +
            "Requiere el permiso ROL_ELIMINAR"
            ,tags = {"Admin"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",description = "Se ha eliminado el rol correctamente",content = @Content),
            @ApiResponse(responseCode = "403", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(
                                    type = "object",
                                    example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"} "
                            )))
    })
    ResponseEntity<?> eliminarRol(
            @Parameter(
                    description = "Nombre del rol a eliminar",
                    required = true,
                    example = "INVITADO"
            )
            @PathVariable String rolNombre);

    @Operation(summary = "Asigna un permiso a un rol",description = "Asigna un permiso a un rol, los permisos en " +
            "/api/admin metodo GET. Requiere el permiso ASIGNAR_PERMISO_ROL"
            
            ,tags = {"Admin"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Se ha asignado el rol correctamente",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = RolResponseDTO.class))),
            @ApiResponse(responseCode = "400",description = "Entrada invalida de datos ",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(
                                    type = "object",
                                    example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"}"
                            ))),
            @ApiResponse(responseCode = "403",description = "No tiene autorizacion para realizar esta operacion",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(
                                    type = "object",
                                    example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"} "
                            )))
    })
    ResponseEntity<RolResponseDTO> asignarPermisoARol(
            @Parameter(
                    description = "Nombre del rol al que se le va asignar el permiso",
                    required = true,
                    example = "USER"
            )
            @PathVariable String rol,
            @Parameter(
                    description = "Nombre del permiso a asignar",
                    required = true,
                    example = "PERMISOS_TODOS_LEER"
            )
            @RequestParam String permiso);

    @Operation(summary = "Enviar recordatorios al correo de las tareas a vencer"
            ,description = "Envia un correo electronico de las tareas a vencer, es un metodo experimental. Requiere permiso ENVIAR_RECORDATORIOS_DEBUG"
            ,tags = {"Admin"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Se ha enviado correctamente los recordatorios"
                    ,                    content = @Content(mediaType = "application/json",
                    schema = @Schema(
                            type = "object",
                            example = "{\"mensaje\":\"Se ha enviado los recodatorios\"}"
                    )))
    })
    ResponseEntity<?> enviarRecordatorios();

    @Operation(summary = "Quitar permisos a un rol",description = "Revoca el permiso a un rol en especifico" +
            ".Requiere el permiso ELIMINAR_PERMISO_ROL"
            ,tags = {"Admin"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Se ha revocado el permiso correctamente"
                    ,content = @Content(mediaType = "application/json",schema = @Schema(implementation = RolResponseDTO.class))),
            @ApiResponse(responseCode = "403",description = "Ha ocurrido un error"
                    ,content = @Content(mediaType = "application/json",
                    schema = @Schema(
                            type = "object",
                            example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"} "
                    )))
    })
    ResponseEntity<RolResponseDTO> revocarPermisoARol(
            @Parameter(
                    description = "Nombre del rol a revocar el permiso",
                    required = true,
                    example = "USER"
            )
            @PathVariable String rol,
            @Parameter(
                    description = "Nombre del permiso a revocar",
                    required = true,
                    example="PERMISOS_TODOS_LEER"
            )
            @RequestParam String permiso);
}
