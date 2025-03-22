package com.todolist.todo.initializer;


import com.todolist.todo.model.Rol;
import com.todolist.todo.model.Usuario;
import com.todolist.todo.repository.UsuarioRepository;
import com.todolist.todo.service.PermisoService;
import com.todolist.todo.service.RolService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;


@Component
@AllArgsConstructor
public class DatosInitializer implements CommandLineRunner {
    private final RolService rolService;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final PermisoService permisoService;
    @Override
    public void run(String... args){

        //Crear Permisos
        //Tarea
        permisoService.crearObtenerPermiso("USUARIO_TAREA_LEER",
                "Obtiene las tareas del usuario autenticado");
        permisoService.crearObtenerPermiso("TAREA_TODAS_LEER",
                "Obtiene todas las tareas");
        permisoService.crearObtenerPermiso("TAREA_ID_LEER",
                "Obtiene la tarea por su id");
        permisoService.crearObtenerPermiso("TAREA_ID_USUARIO_LEER",
                "Obtiene la tarea por su id y se asegura que le pertenezca esa tarea al usuario autenticado");
        permisoService.crearObtenerPermiso("TAREA_CATEGORIA_LEER",
                "Obtiene las tareas por categoria");
        permisoService.crearObtenerPermiso("TAREA_USUARIO_CATEGORIA_LEER",
                "Obtiene las tareas por categoria y se asegura que la categoria le pertenezca al usuario autenticado");
        permisoService.crearObtenerPermiso("TAREA_CATEGORIA_ELIMINAR",
                "Elimina una categoria de una tarea");
        permisoService.crearObtenerPermiso("TAREA_USUARIO_CATEGORIA_ELIMINAR",
                "Elimina una categoria de una tarea y se asegura que le pertenezca al usuario autenticado");
        permisoService.crearObtenerPermiso("TAREA_GUARDAR",
                "Guarda una tarea nueva");
        permisoService.crearObtenerPermiso("TAREA_ASIGNAR_CATEGORIA",
                "Asigna una categoria a una tarea");
        permisoService.crearObtenerPermiso("TAREA_USUARIO_ASIGNAR_CATEGORIA",
                "Asigna una categoria a una tarea y se asegura que le pertenezca al usuario autenticado");
        permisoService.crearObtenerPermiso("TAREA_ACTUALIZAR",
                "Actualiza los datos de una tarea por nuevos datos que entregue el usuario");
        permisoService.crearObtenerPermiso("TAREA_USUARIO_ACTUALIZAR",
                "Actualiza los datos de una tarea que le pertenezca al usuario autenticado por nuevos datos que entregue el usuario");
        permisoService.crearObtenerPermiso("TAREA_PARCIAL_ACTUALIZAR",
                "Actualiza los datos de una tarea parcialmente");
        permisoService.crearObtenerPermiso("TAREA_USUARIO_PARCIAL_ACTUALIZAR",
                "Actualiza los datos de una tarea parcialmente y se asegura que la tarea le pertenezca al usuario antenticado");
        permisoService.crearObtenerPermiso("TAREA_ESTADO_ACTUALIZAR",
                "Actualiza el estado de una tarea");
        permisoService.crearObtenerPermiso("TAREA_USUARIO_ESTADO_ACTUALIZAR",
                "Actualiza el estado de una tarea que le pertenezca al usuario antenticado");
        permisoService.crearObtenerPermiso("TAREA_ELIMINAR",
                "Elimina una tarea");
        permisoService.crearObtenerPermiso("TAREA_USUARIO_ELIMINAR",
                "Elimina una tarea que le pertenezca al usuario");
        permisoService.crearObtenerPermiso("TAREA_SUBTAREA_GUARDAR",
                "Guarda una subtarea a traves del id tarea");
        permisoService.crearObtenerPermiso("TAREA_SUBTAREA_USUARIO_GUARDAR",
                "Guarda una subtarea verificando primeramente si la tarea le pertenece al usuario");
        permisoService.crearObtenerPermiso("TAREA_SUBTAREA_ACTUALIZAR",
                "Actualiza una sub tarea");
        permisoService.crearObtenerPermiso("TAREA_USUARIO_SUBTAREA_ACTUALIZAR",
                "Actualiza una subtarea asegurandose que la tarea padre que la contiene le pertenezca al usuario");
        permisoService.crearObtenerPermiso("TAREA_SUBTAREA_ELIMINAR",
                "Elimina una subtarea");
        permisoService.crearObtenerPermiso("TAREA_USUARIO_SUBTAREA_ELIMINAR",
                "Elimina una subtarea asegurandose que la tarea padre que la contiene le pertenezca al usuario");
        permisoService.crearObtenerPermiso("TAREA_ESTABLECER_RECORDATORIO_EDITAR",
                "Establece una fecha limite para la tarea");
        permisoService.crearObtenerPermiso("TAREA_ESTABLECER_USUARIO_RECORDATORIO_EDITAR",
                "Se asegura que la tarea le pertenezca al usuario y establece una fecha limite a la tarea");
        permisoService.crearObtenerPermiso("TAREA_OBTENER_RECORDATORIO_LEER",
                "Obtiene todas las tareas que esten a vencer por determinados dias anticipados");
        permisoService.crearObtenerPermiso("TAREA_OBTENER_RECORDATORIO_USUARIO_LEER",
                "Obtiene las tareas del usuario que esten por vencer por determinados dias anticipados");
        permisoService.crearObtenerPermiso("ENVIAR_RECORDATORIOS_DEBUG",
                "Fuera a enviar los emails de recordatorios sin tener que esperar");
        permisoService.crearObtenerPermiso("HISTORIAL_TAREA",
                "Obtiene el historial de una tarea en especifico");
        permisoService.crearObtenerPermiso("HISTORIAL_USUARIO_TAREA",
                "Obtiene el historial de una tarea en especifico que le pertenezca al usuario");
        permisoService.crearObtenerPermiso("TAREA_PRIORIDAD_ACTUALIZAR",
                "Actualiza el estado de una tarea");
        permisoService.crearObtenerPermiso("TAREA_USUARIO_PRIORIDAD_ACTUALIZAR",
                "Actualiza el estado de una tarea asegurandose que le pertenezca al usuario");
        permisoService.crearObtenerPermiso("TAREA_COMPARTIR_ESCRIBIR",
                "Permite compartir la tarea con otro usuario");
        permisoService.crearObtenerPermiso("TAREA_COMPARTIR_USUARIO_ESCRIBIR",
                "Se asegura que la tarea le pertenezca al usuario y permite compartirla");
        permisoService.crearObtenerPermiso("TAREA_DEJAR_COMPARTIR_ELIMINAR",
                "Quita la tarea al usuario");
        permisoService.crearObtenerPermiso("TAREA_DEJAR_COMPARTIR_USUARIO_ELIMINAR",
                "Se asegura que la tarea le pertenezca y le quita la tarea al usuario");
        permisoService.crearObtenerPermiso("TAREAS_COMPARTIDAS_LEER",
                "Obtiene las tareas compartidas con el usuario");
        //Categoria
        permisoService.crearObtenerPermiso("CATEGORIA_LEER",
                "Obtiene las categorias del usuario antentificado");
        permisoService.crearObtenerPermiso("CATEGORIA_TAREA_LEER",
                "Obtiene las categorias de una tarea");
        permisoService.crearObtenerPermiso("CATEGORIA_TAREA_USUARIO_LEER",
                "Obtiene las categorias de una tarea que le pertenezca al usuario");
        permisoService.crearObtenerPermiso("CATEGORIA_ID_ELIMINAR",
                "Elimina una categoria por el id");
        permisoService.crearObtenerPermiso("CATEGORIA_ID_USUARIO_ELIMINAR",
                "Elimina una categoria que le pertenezca al usuario autentificado");
        permisoService.crearObtenerPermiso("CATEGORIA_ID_ACTUALIZAR",
                "Actualiza los datos de una categoria por el id");
        permisoService.crearObtenerPermiso("CATEGORIA_ID_USUARIO_ACTUALIZAR",
                "Actualiza los datos de una categoria que le pertenezca al usuario");
        permisoService.crearObtenerPermiso("CATEGORIA_CREAR",
                "Crea una categoria al nombre del usuario autentificado");


        //Usuario
        permisoService.crearObtenerPermiso("USUARIO_LEER",
                "Obtiene los datos del usuario autentificado");
        permisoService.crearObtenerPermiso("USUARIO_TODOS_LEER",
                "Obtiene todos los datos de los usuarios");
        permisoService.crearObtenerPermiso("USUARIO_ID_LEER",
                "Obtiene los datos del usuario por id");
        permisoService.crearObtenerPermiso("USUARIO_CREAR",
                "Crea un usuario nuevo");
        permisoService.crearObtenerPermiso("USUARIO_TAREA_CREAR",
                "Crea una tarea y se la asigna a un usuario");
        permisoService.crearObtenerPermiso("USUARIO_ACTUALIZAR",
                "Actualiza los datos de un usuario por el id");
        permisoService.crearObtenerPermiso("USUARIO_ID_PARCIAL_ACTUALIZAR",
                "Actualiza los datos de un usuario parcialmente por el id");
        permisoService.crearObtenerPermiso("USUARIO_PARCIAL_ACTUALIZAR",
                "Actualiza los datos del usuario autentificado");
        permisoService.crearObtenerPermiso("USUARIO_ELIMINAR",
                "Elimina un usuario");
        permisoService.crearObtenerPermiso("USUARIO_TAREA_ELIMINAR",
                "Elimina una tarea a un usuario");


        //Permisos
        permisoService.crearObtenerPermiso("PERMISOS_TODOS_LEER",
                "Obtiene todos los permisos");
        permisoService.crearObtenerPermiso("ASIGNAR_PERMISO_ROL",
                "Asigna un permiso a un rol en especifico");

        //Rol
        permisoService.crearObtenerPermiso("ROLES_TODOS_LEER",
                "Permite leer todos los roles");
        permisoService.crearObtenerPermiso("ROL_CREAR",
                "Permite crear un rol");
        permisoService.crearObtenerPermiso("ROL_EDITAR",
                "Permite editar un rol");
        permisoService.crearObtenerPermiso("ROL_ELIMINAR",
                "Permite eliminar un rol");


        //Crear Roles
        rolService.crearObtenerRol("ADMIN");
        rolService.crearObtenerRol("USER");

        //Asignar permisos
        Rol adminRol=rolService.asignarPermisos("ADMIN",new HashSet<>(permisoService.obtenerPermisos()));

        if(usuarioRepository.findByUsername("admin").isEmpty()){
            Usuario admin = new Usuario();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEmail("santiago.rdbn@gmail.com");
            admin.setNombre("Santiago");
            admin.setApellido("Rodriguez Bone");
            admin.getRoles().add(adminRol);
            admin.setCuentaVerificada(true);
            usuarioRepository.save(admin);
        }

    }
}
