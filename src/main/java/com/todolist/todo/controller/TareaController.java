package com.todolist.todo.controller;

import com.todolist.todo.dto.request.TareaRequestDTO;
import com.todolist.todo.dto.response.CategoriaResumeDTO;
import com.todolist.todo.dto.response.TareaResponseDTO;
import com.todolist.todo.dto.response.TareaResumenDTO;
import com.todolist.todo.enumerator.EstadoTarea;
import com.todolist.todo.enumerator.TipoRol;
import com.todolist.todo.exception.EntradaInvalidaException;
import com.todolist.todo.exception.RecursoNoEncontradoException;
import com.todolist.todo.model.Tarea;
import com.todolist.todo.service.CategoriaService;
import com.todolist.todo.service.TareaService;
import com.todolist.todo.utility.GenericoDTOConverter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tareas")
public class TareaController {

    private final TareaService tareaService;
    private final GenericoDTOConverter genericoDTOConverter;
    private final CategoriaService categoriaService;

    public TareaController(TareaService tareaService, GenericoDTOConverter genericoDTOConverter, CategoriaService categoriaService) {
        this.tareaService = tareaService;
        this.genericoDTOConverter = genericoDTOConverter;
        this.categoriaService = categoriaService;
    }
    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<TareaResumenDTO> obtenerTareas(Authentication authentication) {
        String username = authentication.getName();
        List<Tarea> tareas = tareaService.obtenerTareas(username);
        return genericoDTOConverter.convertirListADTO(tareas, TareaResumenDTO.class);
    }
    @GetMapping("/todas")
    @PreAuthorize("hasRole('ADMIN')")
    public List<TareaResponseDTO> obtenerTodaTareas(){
        List<Tarea> tareas= tareaService.obtenerTodasTareas();
        return genericoDTOConverter.convertirListADTO(tareas, TareaResponseDTO.class);
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER') and @tareaRepository.existsByIdentificadorAndUsuarioUsername(#id," +
            "authentication.name)")
    public ResponseEntity<TareaResumenDTO> obtenerTareaPorId(@PathVariable Long id) {
        Tarea tarea = tareaService.obtenerTarea(id);
        TareaResumenDTO tareaResponse= genericoDTOConverter.convertirADTO(tarea, TareaResumenDTO.class);
        return ResponseEntity.ok(tareaResponse);
    }
    @GetMapping("/categorias")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')and @categoriaRepository.existsByNombreAndUsuarioUsername(#categoria," +
            "authentication.name)")
    public List<TareaResponseDTO> obtenerTareasPorCategoria(@RequestParam String categoria) {
        if(categoria ==null) throw new EntradaInvalidaException("El parametro 'categoria' no puede ser nulo o vacio");
        List<Tarea> tareas=categoriaService.obtenerTareasPorCategoria(categoria);
        return genericoDTOConverter.convertirListADTO(tareas, TareaResponseDTO.class);

    }
    @DeleteMapping("/{id_tarea}/categorias/{id_categoria}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER') and " +
            "@tareaRepository.existsByIdentificadorAndCategoriasIdentificadorAndUsuarioUsername(#id_tarea,#id_categoria," +
            "authentication.name)")
    public ResponseEntity<TareaResponseDTO> eliminarCategoriaTarea(@PathVariable Long id_tarea, @PathVariable Long id_categoria) {
        Tarea tarea=categoriaService.eliminarCategoria(id_tarea, id_categoria);
        TareaResponseDTO tareaResponse= genericoDTOConverter.convertirADTO(tarea, TareaResponseDTO.class);
        return ResponseEntity.ok(tareaResponse);
    }
    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<TareaResumenDTO> guardarTarea(@Valid @RequestBody TareaRequestDTO tarea, BindingResult result,Authentication authentication) {
        if(result.hasErrors()){
            String err=result.getFieldErrors().stream()
                    .map(error->"Campo '"+error.getField()+"' : "+error.getDefaultMessage())
                    .collect(Collectors.joining("; "));
            throw new EntradaInvalidaException("Error al guardar la tarea: "+err);
        }
        Tarea tareaNueva=genericoDTOConverter.convertirAEntidad(tarea,Tarea.class);
        Tarea tareaGuardada=tareaService.guardarTarea(authentication.getName(),tareaNueva);
        TareaResumenDTO tareaResponse=genericoDTOConverter.convertirADTO(tareaGuardada,TareaResumenDTO.class);
        return new ResponseEntity<>(tareaResponse, HttpStatus.CREATED);
    }

    @PostMapping("/{id_tarea}/categorias/{id_categoria}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER') and " +
            "@tareaRepository.existsByIdentificadorAndCategoriasIdentificadorAndUsuarioUsername(#id_tarea,#id_categoria," +
            "authentication.name)")
    public ResponseEntity<TareaResponseDTO> asignarCategoriaTarea(@PathVariable Long id_tarea, @PathVariable Long id_categoria) {
        Tarea tarea=categoriaService.asignarCategoriaATarea(id_tarea, id_categoria);
        TareaResponseDTO tareaResponse= genericoDTOConverter.convertirADTO(tarea, TareaResponseDTO.class);
        return ResponseEntity.ok(tareaResponse);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER') and @tareaRepository.existsByIdentificadorAndUsuarioUsername(#id," +
            "authentication.name)")
    public ResponseEntity<TareaResumenDTO> actualizarTarea(@PathVariable Long id,@Valid @RequestBody TareaRequestDTO tarea, BindingResult result) {
        if(result.hasFieldErrors()){
            String err=result.getFieldErrors().stream()
                    .map(error->"Campo '"+error.getField()+"' : "+error.getDefaultMessage())
                    .collect(Collectors.joining(" ; "));
            throw new EntradaInvalidaException("Error al actualizar la tarea: "+err);
        }
        Tarea tareaActualizada=genericoDTOConverter.convertirAEntidad(tarea,Tarea.class);
        Tarea tareaGuardar=tareaService.actualizarTarea(id,tareaActualizada);
        TareaResumenDTO tareaResponse=genericoDTOConverter.convertirADTO(tareaGuardar,TareaResumenDTO.class);
        return ResponseEntity.ok(tareaResponse);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER') and @tareaRepository.existsByIdentificadorAndUsuarioUsername(#id," +
            "authentication.name)")
    public ResponseEntity<TareaResumenDTO> actualizarParcialTarea(@PathVariable Long id,@RequestBody TareaRequestDTO tarea, BindingResult result) {
        if(!tarea.tieneCampos()){
            String err=result.getFieldErrors().stream()
                    .map(error->"Campo '"+error.getField()+"' : "+error.getDefaultMessage())
                    .collect(Collectors.joining(" ; "));
            throw new EntradaInvalidaException("Error al hacer actualizacion parcial de la tarea: "+err);
        }
        Tarea tareaActualizada=genericoDTOConverter.convertirAEntidad(tarea,Tarea.class);
        Tarea tareaGuardar=tareaService.actualizarTarea(id,tareaActualizada);
        TareaResumenDTO tareaResponse=genericoDTOConverter.convertirADTO(tareaGuardar,TareaResumenDTO.class);
        return ResponseEntity.ok(tareaResponse);

    }

    @PatchMapping("/{id}/estado/{estado}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')and @tareaRepository.existsByIdentificadorAndUsuarioUsername(#id," +
            "authentication.name)")
    public ResponseEntity<TareaResumenDTO> actualizarEstadoTarea(@PathVariable Long id,
                                                                 @PathVariable EstadoTarea estado){
        Tarea tarea= tareaService.actualizarEstado(id,estado);
        TareaResumenDTO tareaResponse=genericoDTOConverter.convertirADTO(tarea,TareaResumenDTO.class);
        return ResponseEntity.ok(tareaResponse);
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER') and @tareaRepository.existsByIdentificadorAndUsuarioUsername(#id," +
            "authentication.name)")
    public ResponseEntity<Void> eliminarTarea(@PathVariable Long id) {
        tareaService.eliminarTarea(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
