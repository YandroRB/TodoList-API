package com.todolist.todo.service;

import com.todolist.todo.enumerator.EstadoTarea;
import com.todolist.todo.exception.EntradaInvalidaException;
import com.todolist.todo.exception.RecursoNoEncontradoException;
import com.todolist.todo.model.Tarea;
import com.todolist.todo.model.Usuario;
import com.todolist.todo.repository.TareaRepository;
import com.todolist.todo.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

@Service
public class TareaService {
    private final TareaRepository tareaRepository;
    private final UsuarioRepository usuarioRepository;

    public TareaService(TareaRepository tareaRepository, UsuarioRepository usuarioRepository) {
        this.tareaRepository = tareaRepository;
        this.usuarioRepository = usuarioRepository;
    }
    public List<Tarea> obtenerTodasTareas() {
        return tareaRepository.findAll();
    }
    public List<Tarea> obtenerTareas(String username){
        return tareaRepository.findByUsuarioUsername(username);
    }
    public Tarea obtenerTarea(Long id){
        return tareaRepository.findById(id).orElseThrow(()->new RecursoNoEncontradoException("No se encontró la tarea con el id "+id));
    }
    public Tarea guardarTarea(String username,Tarea tarea){
        Optional<Usuario> usuario=usuarioRepository.findByUsername(username);
        tarea.setUsuario(usuario.orElse(null));
        return tareaRepository.save(tarea);
    }
    public void eliminarTarea(Long id){
        if(!tareaRepository.existsById(id)) throw new RecursoNoEncontradoException("No se encontro la tarea con el id "+id);;
        tareaRepository.deleteById(id);
    }
    public Tarea actualizarTarea(Long id,Tarea tarea){
        return tareaRepository.findById(id).map(_tarea->{
            _tarea.setTitulo(tarea.getTitulo()==null? _tarea.getTitulo():tarea.getTitulo());
            _tarea.setDescripcion(tarea.getDescripcion()==null? _tarea.getDescripcion():tarea.getDescripcion());
            _tarea.setEstado(tarea.getEstado()==null?_tarea.getEstado():tarea.getEstado());
            return tareaRepository.save(_tarea);
        }).orElseThrow(()-> new RecursoNoEncontradoException("No se encontro la tarea con el id "+id+" para actualizar los datos") );
    }
    public Tarea actualizarEstado(Long id, EstadoTarea estado){
        Optional<Tarea> tarea = tareaRepository.findById(id);
        return tarea.map(_tarea->{
            if(estado==null)  throw new EntradaInvalidaException("Estado invalida. Estados permitidos: "+ Arrays.toString(EstadoTarea.values()));
            _tarea.setEstado(estado);
            return tareaRepository.save(_tarea);
        }).orElseThrow(()->new RecursoNoEncontradoException("No se encontro la tarea con el id "+id+" para actualizar el estado"));
    }
}
