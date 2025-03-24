package com.todolist.todo.controller;

import com.todolist.todo.dto.request.RolRequestDTO;
import com.todolist.todo.dto.response.PermisoDTO;
import com.todolist.todo.dto.response.RolResponseDTO;
import com.todolist.todo.exception.EntradaInvalidaException;
import com.todolist.todo.model.Permiso;
import com.todolist.todo.model.Rol;
import com.todolist.todo.repository.RolRepository;
import com.todolist.todo.service.PermisoService;
import com.todolist.todo.service.RecordatorioService;
import com.todolist.todo.service.RolService;
import com.todolist.todo.utility.GenericoDTOConverter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Tag(name="Admin",description = "Gestiona roles y permisos")
public class AdministradorController {
    private final PermisoService permisoService;
    private final RolService rolService;
    private final GenericoDTOConverter genericoDTOConverter;
   private final RecordatorioService recordatorioService;

    @Operation(summary = "Obtiene todos los permisos",
    description = "Obtiene todos los permisos con sus descripcion de que autoriza cada uno de ellos." +
            "Requiere permiso PERMISOS_TODOS_LEER.",
    security = @SecurityRequirement(name = "bearerAuth"),
    tags = {"Admin"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Lista de permisos obtenidas exitosamente",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(type = "array",implementation = PermisoDTO.class))),
            @ApiResponse(responseCode = "403",description = "No tienes permisos para realizar esta operaticion",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(
                                    type = "object",
                                    example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\" "
                            )))
    })
    @GetMapping
    @PreAuthorize("hasAuthority('PERMISOS_TODOS_LEER')")
    public List<PermisoDTO> obtenerPermisos(){
        List<Permiso> permisos=permisoService.obtenerPermisos();
        return genericoDTOConverter.convertirListADTO(permisos,PermisoDTO.class);
    }

    @Operation(summary = "Obtiene todos los roles",
            description = "Obtiene todos los roles. Requiere permiso ROLES_TODOS_LEER",
            security = @SecurityRequirement(name = "bearerAuth"),
            tags ={"Admin"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Lista de roles obtenida exitosamente",
                         content = @Content(mediaType = "application/json",
                         schema = @Schema(type = "array",implementation = RolResponseDTO.class))),
            @ApiResponse(responseCode = "403",description = "No tienes permiso para realizar esta operacion",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(
                                    type = "object",
                                    example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\" "
                            )))
    })
    @GetMapping("/rol")
    @PreAuthorize("hasAuthority('ROLES_TODOS_LEER')")
    public List<RolResponseDTO> obtenerRoles(){
        List<Rol> roles=rolService.obtenerRoles();
        return genericoDTOConverter.convertirListADTO(roles, RolResponseDTO.class);
    }

    @Operation(summary = "Crea un rol",description = "Permite crear un rol para poder asignarle permisos y ser" +
            "utilizado en usuarios. Requiere permiso ROL_CREAR",
            security = @SecurityRequirement(name = "bearerAuth"),
            tags = {"Admin"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Rol creado exitosamente",
                         content = @Content(mediaType = "application/json",
                         schema = @Schema(implementation = RolResponseDTO.class))),
            @ApiResponse(responseCode = "400",description = "Error al validar los datos de entrada",content = @Content),
            @ApiResponse(responseCode = "403",description = "No tiene autorizacion para realizar esta operacion",content = @Content)
    })

    @PostMapping
    @PreAuthorize("hasAuthority('ROL_CREAR')")
    public ResponseEntity<RolResponseDTO> crearRol(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true
                    ,description = "Detalles del rol a crear"
                    ,content = @Content(schema = @Schema(implementation = RolRequestDTO.class)))
            @Valid @RequestBody RolRequestDTO rolRequest, BindingResult result){
        if(result.hasFieldErrors()){
            String err= result.getFieldErrors().stream().
                    map(error->"Campo '"+error.getField()+"' : "+error.getDefaultMessage()).
                    collect(Collectors.joining(" , "));
            throw new EntradaInvalidaException("Error al crear el rol: "+err);
        }
        Rol rolResult=rolService.crearObtenerRol(rolRequest.getNombreRol());
        RolResponseDTO rolResponse=genericoDTOConverter.convertirADTO(rolResult,RolResponseDTO.class);
        return new ResponseEntity<>(rolResponse,HttpStatus.CREATED);
    }

    @Operation(summary = "Edita un rol",description = "Permite editar un rol en especifico." +
            "Requiere permiso ROL_EDITAR"
            ,security = @SecurityRequirement(name = "bearerAuth")
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
                            example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\" "
                    )))
    })

    @PutMapping("/rol/{rolNombre}")
    @PreAuthorize("hasAuthority('ROL_EDITAR')")
    public ResponseEntity<RolResponseDTO> editarRol(
            @Parameter(description = "Nombre del rol a editar"
                    ,required = true
                    ,example = "ADMIN")
            @PathVariable String rolNombre,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true
                    ,description = "Datos nuevos del rol"
                    ,content = @Content(schema = @Schema(implementation = RolRequestDTO.class))
            )
            @Valid @RequestBody RolRequestDTO rolRequest,BindingResult result){
        if(result.hasFieldErrors()){
            String err=result.getFieldErrors().stream().
                    map(error->"Campo '"+error.getField()+"' : "+error.getDefaultMessage()).
                    collect(Collectors.joining(" , "));
            throw new EntradaInvalidaException("Error al editar el rol: "+err);
        }
        Rol rolDataRequest=genericoDTOConverter.convertirAEntidad(rolRequest,Rol.class);
        Rol rolResult=rolService.editarRol(rolNombre,rolDataRequest);
        RolResponseDTO rolResponse=genericoDTOConverter.convertirADTO(rolResult, RolResponseDTO.class);
        return new ResponseEntity<>(rolResponse,HttpStatus.OK);
    }

    @Operation(summary = "Elimina un rol",description = "Elimina un rol en especifico." +
            "Requiere el permiso ROL_ELIMINAR",security = @SecurityRequirement(name = "bearerAuth")
            ,tags = {"Admin"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",description = "Se ha eliminado el rol correctamente",content = @Content),
            @ApiResponse(responseCode = "403", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(
                                    type = "object",
                                    example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\" "
                            )))
    })
    @DeleteMapping("/rol/{rolNombre}")
    @PreAuthorize("hasAuthority('ROL_ELIMINAR')")
    public ResponseEntity<?> eliminarRol(@PathVariable String rolNombre){
        rolService.eliminarRol(rolNombre);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Asigna un permiso a un rol",description = "Asigna un permiso a un rol, los permisos en " +
            "/api/admin metodo GET. Requiere el permiso ASIGNAR_PERMISO_ROL"
            ,security = @SecurityRequirement(name = "bearerAuth")
            ,tags = {"Admin"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Se ha asignado el rol correctamente",
            content = @Content(mediaType = "application/json",schema = @Schema(implementation = RolResponseDTO.class))),
            @ApiResponse(responseCode = "400",description = "Entrada invalida de datos ",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(
                                    type = "object",
                                    example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\" "
                            ))),
            @ApiResponse(responseCode = "403",description = "No tiene autorizacion para realizar esta operacion",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(
                                    type = "object",
                                    example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\" "
                            )))
    })
    @PostMapping("/{rol}")
    @PreAuthorize("hasAuthority('ASIGNAR_PERMISO_ROL')")
    public ResponseEntity<RolResponseDTO> asignarPermisoARol(
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
            @RequestParam String permiso){
        if(permiso ==null) throw new EntradaInvalidaException("Debe ingresar el nombre del permiso");
        Rol rolConPermiso=rolService.asignarPermiso(rol,permiso);
        RolResponseDTO rolResponse=genericoDTOConverter.convertirADTO(rolConPermiso, RolResponseDTO.class);
        return new ResponseEntity<>(rolResponse, HttpStatus.OK);
    }

    @Operation(summary = "Enviar recordatorios al correo de las tareas a vencer"
            ,description = "Envia un correo electronico de las tareas a vencer, es un metodo experimental. Requiere permiso ENVIAR_RECORDATORIOS_DEBUG"
            ,security = @SecurityRequirement(name = "bearerAuth"),tags = {"Admin"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Se ha enviado correctamente los recordatorios"
                    ,                    content = @Content(mediaType = "application/json",
                    schema = @Schema(
                            type = "object",
                            example = "{\"mensaje\":\"Se ha enviado los recodatorios\""
                    )))
    })
    @PostMapping("/enviarrecordatorios")
    @PreAuthorize("hasAuthority('ENVIAR_RECORDATORIOS_DEBUG')")
    public ResponseEntity<?> enviarRecordatorios(){
        recordatorioService.enviarRecordatoriosTareas();
        return ResponseEntity.ok(Map.of("mensaje","Recordatorios enviados manualmente"));
    }

    @Operation(summary = "Quitar permisos a un rol",description = "Revoca el permiso a un rol en especifico" +
            ".Requiere el permiso ELIMINAR_PERMISO_ROL",security = @SecurityRequirement(name = "bearerAuth")
            ,tags = {"Admin"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Se ha revocado el permiso correctamente"
                    ,content = @Content(mediaType = "application/json",schema = @Schema(implementation = RolResponseDTO.class))),
            @ApiResponse(responseCode = "403",description = "Ha ocurrido un error"
                    ,content = @Content(mediaType = "application/json",
                    schema = @Schema(
                            type = "object",
                            example = "{\"mensaje\":\"Ha ocurrido un error\",\"detalle\":\"/api/tarea\" "
                    )))
    })
    @DeleteMapping("/{rol}")
    @PreAuthorize("hasAuthority('ELIMINAR_PERMISO_ROL')")
    public ResponseEntity<RolResponseDTO> revocarPermisoARol(
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
            @RequestParam String permiso){
        if(permiso == null)throw new EntradaInvalidaException("Debe ingresar el nombre del permiso");
        Rol rolSinPermiso=rolService.revocarPermiso(rol,permiso);
        RolResponseDTO rolResponse=genericoDTOConverter.convertirADTO(rolSinPermiso, RolResponseDTO.class);
        return new ResponseEntity<>(rolResponse,HttpStatus.OK);
    }

}
