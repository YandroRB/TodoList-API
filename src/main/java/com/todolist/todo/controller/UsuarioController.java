package com.todolist.todo.controller;

import com.todolist.todo.dto.request.TareaRequestDTO;
import com.todolist.todo.dto.request.UsuarioRequestDTO;
import com.todolist.todo.dto.response.UsuarioResponseDTO;
import com.todolist.todo.exception.EntradaInvalidaException;
import com.todolist.todo.model.Tarea;
import com.todolist.todo.model.Usuario;
import com.todolist.todo.service.UsuarioService;
import com.todolist.todo.utility.GenericoDTOConverter;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final GenericoDTOConverter genericoDTOConverter;
    public UsuarioController(UsuarioService usuarioService, GenericoDTOConverter genericoDTOConverter) {
        this.usuarioService = usuarioService;
        this.genericoDTOConverter = genericoDTOConverter;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('USUARIO_LEER')")
    public UsuarioResponseDTO obtenerUsuario(Authentication authentication) {
        Usuario usuario = usuarioService.obtenerUsuario(authentication.getName());
        return genericoDTOConverter.convertirADTO(usuario,UsuarioResponseDTO.class);
    }
    @GetMapping("/todos")
    @PreAuthorize("hasAuthority('USUARIO_TODOS_LEER')")
    public List<UsuarioResponseDTO> obtenerTodosUsuarios(){
        List<Usuario> usuarios=usuarioService.obtenerTodosUsuarios();
        return genericoDTOConverter.convertirListADTO(usuarios,UsuarioResponseDTO.class);
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USUARIO_ID_LEER')")
    public ResponseEntity<UsuarioResponseDTO> obtenerUsuarioPorId(@PathVariable Long id) {
        Usuario usuario=usuarioService.obtenerUsuario(id);
        UsuarioResponseDTO usuarioResponse= genericoDTOConverter.convertirADTO(usuario, UsuarioResponseDTO.class);
        return ResponseEntity.ok(usuarioResponse);
    }
    @PostMapping
    @PreAuthorize("hasAuthority('USUARIO_CREAR')")
    public ResponseEntity<UsuarioResponseDTO> guardarUsuario(@Valid @RequestBody UsuarioRequestDTO usuario, BindingResult result) {
        if(result.hasFieldErrors()){
            String err=result.getFieldErrors().stream()
                    .map(error->"Campo '"+error.getField()+"' : "+error.getDefaultMessage())
                    .collect(Collectors.joining("; "));
            throw new EntradaInvalidaException("Error al guardar el usuario: "+err);
        }
        Usuario usuarioRequest=genericoDTOConverter.convertirAEntidad(usuario,Usuario.class);
        Usuario usuarioGuardar= usuarioService.guardarUsuario(usuarioRequest);
        UsuarioResponseDTO usuarioResponse = genericoDTOConverter.convertirADTO(usuarioGuardar, UsuarioResponseDTO.class);
        return new ResponseEntity<>(usuarioResponse, HttpStatus.CREATED);
    }

    @PostMapping("/{id}/tareas")
    @PreAuthorize("hasAuthority('USUARIO_TAREA_CREAR')")
    public ResponseEntity<UsuarioResponseDTO> agregarTareaAUsuario(@PathVariable Long id,@Valid @RequestBody TareaRequestDTO tarea, BindingResult result) {
        if (!result.hasFieldErrors()) {
            Tarea tareaRequest = genericoDTOConverter.convertirAEntidad(tarea, Tarea.class);
            Usuario usuario = usuarioService.agregarTarea(id, tareaRequest);
            UsuarioResponseDTO usuarioResponse = genericoDTOConverter.convertirADTO(usuario, UsuarioResponseDTO.class);
            return new ResponseEntity<>(usuarioResponse, HttpStatus.CREATED);
        } else {
            String err = result.getFieldErrors().stream()
                    .map(error -> "Campo '" + error.getField() + "' : " + error.getDefaultMessage())
                    .collect(Collectors.joining("; "));
            throw new EntradaInvalidaException("Error al guardar la tarea en el usuario: " + err);
        }

    }

    /*
    @PostMapping("/tareas")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<UsuarioResponseDTO> agregarTarea(@Valid @RequestBody TareaRequestDTO tarea, BindingResult result,Authentication authentication) {
        if(result.hasFieldErrors()){
            String err=result.getFieldErrors().stream()
                    .map(error->"Campo '"+error.getField()+"' : "+error.getDefaultMessage())
                    .collect(Collectors.joining("; "));
            throw new EntradaInvalidaException("Error al guardar la tarea en el usuario: "+err);
        }
        Tarea tareaRequest=genericoDTOConverter.convertirAEntidad(tarea, Tarea.class);
        Usuario usuario=usuarioService.obtenerUsuario(authentication.getName());
        Usuario usuarioResult=usuarioService.agregarTarea(usuario.getIdentificador(), tareaRequest);
        UsuarioResponseDTO usuarioResponse = genericoDTOConverter.convertirADTO(usuarioResult, UsuarioResponseDTO.class);
        return new ResponseEntity<>(usuarioResponse, HttpStatus.CREATED);

    }*/

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('USUARIO_ACTUALIZAR')")
    public ResponseEntity<UsuarioResponseDTO> actualizarUsuario(@PathVariable Long id,@Valid @RequestBody UsuarioRequestDTO usuario, BindingResult result) {
        if(result.hasFieldErrors()){
            String err=result.getFieldErrors().stream()
                    .map(error->"Campo '"+error.getField()+"' : "+error.getDefaultMessage())
                    .collect(Collectors.joining(" ; "));
            throw new EntradaInvalidaException("Error al actualizar el usuario: "+err);
        }
        Usuario usuarioRequest=genericoDTOConverter.convertirAEntidad(usuario,Usuario.class);
        Usuario usuarioActualizar= usuarioService.actualizarUsuario(id, usuarioRequest);
        UsuarioResponseDTO usuarioResponse = genericoDTOConverter.convertirADTO(usuarioActualizar, UsuarioResponseDTO.class);
        return new ResponseEntity<>(usuarioResponse, HttpStatus.OK);
    }
    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('USUARIO_ID_PARCIAL_ACTUALIZAR')")
    public ResponseEntity<UsuarioResponseDTO> actualizarUsuarioIDParcial(@PathVariable Long id,
                                                                       @RequestBody UsuarioRequestDTO usuarioRequestDTO, BindingResult result) {
        if(!usuarioRequestDTO.tieneCampo()){
            String err=result.getFieldErrors().stream()
                    .map(error->"Campo '"+error.getField()+"' : "+error.getDefaultMessage())
                    .collect(Collectors.joining(" ; "));
            throw  new EntradaInvalidaException("Error al actualizar el usuario almenos uno de estos campos debe ser valido "+err);
        }
        Usuario usuarioEncontrar = usuarioService.obtenerUsuario(id);
        boolean requiereLogin= usuarioRequestDTO.getUsername()!=null&&!usuarioEncontrar.getUsername().equals(usuarioRequestDTO.getUsername());
        Usuario usuarioRequest=genericoDTOConverter.convertirAEntidad(usuarioRequestDTO,Usuario.class);
        Usuario usuarioActualizar= usuarioService.actualizarUsuario(id, usuarioRequest);
        UsuarioResponseDTO usuarioResponse = genericoDTOConverter.convertirADTO(usuarioActualizar, UsuarioResponseDTO.class);
        usuarioResponse.setRequiereLogin(requiereLogin);
        return new ResponseEntity<>(usuarioResponse, HttpStatus.OK);
    }

    @PatchMapping()
    @PreAuthorize("hasAuthority('USUARIO_PARCIAL_ACTUALIZAR')")
    public ResponseEntity<UsuarioResponseDTO> actualizarUsuarioParcial(@RequestBody UsuarioRequestDTO usuarioRequestDTO, BindingResult result,
                                                                       Authentication authentication) {
        if (usuarioRequestDTO.tieneCampo()) {
            Usuario usuarioRequest = genericoDTOConverter.convertirAEntidad(usuarioRequestDTO, Usuario.class);
            Usuario usuarioEncontrar = usuarioService.obtenerUsuario(authentication.getName());
            boolean requiereLogin= usuarioRequestDTO.getUsername()!=null&&!usuarioEncontrar.getUsername().equals(usuarioRequest.getUsername());
            Usuario usuarioActualizar = usuarioService.actualizarUsuario(usuarioEncontrar.getIdentificador(), usuarioRequest);
            UsuarioResponseDTO usuarioResponse = genericoDTOConverter.convertirADTO(usuarioActualizar, UsuarioResponseDTO.class);
            usuarioResponse.setRequiereLogin(requiereLogin);
            return new ResponseEntity<>(usuarioResponse, HttpStatus.OK);
        } else {
            String err = result.getFieldErrors().stream()
                    .map(error -> "Campo '" + error.getField() + "' : " + error.getDefaultMessage())
                    .collect(Collectors.joining(" ; "));
            throw new EntradaInvalidaException("Error al actualizar el usuario almenos uno de estos campos debe ser valido " + err);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('USUARIO_ELIMINAR')")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id_usuario}/tareas/{id_tarea}")
    @PreAuthorize("hasAuthority('USUARIO_TAREA_ELIMINAR')")
    public ResponseEntity<?> eliminarTareaPorUsuario(@PathVariable Long id_usuario, @PathVariable Long id_tarea){
        Map<String,Object> respuesta =usuarioService.eliminarTarea(id_usuario,id_tarea);
        return new ResponseEntity<>(respuesta,HttpStatus.OK);
    }
    /*
    @DeleteMapping("/tareas/{id_tarea}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<?> eliminarTarea(@PathVariable Long id_tarea, Authentication authentication){
        Usuario usuario=usuarioService.obtenerUsuario(authentication.getName());
        Map<String,Object> respuesta=usuarioService.eliminarTarea(usuario.getIdentificador(),id_tarea);
        return new ResponseEntity<>(respuesta,HttpStatus.OK);
    }*/
}
