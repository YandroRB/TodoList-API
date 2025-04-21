package com.todolist.todo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todolist.todo.dto.request.TareaExportDTO;
import com.todolist.todo.exception.EntradaInvalidaException;
import com.todolist.todo.exception.RecursoNoEncontradoException;
import com.todolist.todo.model.Tarea;
import com.todolist.todo.model.Usuario;
import com.todolist.todo.repository.CategoriaRepository;
import com.todolist.todo.repository.TareaRepository;
import com.todolist.todo.repository.UsuarioRepository;
import com.todolist.todo.utility.GenericoDTOConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IEService {
    private final TareaRepository tareaRepository;
    private final UsuarioRepository usuarioRepository;
    private final CategoriaRepository categoriaRepository;
    private final ObjectMapper objectMapper;
    private final GenericoDTOConverter genericoDTOConverter;

    public byte[] exportarTareaToJson(String username){
        try{
            List<Tarea> tareas=tareaRepository.findALLMainTareasUsuario(username);
            List<TareaExportDTO> tareasExport= genericoDTOConverter.convertirListADTO(tareas, TareaExportDTO.class);
            return objectMapper.writeValueAsBytes(tareasExport);
        }catch (IOException e){
            throw new RecursoNoEncontradoException(e.getMessage());
        }
    }
    public List<Tarea> importarTareaFromJson(byte[] jsonBytes, String username){
        Usuario usuario=usuarioRepository.findByUsername(username).orElseThrow( () -> new RecursoNoEncontradoException("No se encontro el usuario"));
        try{
            List<TareaExportDTO> tareasDTo=objectMapper.readValue(jsonBytes,objectMapper.getTypeFactory().constructCollectionType(
                    List.class, TareaExportDTO.class
            ));
            List<Tarea> tareas=genericoDTOConverter.convertirListAEntidad(tareasDTo,Tarea.class);
            for(Tarea tarea:tareas){
                tarea.setUsuario(usuario);
            }
            return tareaRepository.saveAll(tareas);
        }catch (IOException e){
            throw new EntradaInvalidaException("No se pudo obtener las tareas del archivo, formato incorrecto");
        }

    }
}
