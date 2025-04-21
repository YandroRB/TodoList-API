package com.todolist.todo.controller;

import com.todolist.todo.documentation.CategoriaControllerDocumentation;
import com.todolist.todo.dto.request.CategoriaRequestDTO;
import com.todolist.todo.dto.response.CategoriaResumeDTO;
import com.todolist.todo.exception.EntradaInvalidaException;
import com.todolist.todo.model.Categoria;
import com.todolist.todo.service.CategoriaService;
import com.todolist.todo.utility.GenericoDTOConverter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
public class CategoriaController implements CategoriaControllerDocumentation {
    private final CategoriaService categoriaService;
    private final GenericoDTOConverter genericoDTOConverter;


    @Override
    @GetMapping
    @PreAuthorize("hasAuthority('CATEGORIA_LEER')")
    public List<CategoriaResumeDTO> obtenerCategoriasPorUsuario(Authentication authentication) {
        String username = authentication.getName();
        List<Categoria> categorias= categoriaService.obtenerCategoriasPorUsuario(username);
        return genericoDTOConverter.convertirListADTO(categorias,CategoriaResumeDTO.class);
    }

    @Override
    @GetMapping("/{id_tarea}")
    @PreAuthorize("hasAuthority('CATEGORIA_TAREA_LEER') or hasAuthority('CATEGORIA_TAREA_USUARIO_LEER') and @tareaRepository.existsByIdentificadorAndUsuarioUsername(#id_tarea," +
            "authentication.name)")
    public List<CategoriaResumeDTO> obtenerCategoriasPorTarea(@PathVariable Long id_tarea){
        List<Categoria> categorias=categoriaService.obtenerCategoriasTarea(id_tarea);
        return genericoDTOConverter.convertirListADTO(categorias,CategoriaResumeDTO.class);
    }

    @Override
    @DeleteMapping("/{id_categoria}")
    @PreAuthorize("hasAuthority('CATEGORIA_ID_ELIMINAR') or hasAuthority('CATEGORIA_ID_USUARIO_ELIMINAR' and @categoriaRepository.existsByIdentificadorAndUsuarioUsername(#id_categoria," +
            "authentication.name))")
    public ResponseEntity<?> eliminarCategoria(@PathVariable Long id_categoria){
        categoriaService.eliminarCategoria(id_categoria);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    @PutMapping("/{id_categoria}")
    @PreAuthorize("hasAuthority('CATEGORIA_ID_ACTUALIZAR') or hasAuthority('CATEGORIA_ID_USUARIO_ACTUALIZAR' and @categoriaRepository.existsByIdentificadorAndUsuarioUsername(#id_categoria," +
            "authentication.name))")
    public ResponseEntity<CategoriaResumeDTO> actualizarCategoria(@PathVariable Long id_categoria,
                                                                  @Valid @RequestBody CategoriaRequestDTO categoria,
                                                                  BindingResult result){
        if(result.hasFieldErrors()){
            String err=result.getFieldErrors().stream()
                    .map(error->"Campo '"+error.getField()+"' : "+error.getDefaultMessage())
                    .collect(Collectors.joining(" ; "));
            throw new EntradaInvalidaException("Error al actualizar la tarea: "+err);
        }
        Categoria categoriaRequest=genericoDTOConverter.convertirAEntidad(categoria,Categoria.class);
        Categoria categoriaResult=categoriaService.actualizarNombre(id_categoria,categoriaRequest);
        CategoriaResumeDTO categoriaResponse=genericoDTOConverter.convertirADTO(categoriaResult,CategoriaResumeDTO.class);
        return new ResponseEntity<>(categoriaResponse,HttpStatus.OK);
    }

    @Override
    @PostMapping
    @PreAuthorize("hasAuthority('CATEGORIA_CREAR')")
    public ResponseEntity<CategoriaResumeDTO> guardarCategoria(Authentication authentication,
                                                               @Valid @RequestBody CategoriaRequestDTO categoria,
                                                               BindingResult result){
        if(result.hasFieldErrors()){
            String err=result.getFieldErrors().stream()
                    .map(error->"Campo '"+error.getField()+"' : "+error.getDefaultMessage())
                    .collect(Collectors.joining(" ; "));
            throw new EntradaInvalidaException("Error al actualizar la tarea: "+err);
        }
        String username = authentication.getName();
        Categoria categoriaRequest=genericoDTOConverter.convertirAEntidad(categoria,Categoria.class);
        Categoria categoriaResult=categoriaService.guardarCategoria(username,categoriaRequest);
        CategoriaResumeDTO categoriaResponse=genericoDTOConverter.convertirADTO(categoriaResult,CategoriaResumeDTO.class);
        return new ResponseEntity<>(categoriaResponse,HttpStatus.CREATED);
    }
}
