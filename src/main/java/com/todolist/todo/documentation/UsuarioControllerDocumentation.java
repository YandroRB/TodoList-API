package com.todolist.todo.documentation;

import com.todolist.todo.dto.request.TareaRequestDTO;
import com.todolist.todo.dto.request.UsuarioRequestDTO;
import com.todolist.todo.dto.response.UsuarioResponseDTO;
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

@Tag(name="Usuario",description = "Gestion de usuario")
public interface UsuarioControllerDocumentation {

    @Operation(summary = "Obtener usuario",description = "Obtiene los datos del usuario que tiene iniciado sesion." +
            "Requiere el permiso USUARIO_LEER",tags = {"Usuario"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Se ha obtenido los datos del usuario correctamente"
                    ,content = @Content(mediaType = "application/json",schema = @Schema(implementation = UsuarioResponseDTO.class))),
            @ApiResponse(responseCode = "404",description = "No se ha encontrado el usuario"
                    ,content = @Content(mediaType = "application/json"
                    ,schema = @Schema(type = "object",example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"}"))),
            @ApiResponse(responseCode = "403",description = "No tiene autorizacion para realizar esta operacion"
                    ,content = @Content(mediaType = "application/json"
                    ,schema = @Schema(type = "object",example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"}")))
    })
    UsuarioResponseDTO obtenerUsuario(Authentication authentication);

    @Operation(summary = "Obtener todos los usuarios", description = "Obtiene los datos de todos los usuarios registrados." +
            "Requiere el permiso USUARIO_TODOS_LEER"
            ,tags = {"Usuario"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Se ha obtenido la lista de usuarios exitosamente"
                    ,content = @Content(mediaType = "application/json",schema = @Schema(type = "array",implementation = UsuarioResponseDTO.class))),
            @ApiResponse(responseCode = "403",description = "No tiene autorizacion para realizar esta operacion"
                    ,content = @Content(mediaType = "application/json"
                    ,schema = @Schema(type = "object",example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"}")))
    })
    List<UsuarioResponseDTO> obtenerTodosUsuarios();

    @Operation(summary = "Obtener usuario por id", description = "Obtiene el usuario por id." +
            "Requiere el permiso USUARIO_ID_LEER",tags = {"Usuario"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Se ha obtenido el usuario exitosamente"
                    ,content = @Content(mediaType = "application/json",schema = @Schema(implementation = UsuarioResponseDTO.class))),
            @ApiResponse(responseCode = "404",description = "No se encontró el usuario"
                    ,content = @Content(mediaType = "application/json"
                    ,schema = @Schema(type = "object",example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"}"))),
            @ApiResponse(responseCode = "403",description = "No tiene autorizacion para realizar esta operacion"
                    ,content = @Content(mediaType = "application/json"
                    ,schema = @Schema(type = "object",example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"}")))
    })
    ResponseEntity<UsuarioResponseDTO> obtenerUsuarioPorId(
            @Parameter(
                    description = "ID del usuario a encontrar",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id);

    @Operation(summary = "Guardar usuario",description = "Registra un nuevo usuario con sus respectivos datos." +
            "Requiere el permiso USUARIO_CREAR",tags = {"Usuario"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",description = "Se ha registrado el usuario exitosamente"
                    ,content = @Content(mediaType = "application/json",schema = @Schema(implementation = UsuarioResponseDTO.class))),
            @ApiResponse(responseCode = "400",description = "Hubo en la validacion de datos"
                    ,content = @Content(mediaType = "application/json"
                    ,schema = @Schema(type = "object",example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"}"))),
            @ApiResponse(responseCode = "403",description = "No tiene autorizacion para realizar esta operacion"
                    ,content = @Content(mediaType = "application/json"
                    ,schema = @Schema(type = "object",example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"}")))
    })
    ResponseEntity<UsuarioResponseDTO> guardarUsuario(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del usuario a registrar",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UsuarioRequestDTO.class))
            )
            @Valid @RequestBody UsuarioRequestDTO usuario, BindingResult result);

    @Operation(summary = "Actualizar los datos del usuario",description = "Actualiza los datos del usuario que se busca por ID, si se actualiza el username se requiere volver a iniciar sesion." +
            "Requiere el permiso USUARIO_ACTUALIZAR",tags = {"Usuario"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Se ha actualizado exitosamente el usuario"
                    ,content = @Content(mediaType = "application/json",schema = @Schema(implementation = UsuarioResponseDTO.class))),
            @ApiResponse(responseCode = "400",description = "Hubo un error en la validacion de datos"
                    ,content = @Content(mediaType = "application/json"
                    ,schema = @Schema(type = "object",example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"}"))),
            @ApiResponse(responseCode = "404",description = "No se encontró el usuario"
                    ,content = @Content(mediaType = "application/json"
                    ,schema = @Schema(type = "object",example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"}"))),
            @ApiResponse(responseCode = "403",description = "No tiene autorizacion para realizar esta operacion"
                    ,content = @Content(mediaType = "application/json"
                    ,schema = @Schema(type = "object",example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"}")))
    })
    ResponseEntity<UsuarioResponseDTO> actualizarUsuario(
            @Parameter(
                    description = "ID del usuario a actualizar los datos",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del usuario a actualizar,requiere actualizar todos los datos",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UsuarioRequestDTO.class))
            )
            @Valid @RequestBody UsuarioRequestDTO usuario,
                                                         BindingResult result);

    @Operation(summary ="Actualizar datos del usuario actual",description = "Actualiza los datos del usuario que esta iniciado sesion,si actualiza el username se requiere volver a iniciar sesion." +
            "Requiere el permiso USUARIO_PARCIAL_ACTUALIZAR"
            ,tags = {"Usuario"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Se ha actualizado los datos exitosamente"
                    ,content = @Content(mediaType = "application/json",schema = @Schema(implementation = UsuarioResponseDTO.class))),
            @ApiResponse(responseCode = "400",description = "Hubo un error en la validacion de datos"
                    ,content = @Content(mediaType = "application/json"
                    ,schema = @Schema(type = "object",example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"}"))),
            @ApiResponse(responseCode = "403",description = "No tiene autorizacion para realizar esta operacion"
                    ,content = @Content(mediaType = "application/json"
                    ,schema = @Schema(type = "object",example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"}")))
    })
    ResponseEntity<UsuarioResponseDTO> actualizarUsuarioParcial(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del usuario a actualizar, no es obligatorio llenar todos los campos",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UsuarioRequestDTO.class))

            )
            @RequestBody UsuarioRequestDTO usuarioRequestDTO, BindingResult result,
                                                              Authentication authentication);


    @Operation(summary = "Eliminar usuario",description = "Elimina un usuario con sus datos, incluye tambien sus tareas. " +
            "Requiere el permiso USUARIO_ELIMINAR",tags = {"Usuario"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",description = "Se ha eliminado el usuario con exito",content = @Content),
            @ApiResponse(responseCode = "404",description = "No se ha encontrado el usuario"
                    ,content = @Content(mediaType = "application/json"
                    ,schema = @Schema(type = "object",example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"}"))),
            @ApiResponse(responseCode = "403",description = "No tienes autorizacion para realizar esta operacion"
                    ,content = @Content(mediaType = "application/json"
                    ,schema = @Schema(type = "object",example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"}")))
    })
    ResponseEntity<Void> eliminarUsuario(
            @Parameter(
                    description = "ID del usuario a eliminar",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id);

    @Operation(summary = "Elimina una tarea de un usuario",description = "Elimina una tarea de un usuario a traves del id de la tarea y el id del usuario." +
            "Requiere el permiso USUARIO_TAREA_ELIMINAR",tags = {"Usuario"})

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Se ha eliminado exitosamente la tarea del usuario"
                    ,content = @Content(mediaType = "application/json"
                    ,schema = @Schema(type = "object",example = "{\"mensaje\":\"Se ha eliminado exitosamente la tarea\",\"Tarea\":{\"id_tarea\":1}}"))),
            @ApiResponse(responseCode = "404",description = "No se ha encontrado el usuario o la tarea"
                    ,content = @Content(mediaType = "application/json"
                    ,schema = @Schema(type = "object",example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"}"))),
            @ApiResponse(responseCode = "403",description = "No tiene autorizacion para realizar esta operacion"
                    ,content = @Content(mediaType = "application/json"
                    ,schema = @Schema(type = "object",example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"}")))
    })
    ResponseEntity<?> eliminarTareaPorUsuario(
            @Parameter(
                    description = "ID del usuario que quieres eliminarle la tarea",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id_usuario,
            @Parameter(
                    description = "ID de la tarea que quieres eliminar",
                    required = true,
                    example = "3"
            )
            @PathVariable Long id_tarea);


    @Operation(summary = "Actualizar usuario por id", description = "Busca un usuario por el id y actualiza parcialmente los datos del usuario." +
            "Requiere el permiso USUARIO_ID_PARCIAL_ACTUALIZAR", tags={"Usuario"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Se ha actualizado correctamente los datos del usuario"
                    ,content = @Content(mediaType = "application/json",schema = @Schema(implementation = UsuarioResponseDTO.class))),
            @ApiResponse(responseCode = "404",description = "No se encontró el id del usuario"
                    ,content = @Content(mediaType = "application/json"
                    ,schema = @Schema(type = "object",example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"}"))),
            @ApiResponse(responseCode = "400",description = "Hubo un error en la validacion de datos"
                    ,content = @Content(mediaType = "application/json"
                    ,schema = @Schema(type = "object",example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"}"))),
            @ApiResponse(responseCode = "403",description = "No tiene autorizacion para realizar esta operacion"
                    ,content = @Content(mediaType = "application/json"
                    ,schema = @Schema(type = "object",example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"}")))
    })
    ResponseEntity<UsuarioResponseDTO> actualizarUsuarioIDParcial(
            @Parameter(
                    description = "ID del usuario a modificar el usuario",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del usuario a actualizar, no requiere llenar todos los campos, si se actualiza el username se requiere volver a iniciar sesion",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UsuarioRequestDTO.class))
            )
            @RequestBody UsuarioRequestDTO usuarioRequestDTO, BindingResult result);


    @Operation(summary = "Agregar tareas a un usuario", description = "Agrega tareas a un usuario." +
            "Requiere el permiso USUARIO_TAREA_CREAR",tags = {"Usuario"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",description = "Se ha agregado la tarea al usuario exitosamente"
                    ,content = @Content(mediaType = "application/json",schema = @Schema(implementation = UsuarioResponseDTO.class))),
            @ApiResponse(responseCode = "400",description = "Hubo un error en la validacion de datos"
                    ,content = @Content(mediaType = "application/json"
                    ,schema = @Schema(type = "object",example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"}"))),
            @ApiResponse(responseCode = "404",description = "No se encontró el usuario a agregar la tarea"
                    ,content = @Content(mediaType = "application/json"
                    ,schema = @Schema(type = "object",example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"}")))
    })
    ResponseEntity<UsuarioResponseDTO> agregarTareaAUsuario(
            @Parameter(
                    description = "ID del usuario a agregar una tarea",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos de la tarea a agregar ",
                    required = true,
                    content = @Content(schema = @Schema(implementation = TareaRequestDTO.class))
            )
            @Valid @RequestBody TareaRequestDTO tarea, BindingResult result);

}
