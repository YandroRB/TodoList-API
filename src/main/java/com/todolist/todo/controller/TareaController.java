package com.todolist.todo.controller;

import com.todolist.todo.dto.request.TareaRecordatorioRequestDTO;
import com.todolist.todo.dto.request.TareaRequestDTO;

import com.todolist.todo.dto.response.TareaResponseDTO;
import com.todolist.todo.dto.response.TareaResumenDTO;
import com.todolist.todo.enumerator.EstadoTarea;
import com.todolist.todo.enumerator.PrioridadTarea;
import com.todolist.todo.exception.EntradaInvalidaException;

import com.todolist.todo.model.Tarea;
import com.todolist.todo.service.CategoriaService;
import com.todolist.todo.service.TareaService;
import com.todolist.todo.utility.GenericoDTOConverter;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
    @PreAuthorize("hasAuthority('USUARIO_TAREA_LEER')")
    public List<TareaResponseDTO> obtenerTareas(@RequestParam(required = false) String sortby,Authentication authentication) {
        String username = authentication.getName();
        if(sortby!=null&&sortby.equalsIgnoreCase("asc")){
            List<Tarea> tareas = tareaService.obtenerTareasOrderBy(true,username);
            return genericoDTOConverter.convertirListADTO(tareas, TareaResponseDTO.class);
        }
        if(sortby!=null&&sortby.equalsIgnoreCase("desc")){
            List<Tarea> tareas = tareaService.obtenerTareasOrderBy(false,username);
            return genericoDTOConverter.convertirListADTO(tareas, TareaResponseDTO.class);
        }
        List<Tarea> tareas = tareaService.obtenerTareas(username);
        return genericoDTOConverter.convertirListADTO(tareas, TareaResponseDTO.class);
    }

    @GetMapping("/todas")
    @PreAuthorize("hasAuthority('TAREA_TODAS_LEER')")
    public List<TareaResponseDTO> obtenerTodaTareas(@RequestParam(required = false) String sortby){
        if(sortby!=null&&sortby.equalsIgnoreCase("asc")){
            List<Tarea> tareas = tareaService.obtenerTodasTareasOrderBy(true);
            return genericoDTOConverter.convertirListADTO(tareas, TareaResponseDTO.class);
        }
        if(sortby!=null&&sortby.equalsIgnoreCase("desc")){
            List<Tarea> tareas=tareaService.obtenerTodasTareasOrderBy(false);
            return genericoDTOConverter.convertirListADTO(tareas, TareaResponseDTO.class);
        }
        List<Tarea> tareas= tareaService.obtenerTodasTareas();
        return genericoDTOConverter.convertirListADTO(tareas, TareaResponseDTO.class);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('TAREA_ID_LEER') or hasAuthority('TAREA_ID_USUARIO_LEER') and @tareaRepository.existByIdentificadorAndUsername(#id," +
            "authentication.name)")
    public ResponseEntity<TareaResumenDTO> obtenerTareaPorId(@PathVariable Long id) {
        Tarea tarea = tareaService.obtenerTarea(id);
        TareaResumenDTO tareaResponse= genericoDTOConverter.convertirADTO(tarea, TareaResumenDTO.class);
        return ResponseEntity.ok(tareaResponse);
    }

    @GetMapping("/categorias")
    @PreAuthorize("hasAuthority('TAREA_CATEGORIA_LEER') or hasAuthority('TAREA_USUARIO_CATEGORIA_LEER')and @categoriaRepository.existsByNombreAndUsuarioUsername(#categoria," +
            "authentication.name)")
    public List<TareaResponseDTO> obtenerTareasPorCategoria(@RequestParam String categoria) {
        if(categoria ==null) throw new EntradaInvalidaException("El parametro 'categoria' no puede ser nulo o vacio");
        List<Tarea> tareas=categoriaService.obtenerTareasPorCategoria(categoria);
        return genericoDTOConverter.convertirListADTO(tareas, TareaResponseDTO.class);

    }

    @GetMapping("/compartidas")
    @PreAuthorize("hasAuthority('TAREAS_COMPARTIDAS_LEER')")
    public List<TareaResponseDTO> obtenerTareasCompartidas(Authentication authentication) {
        String username = authentication.getName();
        List<Tarea> tareas = tareaService.obtenerTareasCompartidas(username);
        return genericoDTOConverter.convertirListADTO(tareas, TareaResponseDTO.class);
    }

    @DeleteMapping("/{id_tarea}/categorias/{id_categoria}")
    @PreAuthorize("hasAuthority('TAREA_CATEGORIA_ELIMINAR') or hasAuthority('TAREA_USUARIO_CATEGORIA_ELIMINAR') and " +
            "@tareaRepository.existsByIdentificadorAndCategoriasIdentificadorAndUsuarioUsername(#id_tarea,#id_categoria," +
            "authentication.name)")
    public ResponseEntity<TareaResponseDTO> eliminarCategoriaTarea(@PathVariable Long id_tarea, @PathVariable Long id_categoria) {
        Tarea tarea=categoriaService.eliminarCategoria(id_tarea, id_categoria);
        TareaResponseDTO tareaResponse= genericoDTOConverter.convertirADTO(tarea, TareaResponseDTO.class);
        return ResponseEntity.ok(tareaResponse);
    }

    @DeleteMapping("/{id_tarea}/subtarea/{id_sub}")
    @PreAuthorize("hasAuthority('TAREA_SUBTAREA_ELIMINAR') or hasAuthority('TAREA_USUARIO_SUBTAREA_ELIMINAR' and" +
            " @tareaRepository.existByIdentificadorAndUsername(#id_tarea,authentication.name))")
    public ResponseEntity<Void> eliminarSubTarea(@PathVariable Long id_tarea, @PathVariable Long id_sub) {
        tareaService.eliminarsubTarea(id_tarea, id_sub);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('TAREA_GUARDAR')")
    public ResponseEntity<TareaResumenDTO> guardarTarea(@Valid @RequestBody TareaRequestDTO tarea,
                                                        BindingResult result,Authentication authentication) {

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

    @PostMapping("/{id_tarea}/subtarea")
    @PreAuthorize("hasAuthority('TAREA_SUBTAREA_GUARDAR')" +
            " or (hasAuthority('TAREA_SUBTAREA_USUARIO_GUARDAR')and" +
            "@tareaRepository.existByIdentificadorAndUsername(#id_tarea,authentication.name))")
    public ResponseEntity<TareaResponseDTO> guardarSubTarea(@PathVariable Long id_tarea,@Valid @RequestBody TareaRequestDTO tareaRequestDTO, BindingResult result) {
        if (!result.hasFieldErrors()) {
            Tarea tareaRequest = genericoDTOConverter.convertirAEntidad(tareaRequestDTO, Tarea.class);
            Tarea tarea=tareaService.guardarSubTarea(id_tarea, tareaRequest);
            TareaResponseDTO tareaResponse= genericoDTOConverter.convertirADTO(tarea, TareaResponseDTO.class);
            return ResponseEntity.ok(tareaResponse);
        } else {
            String err = result.getFieldErrors().stream()
                    .map(error -> "Campo '" + error.getField() + "' : " + error.getDefaultMessage())
                    .collect(Collectors.joining(" ; "));
            throw new EntradaInvalidaException("Error al actualizar la tarea: " + err);
        }
    }
    @PostMapping("/{id_tarea}/categorias/{id_categoria}")
    @PreAuthorize("hasAuthority('TAREA_ASIGNAR_CATEGORIA') or hasAuthority('TAREA_USUARIO_ASIGNAR_CATEGORIA') and " +
            "@tareaRepository.existsByIdentificadorAndCategoriasIdentificadorAndUsuarioUsername(#id_tarea,#id_categoria," +
            "authentication.name)")
    public ResponseEntity<TareaResponseDTO> asignarCategoriaTarea(@PathVariable Long id_tarea, @PathVariable Long id_categoria) {
        Tarea tarea=categoriaService.asignarCategoriaATarea(id_tarea, id_categoria);
        TareaResponseDTO tareaResponse= genericoDTOConverter.convertirADTO(tarea, TareaResponseDTO.class);
        return ResponseEntity.ok(tareaResponse);
    }
    @PostMapping("/{tareaId}/compartir/{usuarioId}")
    @PreAuthorize("hasAuthority('TAREA_COMPARTIR_ESCRIBIR') or hasAuthority('TAREA_COMPARTIR_USUARIO_ESCRIBIR') and " +
            "@tareaRepository.existsByIdentificadorAndUsuarioUsername(#tareaId,authentication.name)")
    public ResponseEntity<TareaResponseDTO> compartirTarea(@PathVariable Long tareaId, @PathVariable Long usuarioId) {
        Tarea tarea=tareaService.compartirTarea(tareaId, usuarioId);
        TareaResponseDTO tareaResponse=genericoDTOConverter.convertirADTO(tarea, TareaResponseDTO.class);
        return ResponseEntity.ok(tareaResponse);
    }
    @DeleteMapping("/{tareaId}/compartir/{usuarioId}")
    @PreAuthorize("hasAuthority('TAREA_DEJAR_COMPARTIR_ELIMINAR') or hasAuthority('TAREA_DEJAR_COMPARTIR_USUARIO_ELIMINAR')and" +
            "@tareaRepository.existsByIdentificadorAndUsuarioUsername(#tareaId,authentication.name)")
    public ResponseEntity<TareaResponseDTO> dejarCompartir(@PathVariable Long tareaId, @PathVariable Long usuarioId) {
        Tarea tarea=tareaService.dejarCompartir(tareaId, usuarioId);
        TareaResponseDTO tareaResponse=genericoDTOConverter.convertirADTO(tarea, TareaResponseDTO.class);
        return ResponseEntity.ok(tareaResponse);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('TAREA_ACTUALIZAR') or hasAuthority('TAREA_USUARIO_ACTUALIZAR') and @tareaRepository.existsByIdentificadorAndUsuarioUsername(#id," +
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
    @PutMapping("/{id}/fechalimite")
    @PreAuthorize("hasAuthority('TAREA_ESTABLECER_RECORDATORIO_EDITAR') or hasAuthority('TAREA_ESTABLECER_USUARIO_RECORDATORIO_EDITAR')")
    public ResponseEntity<TareaResponseDTO> establecerRecordatorio(@PathVariable Long id,
                                                        @RequestBody @Valid TareaRecordatorioRequestDTO tareaRecordatorio,
                                                        BindingResult result){
        if (!result.hasFieldErrors()) {
            Tarea tareaRequest = genericoDTOConverter.convertirAEntidad(tareaRecordatorio, Tarea.class);
            tareaRequest.setIdentificador(id);
            tareaRequest.setRecordatorioActivado(true);
            Tarea tareaResult = tareaService.establecerFechaLimite(tareaRequest);
            TareaResponseDTO tareaResponse = genericoDTOConverter.convertirADTO(tareaResult, TareaResponseDTO.class);
            return ResponseEntity.ok(tareaResponse);
        } else {
            String err = result.getFieldErrors().stream()
                    .map(error -> "Campo '" + error.getField() + "' : " + error.getDefaultMessage())
                    .collect(Collectors.joining(" ; "));
            throw new EntradaInvalidaException("Error al actualizar la tarea: " + err);
        }
    }

    @PatchMapping("/{id_tarea}/subtarea/{id_sub}")
    @PreAuthorize("hasAuthority('TAREA_SUBTAREA_ACTUALIZAR') or hasAuthority('TAREA_USUARIO_SUBTAREA_ACTUALIZAR' and" +
            " @tareaRepository.existByIdentificadorAndUsername(#id_tarea,authentication.name))")
    public ResponseEntity<TareaResponseDTO> actualizarSubTarea(@PathVariable Long id_tarea,
                                                              @PathVariable Long id_sub,
                                                              @Valid @RequestBody TareaRequestDTO tarea,
                                                              BindingResult result)
    {
        if (tarea.tieneCampos()) {
            Tarea tareaRequest = genericoDTOConverter.convertirAEntidad(tarea, Tarea.class);
            tareaRequest.setIdentificador(id_sub);
            Tarea tareaResult = tareaService.actualizarSubTarea(id_tarea, tareaRequest);
            TareaResponseDTO tareaResponse = genericoDTOConverter.convertirADTO(tareaResult, TareaResponseDTO.class);
            return new ResponseEntity<>(tareaResponse, HttpStatus.OK);
        } else {
            String err = result.getFieldErrors().stream()
                    .map(error -> "Campo '" + error.getField() + "' : " + error.getDefaultMessage())
                    .collect(Collectors.joining(" ; "));
            throw new EntradaInvalidaException("Error al hacer actualizacion parcial de la tarea: " + err);
        }
    }
    @GetMapping("/recordatorio")
    @PreAuthorize("hasAuthority('TAREA_OBTENER_RECORDATORIO_LEER') or hasAuthority('TAREA_OBTENER_RECORDATORIO_USUARIO_LEER')")
    public ResponseEntity<List<TareaResponseDTO>> obtenerTareasRecordatorio(@RequestParam (defaultValue = "1") Integer dias,
                                                                 Authentication authentication){
        boolean tienePermiso=authentication.getAuthorities().stream().anyMatch(a->a.getAuthority().equals("TAREA_OBTENER_RECORDATORIO_LEER"));
        if(tienePermiso){
            List<Tarea> tareasResult=tareaService.obtenerTareasPorVencer(dias);
            List<TareaResponseDTO> tareasResponse=genericoDTOConverter.convertirListADTO(tareasResult,TareaResponseDTO.class);
            return ResponseEntity.ok(tareasResponse);
        }
        List<Tarea> tareasUsuario=tareaService.obtenerTareasPorVencerPorUsuario(authentication.getName(),dias);
        List<TareaResponseDTO> tareaResponse=genericoDTOConverter.convertirListADTO(tareasUsuario,TareaResponseDTO.class);
        return ResponseEntity.ok(tareaResponse);
    }
    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('TAREA_PARCIAL_ACTUALIZAR') or hasAuthority('TAREA_USUARIO_PARCIAL_ACTUALIZAR') and @tareaRepository.existsByIdentificadorAndUsuarioUsername(#id," +
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
    @PreAuthorize("hasAuthority('TAREA_ESTADO_ACTUALIZAR') or hasAuthority('TAREA_USUARIO_ESTADO_ACTUALIZAR')and @tareaRepository.existByIdentificadorAndUsername(#id," +
            "authentication.name)")
    public ResponseEntity<TareaResumenDTO> actualizarEstadoTarea(@PathVariable Long id,
                                                                 @PathVariable EstadoTarea estado){
        Tarea tarea= tareaService.actualizarEstado(id,estado);
        TareaResumenDTO tareaResponse=genericoDTOConverter.convertirADTO(tarea,TareaResumenDTO.class);
        return ResponseEntity.ok(tareaResponse);
    }

    @PostMapping("/{id}/prioridad/{prioridad}")
    @PreAuthorize("hasAuthority('TAREA_PRIORIDAD_ACTUALIZAR')or hasAuthority('TAREA_USUARIO_PRIORIDAD_ACTUALIZAR' and" +
            "@tareaRepository.existByIdentificadorAndUsername(#id,authentication.name))")
    public ResponseEntity<TareaResumenDTO> actualizarPrioridadTarea(@PathVariable Long id,
                                                                    @PathVariable PrioridadTarea prioridad){
        Tarea tarea=tareaService.establecerPrioridad(id,prioridad);
        TareaResumenDTO tareaResponse=genericoDTOConverter.convertirADTO(tarea,TareaResumenDTO.class);
        return ResponseEntity.ok(tareaResponse);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('TAREA_ELIMINAR') or hasAuthority('TAREA_USUARIO_ELIMINAR') and @tareaRepository.existsByIdentificadorAndUsuarioUsername(#id," +
            "authentication.name)")
    public ResponseEntity<Void> eliminarTarea(@PathVariable Long id) {
        tareaService.eliminarTarea(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
