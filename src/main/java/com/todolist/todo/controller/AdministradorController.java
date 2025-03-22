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
public class AdministradorController {
    private final PermisoService permisoService;
    private final RolService rolService;
    private final GenericoDTOConverter genericoDTOConverter;
   private final RecordatorioService recordatorioService;

    @GetMapping
    @PreAuthorize("hasAuthority('PERMISOS_TODOS_LEER')")
    public List<PermisoDTO> obtenerPermisos(){
        List<Permiso> permisos=permisoService.obtenerPermisos();
        return genericoDTOConverter.convertirListADTO(permisos,PermisoDTO.class);
    }
    @GetMapping("/rol")
    @PreAuthorize("hasAuthority('ROLES_TODOS_LEER')")
    public List<RolResponseDTO> obtenerRoles(){
        List<Rol> roles=rolService.obtenerRoles();
        return genericoDTOConverter.convertirListADTO(roles, RolResponseDTO.class);
    }
    @PostMapping
    @PreAuthorize("hasAuthority('ROL_CREAR')")
    public ResponseEntity<RolResponseDTO> crearRol(@Valid @RequestBody RolRequestDTO rolRequest, BindingResult result){
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
    @PutMapping("/rol/{rolNombre}")
    @PreAuthorize("hasAuthority('ROL_EDITAR')")
    public ResponseEntity<RolResponseDTO> editarRol(@PathVariable String rolNombre,@Valid @RequestBody RolRequestDTO rolRequest,BindingResult result){
        if(result.hasFieldErrors()){
            String err=result.getFieldErrors().stream().
                    map(error->"Campo '"+error.getField()+"' : "+error.getDefaultMessage()).
                    collect(Collectors.joining(" , "));
        }
        Rol rolDataRequest=genericoDTOConverter.convertirAEntidad(rolRequest,Rol.class);
        Rol rolResult=rolService.editarRol(rolNombre,rolDataRequest);
        RolResponseDTO rolResponse=genericoDTOConverter.convertirADTO(rolResult, RolResponseDTO.class);
        return new ResponseEntity<>(rolResponse,HttpStatus.OK);
    }
    @DeleteMapping("/rol/{rolNombre}")
    @PreAuthorize("hasAuthority('ROL_ELIMINAR')")
    public ResponseEntity<?> eliminarRol(@PathVariable String rolNombre){
        rolService.eliminarRol(rolNombre);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/{rol}")
    @PreAuthorize("hasAuthority('ASIGNAR_PERMISO_ROL')")
    public ResponseEntity<RolResponseDTO> asignarPermisoARol(@PathVariable String rol,@RequestParam String permiso){
        if(permiso ==null) throw new EntradaInvalidaException("Debe ingresar el nombre del permiso");
        Rol rolConPermiso=rolService.asignarPermiso(rol,permiso);
        RolResponseDTO rolResponse=genericoDTOConverter.convertirADTO(rolConPermiso, RolResponseDTO.class);
        return new ResponseEntity<>(rolResponse, HttpStatus.OK);
    }

    @PostMapping("/enviarrecordatorios")
    @PreAuthorize("hasAuthority('ENVIAR_RECORDATORIOS_DEBUG')")
    public ResponseEntity<?> enviarRecordatorios(){
        recordatorioService.enviarRecordatoriosTareas();
        return ResponseEntity.ok(Map.of("mensaje","Recordatorios enviados manualmente"));
    }

    @DeleteMapping("/{rol}")
    public ResponseEntity<RolResponseDTO> revocarPermisoARol(@PathVariable String rol,@RequestParam String permiso){
        if(permiso == null)throw new EntradaInvalidaException("Debe ingresar el nombre del permiso");
        Rol rolSinPermiso=rolService.revocarPermiso(rol,permiso);
        RolResponseDTO rolResponse=genericoDTOConverter.convertirADTO(rolSinPermiso, RolResponseDTO.class);
        return new ResponseEntity<>(rolResponse,HttpStatus.OK);
    }

}
