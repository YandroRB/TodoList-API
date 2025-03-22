package com.todolist.todo.service;

import com.todolist.todo.exception.RecursoNoEncontradoException;
import com.todolist.todo.model.Permiso;
import com.todolist.todo.model.Rol;
import com.todolist.todo.model.Usuario;
import com.todolist.todo.repository.PermisoRepository;
import com.todolist.todo.repository.RolRepository;
import com.todolist.todo.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class RolService {
    private final RolRepository rolRepository;
    private final PermisoRepository permisoRepository;
    private final UsuarioRepository usuarioRepository;

    public List<Rol> obtenerRoles(){
        return rolRepository.findAll();
    }
    public Rol crearObtenerRol(String nombre) {
        return rolRepository.findByNombreRol(nombre).orElseGet(()->
                {
                    Rol rol = new Rol();
                    rol.setNombreRol(nombre);
                    return rolRepository.save(rol);
                });
    }
    public Rol asignarPermisos(String rol,Set<Permiso> permisos) {
        Rol rolEncontrado = rolRepository.findByNombreRol(rol).orElseThrow(()->new RuntimeException("No se encontro el rol "+rol));
        rolEncontrado.getPermisos().addAll(permisos);
        return rolRepository.save(rolEncontrado);
    }
    public Rol editarRol(String rolNombre,Rol rolEditado){
        if(rolNombre.equals("USER") || rolNombre.equals("ADMIN")) throw new RuntimeException("No se puede eliminar roles criticos");
        return rolRepository.findByNombreRol(rolNombre).
                map(rol -> {
                    if(rolEditado.getNombreRol()!=null && !rolEditado.getNombreRol().equals(rol.getNombreRol())){
                        rol.setNombreRol(rolEditado.getNombreRol());
                        return rolRepository.save(rol);
                    }
                    return rol;
                })
                .orElseThrow(()->new RuntimeException("No se encontro el rol con el nombre: "+ rolNombre));
    }
    public void eliminarRol(String nombreRol){
        if(nombreRol.equals("USER") || nombreRol.equals("ADMIN")) throw new RuntimeException("No se puede eliminar roles criticos");
       Rol rol=rolRepository.findByNombreRol(nombreRol).orElseThrow(()->new RecursoNoEncontradoException("No se encontro el rol con el nombre "+nombreRol));
       Rol rolUser=rolRepository.findByNombreRol("USER").orElseThrow(()->new RuntimeException("Rol critico no encontrado"));
       List<Usuario> usuarios=usuarioRepository.findByRolesContaining(rol);
       for(Usuario u:usuarios){
           u.getRoles().remove(rol);
           u.getRoles().add(rolUser);
       }
       if(!usuarios.isEmpty()) usuarioRepository.saveAll(usuarios);
       rolRepository.delete(rol);
    }
    public Rol asignarPermiso(String rol,String permiso){
        if(rol.equals("ADMIN")) throw new RuntimeException("No se puede eliminar roles criticos");
        Rol rolEncontrado=rolRepository.findByNombreRol(rol).orElseThrow(()->new RuntimeException("No se encontro el rol "+ rol));
        Permiso permisoEncontrado=permisoRepository.findByNombrePermiso(permiso).
                orElseThrow(()->new RuntimeException("No se encontro el permiso "+ permiso));
        rolEncontrado.getPermisos().add(permisoEncontrado);
        return rolRepository.save(rolEncontrado);
    }
    public Rol revocarPermiso(String rol,String permiso){
        if(rol.equals("ADMIN")) throw new RuntimeException("No se puede eliminar roles criticos");
        Rol rolEncontrado=rolRepository.findByNombreRol(rol).orElseThrow(()->new RuntimeException("No se encontro el rol "+ rol));
        Permiso permisoEncontrado=permisoRepository.findByNombrePermiso(permiso).
                orElseThrow(()->new RuntimeException("No se encontro el permiso "+ permiso));
        rolEncontrado.getPermisos().remove(permisoEncontrado);
        return rolRepository.save(rolEncontrado);
    }
}
