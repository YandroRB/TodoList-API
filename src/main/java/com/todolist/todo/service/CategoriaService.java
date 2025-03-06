package com.todolist.todo.service;

import com.todolist.todo.exception.RecursoNoEncontradoException;
import com.todolist.todo.model.Categoria;
import com.todolist.todo.model.Tarea;
import com.todolist.todo.model.Usuario;
import com.todolist.todo.repository.CategoriaRepository;
import com.todolist.todo.repository.TareaRepository;
import com.todolist.todo.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CategoriaService {
    private final CategoriaRepository categoriaRepository;
    private final UsuarioRepository usuarioRepository;
    private final TareaRepository tareaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository, UsuarioRepository usuarioRepository, TareaRepository tareaRepository) {
        this.categoriaRepository = categoriaRepository;
        this.usuarioRepository = usuarioRepository;
        this.tareaRepository = tareaRepository;
    }

    public List<Categoria> obtenerCategoriasPorUsuario(String username) {
        return categoriaRepository.findByUsuarioUsername(username);
    }
    public List<Categoria> obtenerCategoriasTarea(Long id) {
        if(!tareaRepository.existsById(id)) throw new RecursoNoEncontradoException("No se encontro la tarea con el id"+id);
        return categoriaRepository.findByTareasIdentificador(id);
    }
    public List<Tarea> obtenerTareasPorCategoria(String categoria) {
        return tareaRepository.findByCategoriasNombre(categoria);
    }
    public Tarea eliminarCategoria(Long id_tarea,Long id_categoria) {
        Tarea tarea = tareaRepository.findById(id_tarea).orElseThrow(()->new RecursoNoEncontradoException("No se encontro la tarea"));
        Categoria categoria= categoriaRepository.findById(id_categoria).orElseThrow(()->new RecursoNoEncontradoException("No se encontro la categoria"));
        tarea.removeCategoria(categoria);
        return tareaRepository.save(tarea);
    }
    public void eliminarCategoria(Long id_categoria) {
        Categoria categoria= categoriaRepository.findById(id_categoria).orElseThrow(()->new RecursoNoEncontradoException("No se encontro la categoria"));
        for(Tarea tarea:categoria.getTareas()){
            tarea.removeCategoria(categoria);
        }
        categoria.setUsuario(null);
        tareaRepository.saveAll(categoria.getTareas());
        categoriaRepository.delete(categoria);
    }
    public Categoria actualizarNombre(Long id,Categoria categoria){
        Categoria categoriaActualizada= categoriaRepository.findById(id).orElseThrow(()->new RecursoNoEncontradoException("No se encontro la categoria"));
        categoriaActualizada.setNombre(categoria.getNombre());
        return categoriaRepository.save(categoriaActualizada);
    }
    public Tarea asignarCategoriaATarea(Long id_tarea,Long id_categoria){
        Tarea tarea = tareaRepository.findById(id_tarea).orElseThrow(()->new RecursoNoEncontradoException("No se encontro la tarea"));
        Categoria categoria = categoriaRepository.findById(id_categoria).orElseThrow(()->new RecursoNoEncontradoException("No se encontro la categoria"));
        tarea.addCategoria(categoria);
        return tareaRepository.save(tarea);
    }
    public Categoria guardarCategoria(String username,Categoria categoria) {
        Optional<Usuario> usuario = usuarioRepository.findByUsername(username);
        categoria.setUsuario(usuario.orElseThrow(()-> new RecursoNoEncontradoException("No se encontro el usuario")));
        return categoriaRepository.save(categoria);
    }


}
