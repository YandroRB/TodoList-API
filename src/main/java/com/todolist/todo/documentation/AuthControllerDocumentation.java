package com.todolist.todo.documentation;

import com.todolist.todo.dto.request.LoginRequestDTO;
import com.todolist.todo.dto.request.RegistroUsuarioRequestDTO;
import com.todolist.todo.dto.request.TokenRefreshRequestDTO;
import com.todolist.todo.dto.response.JwtResponseDTO;
import com.todolist.todo.dto.response.UsuarioResumenDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name="Autentificacion",description = "Inicio de sesion y registro de usuarios")
public interface AuthControllerDocumentation {

    @Operation(summary = "Rgistro de usuario"
            ,description = "Registra un usuario nuevo con los datos ingresados"
            ,tags = {"Autentificacion"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",description = "Se ha registrado exitosamente el usuario"
                    ,content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResumenDTO.class))),
            @ApiResponse(responseCode = "400",description = "Error en la validacion de datos"
                    ,content = @Content(mediaType = "application/json"
                    ,schema = @Schema(type = "object",example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"}"))),
    })
    ResponseEntity<UsuarioResumenDTO> registrarUsuario(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Detalles registro usuario",
                    content = @Content(schema = @Schema(implementation= RegistroUsuarioRequestDTO.class))
            )
            @Valid @RequestBody RegistroUsuarioRequestDTO registroDTO, BindingResult result);

    @Operation(summary = "Autentificacion del usuario",description = "Permite al usuario autentificarse por medio de su usuario y contraseña"
            ,tags = {"Autentificacion"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se ha autentificado correctamente"
                    ,content = @Content(mediaType = "application/json", schema = @Schema(implementation = JwtResponseDTO.class))),
            @ApiResponse(responseCode = "400",description = "Error en la entrada de datos"
                    ,content = @Content(mediaType = "application/json"
                    ,schema = @Schema(type = "object",example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"}"))),
            @ApiResponse(responseCode = "403",description = "Se ha producido un error"
                    ,content = @Content(mediaType = "application/json"
                    ,schema = @Schema(type = "object",example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"}")))
    })
    ResponseEntity<JwtResponseDTO> login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del autentificacion",
                    required = true,
                    content = @Content(schema = @Schema(implementation= LoginRequestDTO.class))
            )
            @Valid @RequestBody LoginRequestDTO loginDTO, BindingResult result);

    @Operation(summary = "Actualizar token", description = "Actualiza el token de corta duracion a travez del token de larga duracion"
            ,tags = {"Autentificacion"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Se ha refrescado el token exitosamente"
                    ,content = @Content(mediaType = "application/json", schema = @Schema(implementation= JwtResponseDTO.class))),
            @ApiResponse(responseCode = "400",description = "Entrada invalida del token"
                    ,content = @Content(mediaType = "application/json"
                    ,schema = @Schema(type = "object",example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"}"))),
            @ApiResponse(responseCode = "403", description = "Ha ocurrido un error"
                    ,content = @Content(mediaType = "application/json"
                    ,schema = @Schema(type = "object",example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\"}")))
    })
    ResponseEntity<JwtResponseDTO> refresh(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Token de larga duracion",
                    required = true,
                    content = @Content(schema = @Schema(implementation= TokenRefreshRequestDTO.class))
            )
            @Valid @RequestBody TokenRefreshRequestDTO refreshDTO, BindingResult result);
}
