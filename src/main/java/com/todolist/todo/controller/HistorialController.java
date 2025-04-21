package com.todolist.todo.controller;

import com.todolist.todo.documentation.HistorialControllerDocumentation;
import com.todolist.todo.dto.response.HistorialResponseDTO;
import com.todolist.todo.service.HistorialService;
import com.todolist.todo.service.TareaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tareas")
@RequiredArgsConstructor
public class HistorialController implements HistorialControllerDocumentation {
    private final HistorialService historialService;
    private final TareaService tareaService;

    @Override
    @GetMapping("/{tareaId}/historial")
    @PreAuthorize("hasAuthority('HISTORIAL_TAREA') or hasAuthority('HISTORIAL_USUARIO_TAREA')and @tareaRepository." +
            "existsByIdentificadorAndUsuarioUsername(#tareaId,authentication.name)")
    public ResponseEntity<List<HistorialResponseDTO>> obtenerHistorialTarea(@PathVariable Long tareaId) {
        if(!tareaService.existeTarea(tareaId)) return ResponseEntity.notFound().build();
        List<HistorialResponseDTO> historial=historialService.obtenerHistorialTareas(tareaId);
        return ResponseEntity.ok(historial);
    }
}
