package com.todolist.todo.service;

import com.todolist.todo.model.Permiso;
import com.todolist.todo.repository.PermisoRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class PermisoService {
    private final PermisoRepository permisoRepository;

    public Permiso crearObtenerPermiso(String permiso,String descripcion){
        return permisoRepository.findByNombrePermiso(permiso).orElseGet(()->{
           Permiso nuevoPermiso = new Permiso();
           nuevoPermiso.setNombrePermiso(permiso);
           nuevoPermiso.setDescripcionPermiso(descripcion);
           return permisoRepository.save(nuevoPermiso);
        });
    }
    public List<Permiso> obtenerPermisos(){
        return permisoRepository.findAll();
    }
}
