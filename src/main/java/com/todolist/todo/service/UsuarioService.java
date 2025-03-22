package com.todolist.todo.service;

import com.todolist.todo.dto.response.TareaResumenDTO;
import com.todolist.todo.exception.RecursoNoEncontradoException;
import com.todolist.todo.model.Tarea;
import com.todolist.todo.model.Usuario;
import com.todolist.todo.repository.UsuarioRepository;
import com.todolist.todo.utility.GenericoDTOConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final GenericoDTOConverter genericoDTOConverter;
    private final AuthService authService;

    public List<Usuario> obtenerTodosUsuarios(){
        return usuarioRepository.findAll();
    }
    public Usuario obtenerUsuario(String username){
        return usuarioRepository.findByUsername(username).orElseThrow(()->new RecursoNoEncontradoException("Usuario no encontrado"));
    }
    public Usuario obtenerUsuario(Long id){return usuarioRepository.findById(id)
            .orElseThrow(()->new RecursoNoEncontradoException("No se encontró el usuario con el id: "+id));
    }
    public Usuario guardarUsuario(Usuario usuario){
        return authService.registrarUsuario(usuario);
    }
    public void eliminarUsuario(Long id){
        if(!usuarioRepository.existsById(id)) throw new RecursoNoEncontradoException("No se encontro el usuario con el id: "+id);
        usuarioRepository.deleteById(id);
    }
    //Reparar cuando actualiza el username
    public Usuario actualizarUsuario(Long id,Usuario usuario){
        return usuarioRepository.findById(id).map(_tarea->{
            _tarea.setNombre(usuario.getNombre()==null?_tarea.getNombre():usuario.getNombre());
            _tarea.setApellido(usuario.getNombre()==null?_tarea.getApellido():usuario.getApellido());
            _tarea.setEmail(usuario.getEmail()==null?_tarea.getEmail():usuario.getEmail());
            _tarea.setUsername(usuario.getUsername()==null?_tarea.getUsername():usuario.getUsername());
            return usuarioRepository.save(_tarea);
        }).orElseThrow(()-> new RecursoNoEncontradoException("No se encontro el usuario con el id:"+id));
    }
    public Usuario agregarTarea(Long idUsuario, Tarea tarea){
        Usuario usuario = usuarioRepository.findById(idUsuario).orElseThrow(()->new RecursoNoEncontradoException("No se encontro el usuario con el id: "+idUsuario));
        usuario.addTarea(tarea);
        return usuarioRepository.save(usuario);
    }
    public Map<String,Object> eliminarTarea(Long idUsuario, Long idTarea){
        Map<String,Object> respuesta = new HashMap<>();
        Usuario usuario=usuarioRepository.findById(idUsuario).orElseThrow(()->new RecursoNoEncontradoException("No se encontro el usuario con el id: "+idUsuario));
        Tarea tarea=usuario.getTareas().stream().filter(
                t -> t.getIdentificador().equals(idTarea)).findFirst().orElseThrow(()-> new RecursoNoEncontradoException("No se encontro la tarea con el id: "+idTarea));
        usuario.removeTarea(tarea);
        respuesta.put("mensaje","Tarea eliminada exitosamente");
        respuesta.put("timestamp",new Date());
        respuesta.put("tarea",genericoDTOConverter.convertirADTO(tarea, TareaResumenDTO.class));
        usuarioRepository.save(usuario);
        return respuesta;
    }

}
