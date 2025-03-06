package com.todolist.todo.service;

import com.todolist.todo.exception.RolExistenteException;
import com.todolist.todo.exception.RolNoExistenteException;
import com.todolist.todo.model.Rol;
import com.todolist.todo.repository.RolRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RolService {
    private RolRepository rolRepository;

    public Rol guardarRol(String nombre){
        if(rolRepository.findByNombreRol(nombre).isPresent())throw new RolExistenteException("El rol ya existe");
        Rol rol = new Rol();
        rol.setNombreRol(nombre);
        return rolRepository.save(rol);
    }
    public Rol buscarRol(String nombre){
        return rolRepository.findByNombreRol(nombre).
                orElseThrow(()-> new RolNoExistenteException("No existe el rol "+nombre));
    }

}
